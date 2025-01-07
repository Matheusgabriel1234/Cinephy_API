package projetos.test.Cinephy.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import projetos.test.Cinephy.DTOs.OmdbResponse;
import projetos.test.Cinephy.DTOs.OmdbSearchResponse;
import projetos.test.Cinephy.entities.MovieEntity;
import projetos.test.Cinephy.entities.UserEntity;
import projetos.test.Cinephy.repository.MovieRepository;
import projetos.test.Cinephy.repository.UserRepository;

@Service
public class OmdbService {

    private final MovieRepository movieRepository;
    private final RestTemplate restTemplate;
    private final String apiKey;


    public OmdbService(MovieRepository movieRepository, RestTemplate restTemplate, @Value("${omdb.api.key}") String apiKey) {
        this.movieRepository = movieRepository;
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    public MovieEntity fetchAndSave(String imdbId){
        String url = String.format("http://www.omdbapi.com/?apikey=%s&i=%s", apiKey, imdbId);
        OmdbResponse response = restTemplate.getForObject(url,OmdbResponse.class);

        if (response == null || response.getImdbId() == null || response.getTitle() == null) {
            throw new RuntimeException("Filme não encontrado na API OMDb com o IMDb ID: " + imdbId);
        }

        MovieEntity movie = new MovieEntity();
        movie.setTitle(response.getTitle());
        movie.setType(response.getType());
        movie.setImdbId(response.getImdbId());
        movie.setYear(response.getYear());

        return movieRepository.save(movie);
    }


    public OmdbSearchResponse searchMovie(String title) {
        String url = String.format("http://www.omdbapi.com/?apikey=%s&s=%s", apiKey, title);

        OmdbSearchResponse response = restTemplate.getForObject(url, OmdbSearchResponse.class);

        if (response == null || "False".equalsIgnoreCase(response.getResponse())) {
            throw new RuntimeException("Erro na busca: " + (response != null ? response.getError() : "Resposta nula"));
        }
        return response;
    }
}
