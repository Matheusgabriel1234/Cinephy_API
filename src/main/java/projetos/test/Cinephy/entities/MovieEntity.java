package projetos.test.Cinephy.entities;

import jakarta.persistence.*;

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

    public MovieEntity(Long id, String imdbId, String title, String year, String type) {
        this.id = id;
        this.imdbId = imdbId;
        this.title = title;
        this.year = year;
        this.type = type;
    }

    public MovieEntity() {
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
