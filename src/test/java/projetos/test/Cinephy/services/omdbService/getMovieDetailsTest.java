package projetos.test.Cinephy.services.omdbService;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import projetos.test.Cinephy.DTOs.MoviesDetailsDTO;
import projetos.test.Cinephy.DTOs.ReviewDTO;
import projetos.test.Cinephy.Exceptions.MovieNotFoundException;
import projetos.test.Cinephy.entities.UserEntity;
import projetos.test.Cinephy.services.MovieService;
import projetos.test.Cinephy.services.OmdbService;
import projetos.test.Cinephy.services.ReviewService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class getMovieDetailsTest {
    @InjectMocks
    private OmdbService omdbService;

    @Mock
    private RestTemplate restTemplate;

    @Value("${omdb.api.key}")
    private String apiKey;

    @Mock
    private MovieService movieService ;

    @Mock
    private ReviewService reviewService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        omdbService = new OmdbService( restTemplate,apiKey,reviewService,movieService);
    }

    @Test
    void getMovieDetail_success(){
        String imdbID = "tt1212121";
        String  url = String.format("http://www.omdbapi.com/?apikey=%s&i=%s",apiKey,imdbID);

        MoviesDetailsDTO mockResponse = createMockMovieDetails(imdbID);

        UserEntity user1 = new UserEntity("user1@example.com");
        UserEntity user2 = new UserEntity("user2@example.com");

        when(restTemplate.getForObject(anyString(),eq(MoviesDetailsDTO.class))).thenReturn(mockResponse);
        when(reviewService.getReviewForMovie(imdbID)).thenReturn(createMockReviews());

        MoviesDetailsDTO result = omdbService.getMovieDetails(imdbID);

        assertNotNull(result);
        assertMovieDetails(result,imdbID);
        assertReviews(result.getReviews());
    }

    @Test
   void getMovieDetailsTest_nullResponse(){
     String imdbId = "tt1212121";

     when(restTemplate.getForObject(anyString(),eq(MoviesDetailsDTO.class))).thenReturn(null);

        MovieNotFoundException exception = assertThrows(MovieNotFoundException.class,
                ()-> omdbService.getMovieDetails(imdbId)
                );

        assertEquals("Filme não encontrado",exception.getMessage());

    }

    @Test
    void getMovieDetailsTest_emptyReviews(){
        String imdbId = "tt1212121";
        MoviesDetailsDTO mockResponse = createMockMovieDetails(imdbId);

        when(restTemplate.getForObject(anyString(),eq(MoviesDetailsDTO.class))).thenReturn(mockResponse);
        when(reviewService.getReviewForMovie(imdbId)).thenReturn(List.of());

        MoviesDetailsDTO result = omdbService.getMovieDetails(imdbId);

        assertNotNull(result);
        assertMovieDetails(result,imdbId);
        assertNotNull(result.getReviews());
        assertEquals(0,result.getReviews().size());
    }

    @Test
    void getMovieDetail_reviewServiceError() {
        String imdbID = "tt1212121";
        MoviesDetailsDTO mockResponse = createMockMovieDetails(imdbID);

        when(restTemplate.getForObject(anyString(), eq(MoviesDetailsDTO.class))).thenReturn(mockResponse);
        when(reviewService.getReviewForMovie(imdbID)).thenThrow(new RuntimeException("Erro no serviço de reviews"));

        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> omdbService.getMovieDetails(imdbID)
        );

        assertEquals("Erro no serviço de reviews", exception.getMessage(), "Exception message should match.");
    }


    @Test
    void getMovieDetail_restTemplateError() {
        String imdbID = "tt1212121";

        when(restTemplate.getForObject(anyString(), eq(MoviesDetailsDTO.class))).thenThrow(new RuntimeException("Erro na API OMDB"));

        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> omdbService.getMovieDetails(imdbID)
        );

        assertEquals("Erro na API OMDB", exception.getMessage(), "Exception message should match.");
    }


    @Test
    void getMovieDetail_incompleteDetails() {
        String imdbID = "tt1212121";
        MoviesDetailsDTO mockResponse = new MoviesDetailsDTO();
        mockResponse.setImdbId(imdbID);

        when(restTemplate.getForObject(anyString(), eq(MoviesDetailsDTO.class))).thenReturn(mockResponse);
        when(reviewService.getReviewForMovie(imdbID)).thenReturn(createMockReviews());

        MoviesDetailsDTO result = omdbService.getMovieDetails(imdbID);

        assertNotNull(result, "The result should not be null.");
        assertEquals(imdbID, result.getImdbId(), "IMDb ID should match.");
        assertNull(result.getBoxOffice(), "Box Office should be null.");
        assertNull(result.getGenre(), "Genre should be null.");
        assertNull(result.getType(), "Type should be null.");
        assertNull(result.getYear(), "Year should be null.");
        assertNull(result.getRated(), "Rated should be null.");
        assertNull(result.getTitle(), "Title should be null.");
    }







    private MoviesDetailsDTO createMockMovieDetails(String imdbID) {
        MoviesDetailsDTO movieDetails = new MoviesDetailsDTO();
        movieDetails.setBoxOffice("$1,000,000");
        movieDetails.setGenre("Comedy");
        movieDetails.setType("movie");
        movieDetails.setYear("2021");
        movieDetails.setImdbId(imdbID);
        movieDetails.setRated("PG-13");
        movieDetails.setTitle("Test Movie");
        return movieDetails;
    }

    private List<ReviewDTO> createMockReviews() {
        return List.of(
                new ReviewDTO("Amazing movie!", 5.0, "user1@example.com"),
                new ReviewDTO("Really good.", 4.5, "user2@example.com")
        );
    }


    private void assertMovieDetails(MoviesDetailsDTO result, String imdbID) {
        assertEquals("$1,000,000", result.getBoxOffice(), "Box Office value should match.");
        assertEquals("Comedy", result.getGenre(), "Genre should match.");
        assertEquals("movie", result.getType(), "Type should match.");
        assertEquals("2021", result.getYear(), "Year should match.");
        assertEquals(imdbID, result.getImdbId(), "IMDb ID should match.");
        assertEquals("PG-13", result.getRated(), "Rated should match.");
        assertEquals("Test Movie", result.getTitle(), "Title should match.");
    }


    private void assertReviews(List<ReviewDTO> reviews) {
        assertNotNull(reviews, "Reviews list should not be null.");
        assertEquals(2, reviews.size(), "There should be 2 reviews.");

        assertEquals("user1@example.com", reviews.get(0).getUser());
        assertEquals(5, reviews.get(0).getRating());
        assertEquals("Amazing movie!", reviews.get(0).getComment());

        assertEquals("user2@example.com", reviews.get(1).getUser());
        assertEquals(4.5, reviews.get(1).getRating());
        assertEquals("Really good.", reviews.get(1).getComment());
    }

}
