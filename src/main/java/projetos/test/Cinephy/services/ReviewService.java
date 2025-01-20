package projetos.test.Cinephy.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import projetos.test.Cinephy.DTOs.ReviewDTO;
import projetos.test.Cinephy.Exceptions.InvalidReviewException;
import projetos.test.Cinephy.Exceptions.ReviewNotFoundException;
import projetos.test.Cinephy.Exceptions.UnauthorizedActionException;
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
private final MovieService movieService;

    public ReviewService(MovieRepository movieRepository, ReviewRepository reviewRepository, MovieService movieService) {
        this.movieRepository = movieRepository;
        this.reviewRepository = reviewRepository;
        this.movieService = movieService;
    }

    public ReviewDTO addReview(String movieId, UserEntity user, ReviewDTO reviewDTO){
        MovieEntity movie = movieRepository.findByImdbId(movieId).orElseGet(() -> movieService.fetchAndSave(movieId));

        if(reviewDTO.getRating() < 0 || reviewDTO.getRating() > 10 ){
            throw new InvalidReviewException("A avaliação deve ser de 0 a 10");
        }

        boolean alreadyReviewed = reviewRepository.existsByUserAndMovie(user,movie);

        if(alreadyReviewed){
            throw new InvalidReviewException("Voce ja avaliou este filme");
        }

        ReviewEntity review = new ReviewEntity();
        review.setComment(reviewDTO.getComment());
        review.setRating(reviewDTO.getRating());
        review.setUser(user);
        review.setMovie(movie);
        reviewRepository.save(review);

        return new ReviewDTO( user.getNickName(),review.getRating(),review.getComment());
    }

    public List<ReviewDTO> getReviewForMovie(String imdbID){
        MovieEntity movie = movieRepository.findByImdbId(imdbID).orElseThrow(() -> new RuntimeException("Esse filme não foi encontrado"));


        return movie.getReviews().stream().map(review -> new ReviewDTO(
                review.getUser().getNickName(),
                review.getRating(),
                review.getComment()


        )).collect(Collectors.toList());
    }

    public void deleteYourReview(Long id, UserEntity user){
        ReviewEntity review = reviewRepository.findById(id).orElseThrow(()-> new ReviewNotFoundException("Review não encontrada"));

        if(!review.getUser().equals(user)){
           throw new UnauthorizedActionException("Ação não autorizada");
        }

        reviewRepository.deleteById(id);
    }

    public ReviewDTO editReview(Long id,UserEntity user,ReviewDTO updateReview){
        ReviewEntity existingReview = reviewRepository.findById(id).orElseThrow(()-> new ReviewNotFoundException("Review não encontrada"));

        if(!existingReview.getUser().equals(user)){
            throw new UnauthorizedActionException("Ação não autorizada");
        }

        if(updateReview.getRating() > 10 || updateReview.getRating() < 0){
            throw new InvalidReviewException("Review deve ter ter um valor valido");
        }

        existingReview.setRating(updateReview.getRating());
        existingReview.setComment(updateReview.getComment());

        reviewRepository.save(existingReview);

        return new ReviewDTO(user.getNickName(),existingReview.getRating(),existingReview.getComment());
    }


}
