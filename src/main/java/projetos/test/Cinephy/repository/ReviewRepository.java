package projetos.test.Cinephy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projetos.test.Cinephy.entities.ReviewEntity;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity,Long> {
}
