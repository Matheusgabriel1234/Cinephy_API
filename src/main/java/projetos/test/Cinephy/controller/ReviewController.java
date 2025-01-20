package projetos.test.Cinephy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import projetos.test.Cinephy.DTOs.ReviewDTO;
import projetos.test.Cinephy.entities.UserEntity;
import projetos.test.Cinephy.services.ReviewService;
import projetos.test.Cinephy.services.UserService;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final UserService userService;
    private final ReviewService reviewService;

    public ReviewController(UserService userService, ReviewService reviewService) {
        this.userService = userService;
        this.reviewService = reviewService;
    }

    @PostMapping("/{imdbID}")
    public ResponseEntity<ReviewDTO> addReview(
            @PathVariable String imdbID,
            @RequestHeader("Authorization") String token,
            @RequestBody ReviewDTO reviewDTO) {
        UserEntity authUser = userService.getUserFromToken(token);
        ReviewDTO review = reviewService.addReview(imdbID,authUser,reviewDTO);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteYourReview(@PathVariable Long id,@RequestHeader("Authorization") String token){
     UserEntity authUser = userService.getUserFromToken(token);
     reviewService.deleteYourReview(id,authUser);
     return ResponseEntity.noContent().build();
    };

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateYourReview(@PathVariable Long id,@RequestHeader("Authorization") String token,@RequestBody ReviewDTO updateReview) {
        UserEntity authUser = userService.getUserFromToken(token);
        ReviewDTO review = reviewService.editReview(id, authUser, updateReview);
        return ResponseEntity.ok().body(review);
    }

}
