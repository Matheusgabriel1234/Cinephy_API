package projetos.test.Cinephy.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tb_movies")
public class MovieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String imdbId;

    private String title;
    private String year;
    private String type;
    @OneToMany(mappedBy = "movie",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ReviewEntity> reviews = new ArrayList<>();
    private String genre;
    private String rated;
    private String boxOffice;

    public MovieEntity(Long id, String imdbId, String title, String year, String type, List<ReviewEntity> reviews, String genre, String rated, String boxOffice) {
        this.id = id;
        this.imdbId = imdbId;
        this.title = title;
        this.year = year;
        this.type = type;
        this.reviews = reviews;
        this.genre = genre;
        this.rated = rated;
        this.boxOffice = boxOffice;
    }

    public MovieEntity() {
    }

    public List<ReviewEntity> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewEntity> reviews) {
        this.reviews = reviews;
    }

    public Long getId() {
        return id;
    }


    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public String getBoxOffice() {
        return boxOffice;
    }

    public void setBoxOffice(String boxOffice) {
        this.boxOffice = boxOffice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieEntity that = (MovieEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
