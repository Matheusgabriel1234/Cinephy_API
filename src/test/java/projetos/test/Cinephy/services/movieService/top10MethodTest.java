package projetos.test.Cinephy.services.movieService;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import projetos.test.Cinephy.DTOs.OmdbResponse;
import projetos.test.Cinephy.Exceptions.InvalidMovieInTop10Exception;
import projetos.test.Cinephy.Exceptions.MovieNotFoundException;
import projetos.test.Cinephy.entities.MovieEntity;
import projetos.test.Cinephy.entities.UserEntity;
import projetos.test.Cinephy.repository.MovieRepository;
import projetos.test.Cinephy.repository.UserRepository;
import projetos.test.Cinephy.services.MovieService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class top10MethodTest {
    @InjectMocks
    private MovieService movieService;

    @Mock
    private RestTemplate restTemplate;

    @Value("${omdb.api.key}")
    private String apiKey;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        movieService = new MovieService(movieRepository, userRepository, restTemplate,   apiKey);
    }

    @Test
    void testAddToTop10_sucess(){
        String imdbId = "tt1772341";

        UserEntity user = new UserEntity();
        user.setTopMovies(new ArrayList<>());

        MovieEntity mockMovie = new MovieEntity();
        mockMovie.setImdbId(imdbId);
        mockMovie.setTitle("Test title");

        when(movieRepository.findByImdbId(imdbId)).thenReturn(Optional.of(mockMovie));
        movieService.addMovieToTop10(imdbId,user);

        assertTrue(user.getTopMovies().contains(mockMovie));
        verify(userRepository,times(1)).save(user);

    }

    @Test
    void testAddToTop10_movieAlreadyInTop10(){
        String imdbID = "tt1772341";
        MovieEntity mockMovie = new MovieEntity();
        mockMovie.setImdbId(imdbID);
        mockMovie.setTitle("Test Title");

        UserEntity user = new UserEntity();
        user.setTopMovies(new ArrayList<>(List.of(mockMovie)));

        when(movieRepository.findByImdbId(imdbID)).thenReturn(Optional.of(mockMovie));

        InvalidMovieInTop10Exception exception = assertThrows(InvalidMovieInTop10Exception.class , ()-> movieService.addMovieToTop10(imdbID,user));

        assertEquals("Esse filme já está no top 10",exception.getMessage());

        verify(userRepository,never()).save(any(UserEntity.class));

    }

    @Test
    void testAddToTop10_userHasMaxMovies(){
        String imdbID = "tt1772341";
        MovieEntity mockMovie = new MovieEntity();
        mockMovie.setImdbId(imdbID);
        mockMovie.setTitle("Test movie");

        UserEntity user = new UserEntity();
        List<MovieEntity> topMovies = new ArrayList<>();

        for(int i = 0;i < 10;i++){
            topMovies.add(mockMovie);
        }
        user.setTopMovies(topMovies);

        when(movieRepository.findByImdbId(imdbID)).thenReturn(Optional.of(mockMovie));

        InvalidMovieInTop10Exception exception = assertThrows(InvalidMovieInTop10Exception.class,
                () -> movieService.addMovieToTop10(imdbID,user)
                );

        assertEquals("Voce ja possui 10 filmes na lista",exception.getMessage());

        verify(userRepository,never()).save(any(UserEntity.class));
    }

    @Test
    void testAddToTop10_movieNotFoundInAPI(){
       String imdbId = "tt1772341";
       UserEntity user = new UserEntity();
       user.setTopMovies(new ArrayList<>());

       when(movieRepository.findByImdbId(imdbId)).thenReturn(Optional.empty());
       when(restTemplate.getForObject(anyString(),eq(OmdbResponse.class))).thenReturn(null);

        MovieNotFoundException exception = assertThrows(MovieNotFoundException.class,
                ()-> movieService.addMovieToTop10(imdbId,user)
                );

        assertEquals( "Filme não encontrado na API OMDb com o IMDb ID: tt1772341",exception.getMessage());
        verify(userRepository,never()).save(any(UserEntity.class));
    }






}
