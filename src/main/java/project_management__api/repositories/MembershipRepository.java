package project_management__api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project_management__api.entities.MembershipEntity;

import java.util.Optional;


public interface MembershipRepository extends JpaRepository<MembershipEntity,Long> {
    Optional<MembershipEntity> findByProjectEntity_ProjectId(Long id);
    Optional<MembershipEntity> findByUserMembershipEntities_UserEntity_UserId(Long userId);}
