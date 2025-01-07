package projetos.test.Cinephy.services;

import org.springframework.stereotype.Service;
import projetos.test.Cinephy.DTOs.ReviewDTO;
import projetos.test.Cinephy.entities.MovieEntity;
import projetos.test.Cinephy.entities.ReviewEntity;
import projetos.test.Cinephy.entities.UserEntity;
import projetos.test.Cinephy.repository.MovieRepository;
import projetos.test.Cinephy.repository.ReviewRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

private final MovieRepository movieRepository;
private final ReviewRepository reviewRepository;
private final OmdbService omdbService;

    public ReviewService(MovieRepository movieRepository, ReviewRepository reviewRepository, OmdbService omdbService) {
        this.movieRepository = movieRepository;
        this.reviewRepository = reviewRepository;
        this.omdbService = omdbService;
    }

    public ReviewDTO addReview(String movieId, UserEntity user, ReviewDTO reviewDTO){
        MovieEntity movie = movieRepository.findByImdbId(movieId).orElseGet(() -> omdbService.fetchAndSave(movieId));

        if(reviewDTO.getRating() < 0 || reviewDTO.getRating() > 10 ){
            throw new RuntimeException("A avaliação deve ser de 0 a 10");
        }

        boolean alreadyReviewed = reviewRepository.existsByUserAndMovie(user,movie);

        if(alreadyReviewed){
            throw new RuntimeException("Voce ja avaliou este filme");
        }

        ReviewEntity review = new ReviewEntity();
        review.setComment(reviewDTO.getComment());
        review.setRating(reviewDTO.getRating());
        review.setUser(user);
        review.setMovie(movie);
        reviewRepository.save(review);

        return new ReviewDTO( review.getComment(),review.getRating(),user.getEmail());
    }

    public List<ReviewDTO> getReviewForMovie(String imdbId){
        MovieEntity movie = movieRepository.findByImdbId(imdbId).orElseThrow(() -> new RuntimeException("Esse filme não foi encontrado"));


        return movie.getReviews().stream().map(review -> new ReviewDTO(
                review.getUser().getEmail(),
                review.getRating(),
                review.getComment()


        )).collect(Collectors.toList());
    }

}
