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
    @Column(name = "release_year")
    private String year;
    private String type;
    @OneToMany(mappedBy = "movie",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ReviewEntity> reviews = new ArrayList<>();


    public MovieEntity(Long id, String imdbId, String title, String year, String type, List<ReviewEntity> reviews) {
        this.id = id;
        this.imdbId = imdbId;
        this.title = title;
        this.year = year;
        this.type = type;
        this.reviews = reviews;
        ;
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
