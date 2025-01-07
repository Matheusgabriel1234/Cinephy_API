package projetos.test.Cinephy.entities;


import jakarta.persistence.*;

import java.util.Objects;
import java.util.Optional;

@Entity
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private MovieEntity movie;

    private String comments;

    private double rating;

    public ReviewEntity(double rating, String comments, MovieEntity movie, UserEntity user, Long id) {
        this.rating = rating;
        this.comments = comments;
        this.movie = movie;
        this.user = user;
        this.id = id;
    }

    public ReviewEntity(){

    }

    public Long getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public MovieEntity getMovie() {
        return movie;
    }

    public void setMovie(MovieEntity movie) {
        this.movie = movie;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewEntity that = (ReviewEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
