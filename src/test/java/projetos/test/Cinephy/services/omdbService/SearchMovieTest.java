package projetos.test.Cinephy.services.omdbService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import projetos.test.Cinephy.DTOs.OmdbSearchResponse;
import projetos.test.Cinephy.Exceptions.MovieNotFoundException;
import projetos.test.Cinephy.services.MovieService;
import projetos.test.Cinephy.services.OmdbService;
import projetos.test.Cinephy.services.ReviewService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
public class SearchMovieTest {
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
    void testSearchMovie_success(){
        String title = "Test Movie";
        String url = String.format("http://www.omdbapi.com/?apikey=%s&s=%s",apiKey,title);

        OmdbSearchResponse mockResponse = new OmdbSearchResponse();
        mockResponse.setResponse("True");

        when(restTemplate.getForObject(anyString(),eq(OmdbSearchResponse.class))).thenReturn(mockResponse);

        OmdbSearchResponse response = omdbService.searchMovie(title);
        assertNotNull(response,"The response must not be null");
        assertEquals("True",response.getResponse());
    }

    @Test
    void testSearchMovie_notFound(){
        String title = "Test movie";
        String url = String.format("http://www.omdbapi.com/?apikey=%s&s=%s",apiKey,title);

        OmdbSearchResponse mockResponse = new OmdbSearchResponse();
        mockResponse.setResponse("False");
        mockResponse.setError("Movie not found!");

        when(restTemplate.getForObject(anyString(),eq(OmdbSearchResponse.class))).thenReturn(mockResponse);

        MovieNotFoundException exception = assertThrows(MovieNotFoundException.class,
                ()-> omdbService.searchMovie(title)
        );

        assertEquals("Erro na busca: Movie not found!",exception.getMessage());
    }

    @Test
    void testSearchMovie_nullResponse(){
        String title = "Null movie";
        String url = String.format("http://www.omdbapi.com/?apikey=%s&s=%s",apiKey,title);

        when(restTemplate.getForObject(anyString(),eq(OmdbSearchResponse.class))).thenReturn(null);

        MovieNotFoundException exception = assertThrows(MovieNotFoundException.class,
                ()->omdbService.searchMovie(title)
        );

        assertEquals("Erro na busca: Resposta nula",exception.getMessage());


    }

}
