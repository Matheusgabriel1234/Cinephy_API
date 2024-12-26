package projetos.test.Cinephy.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import projetos.test.Cinephy.DTOs.OmdbResponse;
import projetos.test.Cinephy.DTOs.OmdbSearchResponse;
import projetos.test.Cinephy.entities.MovieEntity;
import projetos.test.Cinephy.repository.MovieRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final RestTemplate restTemplate;
    private final String apiKey;

    public MovieService(MovieRepository movieRepository, RestTemplate restTemplate, @Value("${omdb.api.key}") String apiKey) {
        this.movieRepository = movieRepository;
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    public List<MovieEntity> getAll(){
        return movieRepository.findAll();
    }

    public OmdbSearchResponse searchMovie(String title){
    String url = String.format("http://www.omdbapi.com/?apikey=%s&s=%s", apiKey, title);

    OmdbSearchResponse response = restTemplate.getForObject(url,OmdbSearchResponse.class);

    if (response == null || "False".equalsIgnoreCase(response.getResponse())) {
        throw new RuntimeException("Erro na busca: " + (response != null ? response.getError() : "Resposta nula"));
    }
    return response;
    }
}
