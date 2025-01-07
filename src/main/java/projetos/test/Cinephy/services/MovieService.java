package projetos.test.Cinephy.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import projetos.test.Cinephy.DTOs.OmdbResponse;
import projetos.test.Cinephy.DTOs.OmdbSearchResponse;
import projetos.test.Cinephy.entities.MovieEntity;
import projetos.test.Cinephy.entities.ReviewEntity;
import projetos.test.Cinephy.entities.UserEntity;
import projetos.test.Cinephy.repository.MovieRepository;
import projetos.test.Cinephy.repository.ReviewRepository;
import projetos.test.Cinephy.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final RestTemplate restTemplate;
    private final String apiKey;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;


    public MovieService(MovieRepository movieRepository, RestTemplate restTemplate,@Value("${omdb.api.key}")  String apiKey,
                        UserRepository userRepository, ReviewRepository reviewRepository) {
        this.movieRepository = movieRepository;
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;

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

    public MovieEntity fetchAndSave(String imdbId){
        String url = String.format("http://www.omdbapi.com/?apikey=%s&i=%s", apiKey, imdbId);
        OmdbResponse response = restTemplate.getForObject(url,OmdbResponse.class);

        if(response == null){
            throw new RuntimeException("Filme não existe na api do OMDB");
        }

        MovieEntity movie = new MovieEntity();
        movie.setTitle(response.getTitle());
        movie.setType(response.getType());
        movie.setImdbId(response.getImdbId());
        movie.setYear(response.getYear());

        return movieRepository.save(movie);
    }



    public void addMovieToTop10(String imdbId,UserEntity user){
        MovieEntity movie = movieRepository.findByImdbId(imdbId).orElseGet(() -> fetchAndSave(imdbId));
        if (user.getTopMovies().size() >= 10) {
         throw new RuntimeException("Voce ja possui 10 filmes na lista");
        }
        user.getTopMovies().add(movie);
        userRepository.save(user);
    }

    public void addReview(String movieId,UserEntity user,double rating,String comment){
     MovieEntity movie = movieRepository.findByImdbId(movieId).orElseThrow(
             () -> new RuntimeException("Filme não encontrado"));

        ReviewEntity review = new ReviewEntity();
        review.setComments(comment);
        review.setRating(rating);
        review.setUser(user);
        review.setMovie(movie);
        reviewRepository.save(review);
    }

}
