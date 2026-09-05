package project_management__api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project_management__api.entities.ProjectEntity;

public interface ProjectRepository extends JpaRepository<ProjectEntity,Long> {
}
