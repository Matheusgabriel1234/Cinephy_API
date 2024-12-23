package projetos.test.Cinephy.repository;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import projetos.test.Cinephy.entities.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
   Optional<UserEntity> findUserByEmail(String email);
}