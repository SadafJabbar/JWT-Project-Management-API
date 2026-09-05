package project_management__api.mapper;

import org.springframework.stereotype.Component;
import project_management__api.dtos.ProjectRequest;
import project_management__api.dtos.ProjectResponse;
import project_management__api.entities.ProjectEntity;

@Component
public class ProjectMapper {

    public ProjectEntity transformToProjectEntity(ProjectRequest projectRequest){
        return ProjectEntity.builder()
                .name(projectRequest.name())
                .description(projectRequest.description())
                .build();
    }
    public ProjectResponse transformToProjectResponse(ProjectEntity projectEntity){
        return ProjectResponse.builder()
                .projectId(projectEntity.getProjectId())
                .name(projectEntity.getName())
                .description(projectEntity.getDescription())
                .build();
    }
    public ProjectEntity updateProjectEntity(ProjectEntity projectEntity,ProjectRequest projectRequest){
        projectEntity.setName(projectRequest.name());
        projectEntity.setDescription(projectRequest.description());
        return projectEntity;
    }
}
