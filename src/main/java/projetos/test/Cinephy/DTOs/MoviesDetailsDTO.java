package projetos.test.Cinephy.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import projetos.test.Cinephy.entities.ReviewEntity;

import java.util.List;

public class MoviesDetailsDTO {
    @JsonProperty("imdbID")
    private String imdbId;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Year")
    private String year;

    @JsonProperty("BoxOffice")
    private String boxOffice;

    @JsonProperty("Genre")
    private String genre;

    @JsonProperty("Rated")
    private String rated;

    private List<ReviewDTO> reviews;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getBoxOffice() {
        return boxOffice;
    }

    public void setBoxOffice(String boxOffice) {
        this.boxOffice = boxOffice;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public List<ReviewDTO> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewDTO> reviews) {
        this.reviews = reviews;
    }
}