package projetos.test.Cinephy.services;

import org.springframework.stereotype.Service;
import projetos.test.Cinephy.entities.MovieEntity;
import projetos.test.Cinephy.entities.ReviewEntity;
import projetos.test.Cinephy.entities.UserEntity;
import projetos.test.Cinephy.repository.MovieRepository;
import projetos.test.Cinephy.repository.ReviewRepository;

@Service
public class ReviewService {

private final MovieRepository movieRepository;
private final ReviewRepository reviewRepository;

    public ReviewService(MovieRepository movieRepository, ReviewRepository reviewRepository) {
        this.movieRepository = movieRepository;
        this.reviewRepository = reviewRepository;
    }

    public void addReview(String movieId, UserEntity user, double rating, String comment){
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
