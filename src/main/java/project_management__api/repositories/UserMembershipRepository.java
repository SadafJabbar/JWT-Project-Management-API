package project_management__api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project_management__api.entities.UserMembershipEntity;


public interface UserMembershipRepository extends JpaRepository<UserMembershipEntity,Long> {
}
