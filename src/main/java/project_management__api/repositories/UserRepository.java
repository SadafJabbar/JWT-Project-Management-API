package project_management__api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project_management__api.entities.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByUsername(String username);
}
