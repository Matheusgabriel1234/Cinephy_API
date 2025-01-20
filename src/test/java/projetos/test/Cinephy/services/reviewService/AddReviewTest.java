package projetos.test.Cinephy.services.reviewService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import projetos.test.Cinephy.DTOs.ReviewDTO;
import projetos.test.Cinephy.Exceptions.InvalidMovieInTop10Exception;
import projetos.test.Cinephy.Exceptions.InvalidReviewException;
import projetos.test.Cinephy.entities.MovieEntity;
import projetos.test.Cinephy.entities.UserEntity;
import projetos.test.Cinephy.repository.MovieRepository;
import projetos.test.Cinephy.repository.ReviewRepository;
import projetos.test.Cinephy.services.MovieService;
import projetos.test.Cinephy.services.OmdbService;
import projetos.test.Cinephy.services.ReviewService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AddReviewTest {
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
    void addReviewTest_success(){
        String imdbID = "tt9999999";
        UserEntity user = new UserEntity();
        ReviewDTO review = new ReviewDTO(user.getNickName(),10,"Filme incrivel");

        MovieEntity movie = new MovieEntity();
        movie.setImdbId(imdbID);

        when(movieRepository.findByImdbId(imdbID)).thenReturn(Optional.of(movie));
        when(reviewRepository.existsByUserAndMovie(user,movie)).thenReturn(false);

        ReviewDTO result = reviewService.addReview(imdbID,user,review);

        assertNotNull(result);
        assertEquals("Filme incrivel",result.getComment());
        assertEquals(10,result.getRating(),0.01);
        assertEquals(user.getEmail(),result.getUser());
    }

    @Test
    void addReviewTest_invalidRating(){
        String imdbID = "tt9999999";
        UserEntity user = new UserEntity();
        ReviewDTO review = new ReviewDTO("Filme incrivel",11,user.getEmail());

        MovieEntity movieTest = new MovieEntity();
        movieTest.setImdbId(imdbID);

        when(movieRepository.findByImdbId(imdbID)).thenReturn(Optional.of(movieTest));

        InvalidReviewException exception = assertThrows(InvalidReviewException.class, ()-> reviewService.addReview(imdbID,user,review));

        assertEquals("A avaliação deve ser de 0 a 10",exception.getMessage());
    }

    @Test
    void addReviewTest_alreadyReviewed(){
        String imdbID = "tt9999999";
        UserEntity user = new UserEntity();
        ReviewDTO review = new ReviewDTO("Filme incrivel",10,user.getEmail());

        MovieEntity movieTest = new MovieEntity();
        movieTest.setImdbId(imdbID);

        when(movieRepository.findByImdbId(imdbID)).thenReturn(Optional.of(movieTest));
        when(reviewRepository.existsByUserAndMovie(user,movieTest)).thenReturn(true);

        InvalidReviewException exception = assertThrows(InvalidReviewException.class, ()-> reviewService.addReview(imdbID,user,review));

        assertEquals("Voce ja avaliou este filme",exception.getMessage());
    }













}
