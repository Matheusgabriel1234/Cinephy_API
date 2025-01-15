package projetos.test.Cinephy.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import projetos.test.Cinephy.DTOs.OmdbResponse;
import projetos.test.Cinephy.Exceptions.MovieNotFoundException;
import projetos.test.Cinephy.entities.MovieEntity;
import projetos.test.Cinephy.repository.MovieRepository;
import projetos.test.Cinephy.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = "spring.profiles.active=test")
public class FetchAndSaveMethodTest {
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
void testFetchAndSave_NewMovieSucess() {
    String imdbID = "tt1772341";
    OmdbResponse mockResponse = new OmdbResponse();
    mockResponse.setType("Comedy");
    mockResponse.setTitle("Test Movie Dummy");
    mockResponse.setYear("2011");
    mockResponse.setImdbId(imdbID);

    when(movieRepository.findByImdbId(imdbID)).thenReturn(Optional.empty());
    String url = String.format("http://www.omdbapi.com/?apikey=%s&i=%s", apiKey, imdbID);
    when(restTemplate.getForObject(eq(url), eq(OmdbResponse.class))).thenReturn(mockResponse);
    when(movieRepository.save(any(MovieEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

    MovieEntity result = movieService.fetchAndSave(imdbID);

    assertNotNull(result, "The returned MovieEntity should not be null.");
    assertEquals(imdbID, result.getImdbId(), "IMDb ID should match.");
    assertEquals("Test Movie Dummy", result.getTitle(), "Title should match.");

    verify(movieRepository, times(1)).save(any(MovieEntity.class));
}

@Test
 void testFetchAndSave_MovieExisting(){
        String imdbId = "tt1772341";
        MovieEntity mockMovie = new MovieEntity();
        mockMovie.setImdbId(imdbId);

        when(movieRepository.findByImdbId(imdbId)).thenReturn(Optional.of(mockMovie));

        MovieEntity result = movieService.fetchAndSave(imdbId);

        assertNotNull(result,"The returned method should not be null");
        assertEquals(imdbId,result.getImdbId(),"ImdbID should not be null");
        verify(movieRepository,never()).save(any(MovieEntity.class));

}

@Test
    void testFetchAndSave_MovieNotInApi(){
        String imdbID = "tt1313131";

        when(movieRepository.findByImdbId(imdbID)).thenReturn(Optional.empty());
        when(restTemplate.getForObject(anyString(),eq(OmdbResponse.class))).thenReturn(null);

    MovieNotFoundException exception = assertThrows(MovieNotFoundException.class,
            ()-> movieService.fetchAndSave(imdbID)
            );

    assertEquals("Filme não encontrado na API OMDb com o IMDb ID: " + imdbID, exception.getMessage());

    verify(movieRepository,never()).save(any(MovieEntity.class));
}




}
