package project_management__api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project_management__api.entities.TaskEntity;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity,Long> {
    List<TaskEntity> findAllByProjectId(Long projectId);

}
