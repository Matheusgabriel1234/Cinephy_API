package projetos.test.Cinephy.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import projetos.test.Cinephy.DTOs.OmdbSearchResponse;
import projetos.test.Cinephy.entities.MovieEntity;
import projetos.test.Cinephy.entities.UserEntity;
import projetos.test.Cinephy.services.MovieService;
import projetos.test.Cinephy.services.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;
    private final UserService userService;

    public MovieController(MovieService movieService, UserService userService) {
        this.movieService = movieService;
        this.userService = userService;
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchMovies(@RequestParam String title){
        OmdbSearchResponse searchResponse = movieService.searchMovie(title);
        return new ResponseEntity<>(searchResponse, HttpStatus.OK);
    };



    @PostMapping("/top10/{imdbId}")
    public ResponseEntity<?> addTopMovieToTop10(@PathVariable String imdbId, @RequestHeader("Authorization") String token){
    UserEntity authenticatedUser = userService.getUserFromToken(token);
    movieService.addMovieToTop10(imdbId,authenticatedUser);
    return ResponseEntity.ok("Filme adicionado ao seu top 10");
    }

    @GetMapping("/top10")
    public ResponseEntity<?> getUserTop10(@RequestHeader("Authorization") String token){
        UserEntity authUser = userService.getUserFromToken(token);
        return ResponseEntity.ok(authUser.getTopMovies());
    }


}
