package projetos.test.Cinephy.services;

import org.springframework.stereotype.Service;
import projetos.test.Cinephy.Exceptions.InvalidMovieInTop10Exception;
import projetos.test.Cinephy.Exceptions.MovieNotFoundException;
import projetos.test.Cinephy.entities.MovieEntity;
import projetos.test.Cinephy.entities.UserEntity;
import projetos.test.Cinephy.repository.MovieRepository;
import projetos.test.Cinephy.repository.UserRepository;
import java.util.List;


@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final OmdbService omdbService;

    public MovieService(MovieRepository movieRepository, UserRepository userRepository, OmdbService omdbService) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.omdbService = omdbService;
    }

    public List<MovieEntity> getAll(){
        return movieRepository.findAll();
    }

    public void addMovieToTop10(String imdbId,UserEntity user){
        MovieEntity movie = movieRepository.findByImdbId(imdbId).orElseGet(() -> omdbService.fetchAndSave(imdbId));

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

}
