package project_management__api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project_management__api.entities.RefreshTokenEntity;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity,Long> {

    Optional<RefreshTokenEntity> findByToken(String token);
    Optional<RefreshTokenEntity> findByUser_Username(String username);

}
