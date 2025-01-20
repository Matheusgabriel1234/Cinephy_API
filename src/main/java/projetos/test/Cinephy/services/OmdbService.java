package projetos.test.Cinephy.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import projetos.test.Cinephy.DTOs.MoviesDetailsDTO;
import projetos.test.Cinephy.DTOs.OmdbSearchResponse;
import projetos.test.Cinephy.Exceptions.MovieNotFoundException;


import java.util.Optional;

@Service
public class OmdbService {

  ;
    private final RestTemplate restTemplate;
    private final String apiKey;
    private final ReviewService reviewService;
    private final MovieService movieService;

    public OmdbService(RestTemplate restTemplate, @Value("${omdb.api.key}") String apiKey, ReviewService reviewService, MovieService movieService) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.reviewService = reviewService;
        this.movieService = movieService;
    }

    public OmdbSearchResponse searchMovie(String title) {
        String url = String.format("http://www.omdbapi.com/?apikey=%s&s=%s", apiKey, title);

        OmdbSearchResponse response = restTemplate.getForObject(url, OmdbSearchResponse.class);

        if (response == null || "False".equalsIgnoreCase(response.getResponse())) {
            throw new MovieNotFoundException("Erro na busca: " + (response != null ? response.getError() : "Resposta nula"));
        }
        return response;
    }

    public MoviesDetailsDTO getMovieDetails(String imdbID){
        movieService.fetchAndSave(imdbID);
        String url = String.format("http://www.omdbapi.com/?apikey=%s&i=%s",apiKey,imdbID);
        MoviesDetailsDTO response = restTemplate.getForObject(url, MoviesDetailsDTO.class);

        if(response == null){
            throw new MovieNotFoundException("Filme não encontrado");
        }

        MoviesDetailsDTO movie = new MoviesDetailsDTO();
        movie.setType(response.getType());
        movie.setYear(response.getYear());
        movie.setImdbId(response.getImdbId());
        movie.setTitle(response.getTitle());
        movie.setReviews(reviewService.getReviewForMovie(imdbID));

        return movie;
    }
}
