package projetos.test.Cinephy.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import projetos.test.Cinephy.DTOs.OmdbResponse;
import projetos.test.Cinephy.Exceptions.InvalidMovieInTop10Exception;
import projetos.test.Cinephy.Exceptions.MovieNotFoundException;
import projetos.test.Cinephy.entities.MovieEntity;
import projetos.test.Cinephy.entities.UserEntity;
import projetos.test.Cinephy.repository.MovieRepository;
import projetos.test.Cinephy.repository.UserRepository;
import java.util.List;
import java.util.Optional;


@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final String apiKey;

    public MovieService(MovieRepository movieRepository, UserRepository userRepository, RestTemplate restTemplate,  @Value("${omdb.api.key}") String apiKey) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    public List<MovieEntity> getAll(){
        return movieRepository.findAll();
    }

    public void addMovieToTop10(String imdbId,UserEntity user){
        MovieEntity movie = movieRepository.findByImdbId(imdbId).orElseGet(() -> fetchAndSave(imdbId));

        if(movie == null){
            throw new MovieNotFoundException("Esse filme não está registrada na Api");
        }

        if (user.getTopMovies().size() >= 10) {
         throw new InvalidMovieInTop10Exception("Voce ja possui 10 filmes na lista");
        }

        if(user.getTopMovies().contains(movie)){
            throw new InvalidMovieInTop10Exception("Esse filme já está no top 10");
        }
        user.getTopMovies().add(movie);
        userRepository.save(user);
    }

    public void removeFromTop10(String imdbId,UserEntity user){
        MovieEntity movie = movieRepository.findByImdbId(imdbId).orElseGet(() -> fetchAndSave(imdbId));

        if(movie == null){
            throw new MovieNotFoundException("Esse filme não está registrada na Api");
        }

        if (user.getTopMovies().size() >= 10) {
            throw new InvalidMovieInTop10Exception("Voce ja possui 10 filmes na lista");
        }



        user.getTopMovies().remove(movie);
        userRepository.save(user);
    }


    public MovieEntity fetchAndSave(String imdbID){
        Optional<MovieEntity> existingMovie = movieRepository.findByImdbId(imdbID);
        if (existingMovie.isPresent()) {
            return existingMovie.get();
        }
        String url = String.format("http://www.omdbapi.com/?apikey=%s&i=%s", apiKey, imdbID);
        OmdbResponse response = restTemplate.getForObject(url,OmdbResponse.class);

        if (response == null || response.getImdbId() == null || response.getTitle() == null) {
            throw new MovieNotFoundException("Filme não encontrado na API OMDb com o IMDb ID: " + imdbID);
        }

        MovieEntity movie = new MovieEntity();
        movie.setTitle(response.getTitle());
        movie.setType(response.getType());
        movie.setImdbId(response.getImdbId());
        movie.setYear(response.getYear());



        return movieRepository.save(movie);
    }


}
