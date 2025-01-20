package projetos.test.Cinephy.services.reviewService;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import projetos.test.Cinephy.DTOs.ReviewDTO;
import projetos.test.Cinephy.entities.MovieEntity;
import projetos.test.Cinephy.entities.ReviewEntity;
import projetos.test.Cinephy.entities.UserEntity;
import projetos.test.Cinephy.repository.MovieRepository;
import projetos.test.Cinephy.repository.ReviewRepository;
import projetos.test.Cinephy.services.MovieService;
import projetos.test.Cinephy.services.ReviewService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class getReviewForMovieTest {
    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieService movieService;

    @Mock
    private ReviewRepository reviewRepository;



    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        reviewService = new ReviewService(movieRepository,reviewRepository,movieService);
    }

    @Test
    void getReviewForMovie_success(){
       String imdbId = "tt999999";
        MovieEntity movie = new MovieEntity();
        movie.setImdbId(imdbId);

        UserEntity user1 = new UserEntity("user1@gmail.com");
        UserEntity user2 = new UserEntity("user2@gmail.com");

        ReviewEntity review1 = new ReviewEntity(null,user1, movie, "Good Movie",8);
        ReviewEntity review2 = new ReviewEntity(null,user2, movie, "Good movie",8);

        movie.setReviews(List.of(review1,review2));

        when(movieRepository.findByImdbId(imdbId)).thenReturn(Optional.of(movie));

        List<ReviewDTO> result = reviewService.getReviewForMovie(imdbId);

        assertNotNull(result);

        assertEquals(2, result.size(), "There should be 2 reviews.");

        assertEquals("Good Movie", result.get(0).getComment());
        assertEquals(8.0, result.get(0).getRating(), 0.01);
        assertEquals("user1@gmail.com", result.get(0).getUser());

        assertEquals("Good movie", result.get(1).getComment());
        assertEquals(8, result.get(1).getRating(), 0.01);
        assertEquals("user2@gmail.com", result.get(1).getUser());
    }


    @Test
    void getReviewForMovie_movieNotFound() {
        String imdbID = "tt9999999";

        when(movieRepository.findByImdbId(imdbID)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> reviewService.getReviewForMovie(imdbID)
        );

        assertEquals("Esse filme não foi encontrado", exception.getMessage(), "Exception message should match.");
    }
}
