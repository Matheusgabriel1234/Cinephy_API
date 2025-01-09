package projetos.test.Cinephy.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import projetos.test.Cinephy.DTOs.MoviesDetailsDTO;
import projetos.test.Cinephy.DTOs.OmdbResponse;
import projetos.test.Cinephy.DTOs.OmdbSearchResponse;
import projetos.test.Cinephy.entities.MovieEntity;
import projetos.test.Cinephy.entities.UserEntity;
import projetos.test.Cinephy.repository.MovieRepository;
import projetos.test.Cinephy.repository.UserRepository;

import java.util.Optional;

@Service
public class OmdbService {

    private final MovieRepository movieRepository;
    private final RestTemplate restTemplate;
    private final String apiKey;
    private final ReviewService reviewService;

    public OmdbService(MovieRepository movieRepository, RestTemplate restTemplate, @Value("${omdb.api.key}") String apiKey, ReviewService reviewService) {
        this.movieRepository = movieRepository;
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.reviewService = reviewService;
    }

    public MovieEntity fetchAndSave(String imdbID){
        Optional<MovieEntity> existingMovie = movieRepository.findByImdbId(imdbID);
        if (existingMovie.isPresent()) {
            return existingMovie.get();
        }
        String url = String.format("http://www.omdbapi.com/?apikey=%s&i=%s", apiKey, imdbID);
        OmdbResponse response = restTemplate.getForObject(url,OmdbResponse.class);

        if (response == null || response.getImdbId() == null || response.getTitle() == null) {
            throw new RuntimeException("Filme não encontrado na API OMDb com o IMDb ID: " + imdbID);
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

    public MoviesDetailsDTO getMovieDetails(String imdbID){
        fetchAndSave(imdbID);
        String url = String.format("http://www.omdbapi.com/?apikey=%s&i=%s",apiKey,imdbID);
        MoviesDetailsDTO response = restTemplate.getForObject(url, MoviesDetailsDTO.class);

        if(response == null){
            throw new RuntimeException("Filme não encontrado");
        }

        MoviesDetailsDTO movie = new MoviesDetailsDTO();
        movie.setBoxOffice(response.getBoxOffice());
        movie.setGenre(response.getGenre());
        movie.setType(response.getType());
        movie.setYear(response.getYear());
        movie.setImdbId(response.getImdbId());
        movie.setRated(response.getRated());
        movie.setTitle(response.getTitle());
        movie.setReviews(reviewService.getReviewForMovie(imdbID));

        return movie;
    }
}
