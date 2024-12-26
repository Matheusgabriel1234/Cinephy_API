package projetos.test.Cinephy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projetos.test.Cinephy.entities.MovieEntity;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<MovieEntity,Long> {
    Optional<MovieEntity> findByImdbId(String imdbId);

}
