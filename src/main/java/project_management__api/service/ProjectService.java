package project_management__api.service;

import project_management__api.dtos.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project_management__api.dtos.ProjectRequest;
import project_management__api.dtos.ProjectResponse;
import project_management__api.entities.ProjectEntity;
import project_management__api.exceptions.ProjectNotFoundException;
import project_management__api.mapper.ProjectMapper;
import project_management__api.repositories.ProjectRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Autowired
    public ProjectService(ProjectRepository projectRepository,
                          ProjectMapper projectMapper){
        this.projectRepository=projectRepository;
        this.projectMapper=projectMapper;
    }

    public ApiResponse<ProjectResponse> getById(Long id){
        ProjectEntity projectEntity=projectRepository.findById(id).orElseThrow(()-> new ProjectNotFoundException(id));
        ProjectResponse projectResponse=projectMapper.transformToProjectResponse(projectEntity);
       return ApiResponse.<ProjectResponse>builder()
                .message("Record fetched successfully by id")
                .data(projectResponse)
                .build();
    }


    public ApiResponse<List<ProjectResponse>> getAllProjects(){
        List<ProjectResponse> responses=new ArrayList<>();
        for (ProjectEntity projectEntity:projectRepository.findAll()){
            ProjectResponse projectResponse=projectMapper.transformToProjectResponse(projectEntity);
            responses.add(projectResponse);
        }
        return ApiResponse.<List<ProjectResponse>>builder()
                .message("All record of Users have been fetched successfully")
                .data(responses)
                .build();
    }

    public ApiResponse<ProjectResponse> createProject(ProjectRequest projectRequest){
        ProjectEntity projectEntity=projectMapper.transformToProjectEntity(projectRequest);
        projectRepository.save(projectEntity);
        ProjectResponse projectResponse=projectMapper.transformToProjectResponse(projectEntity);
        return ApiResponse.<ProjectResponse>builder()
                .message("Record created successfully")
                .data(projectResponse)
                .build();
    }


    public ApiResponse<ProjectResponse> updateProject(Long id,ProjectRequest projectRequest){
        ProjectEntity projectEntity=projectRepository.findById(id).orElseThrow(()-> new ProjectNotFoundException(id));
        ProjectEntity updatedEntity=projectMapper.updateProjectEntity(projectEntity,projectRequest);
        ProjectResponse projectResponse=projectMapper.transformToProjectResponse(updatedEntity);
        projectRepository.save(updatedEntity);
       return ApiResponse.<ProjectResponse>builder()
                .message("Record updated successfully")
                .data(projectResponse)
                .build();
    }


    public ApiResponse<ProjectResponse> deleteProject(Long id){
        ProjectEntity projectEntity=projectRepository.findById(id).orElseThrow(()-> new ProjectNotFoundException(id));
        projectRepository.deleteById(id);
        ProjectResponse projectResponse=projectMapper.transformToProjectResponse(projectEntity);
        return ApiResponse.<ProjectResponse>builder()
                .message("Record deleted successfully")
                .data(projectResponse)
                .build();
    }


}
