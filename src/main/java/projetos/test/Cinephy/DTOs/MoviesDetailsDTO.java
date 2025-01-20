package projetos.test.Cinephy.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import projetos.test.Cinephy.entities.ReviewEntity;

import java.util.List;


public class MoviesDetailsDTO extends OmdbResponse {
   private List<ReviewDTO> reviews;

    public List<ReviewDTO> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewDTO> reviews) {
        this.reviews = reviews;
    }
}