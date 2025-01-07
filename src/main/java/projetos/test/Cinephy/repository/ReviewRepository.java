package projetos.test.Cinephy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projetos.test.Cinephy.entities.MovieEntity;
import projetos.test.Cinephy.entities.ReviewEntity;
import projetos.test.Cinephy.entities.UserEntity;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity,Long> {
    boolean existsByUserAndMovie(UserEntity user, MovieEntity movie);
}
