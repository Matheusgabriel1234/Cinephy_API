package projetos.test.Cinephy.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import projetos.test.Cinephy.DTOs.OmdbSearchResponse;
import projetos.test.Cinephy.entities.MovieEntity;
import projetos.test.Cinephy.services.MovieService;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

private final MovieService service;

    public MovieController(MovieService service) {
        this.service = service;
    }


    @GetMapping("/search")
    public ResponseEntity<?> searchMovies(@RequestParam String title){
        OmdbSearchResponse searchResponse = service.searchMovie(title);
      return new ResponseEntity<>(searchResponse, HttpStatus.OK);
    };
}
