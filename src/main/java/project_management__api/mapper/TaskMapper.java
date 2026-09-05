package project_management__api.mapper;

import org.springframework.stereotype.Component;
import project_management__api.dtos.TaskRequest;
import project_management__api.dtos.TaskResponse;
import project_management__api.entities.TaskEntity;

@Component
public class TaskMapper {
    public TaskEntity transformToTaskEntity(TaskRequest taskRequest,Long projectId){

        return TaskEntity.builder()
                .title(taskRequest.title())
                .description(taskRequest.description())
                .status(taskRequest.status())
                .priority(taskRequest.priority())
                .projectId(projectId)
                .build();
    }
    public TaskResponse transfromToTaskResponse(TaskEntity taskEntity){

        return TaskResponse.builder()
                .taskId(taskEntity.getTaskId())
                .title(taskEntity.getTitle())
                .description(taskEntity.getDescription())
                .status(taskEntity.getStatus())
                .priority(taskEntity.getPriority())
                .createdAt(taskEntity.getCreatedAt())
                .projectId(taskEntity.getProjectId())
                .build();
    }
    public TaskEntity updateTaskEntity(TaskEntity taskEntity,
                                       TaskRequest taskRequest,
                                       Long projectId){
        taskEntity.setTitle(taskRequest.title());
        taskEntity.setDescription(taskRequest.description());
        taskEntity.setStatus(taskRequest.status());
        taskEntity.setPriority(taskRequest.priority());
        taskEntity.setProjectId(projectId);
        return taskEntity;
    }
}
