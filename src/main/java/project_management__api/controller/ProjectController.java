package project_management__api.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project_management__api.dtos.ApiResponse;
import project_management__api.dtos.ProjectRequest;
import project_management__api.dtos.ProjectResponse;
import project_management__api.service.ProjectService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;
    @Autowired
    public ProjectController(ProjectService projectService){
        this.projectService=projectService;
    }


    @Operation(summary="Get  Project By Id")
    @GetMapping("/manager/{id}")
    public ApiResponse<ProjectResponse> getById( @PathVariable Long id){
        return projectService.getById(id);
    }


    @Operation(summary = "Get All Project Records")
    @GetMapping
    public ApiResponse<List<ProjectResponse>> getAllProjects(){
        return projectService.getAllProjects();
    }

    @Operation(summary = "Create a Project")
    @PostMapping
    public ApiResponse<ProjectResponse> createProject(@Valid @RequestBody ProjectRequest projectRequest){
        return projectService.createProject(projectRequest);
    }

    @Operation(summary = "Update a project")
    @PutMapping("/{id}")
    public ApiResponse<ProjectResponse> updateProject(@PathVariable Long id
            ,@Valid @RequestBody ProjectRequest projectRequest){
        return projectService.updateProject(id,projectRequest);
    }

    @Operation(summary = "Delete a Project")
    @DeleteMapping("/{id}")
    public ApiResponse<ProjectResponse> deleteProject(@PathVariable Long id){
        return projectService.deleteProject(id);
    }

}
