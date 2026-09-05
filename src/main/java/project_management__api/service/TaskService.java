package project_management__api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project_management__api.dtos.ApiResponse;
import project_management__api.dtos.TaskRequest;
import project_management__api.dtos.TaskResponse;
import project_management__api.entities.MembershipEntity;
import project_management__api.entities.ProjectEntity;
import project_management__api.entities.TaskEntity;
import project_management__api.entities.UserEntity;
import project_management__api.exceptions.ProjectNotFoundException;
import project_management__api.exceptions.TaskNotFoundException;
import project_management__api.exceptions.UsernameNotFound;
import project_management__api.mapper.TaskMapper;
import project_management__api.repositories.MembershipRepository;
import project_management__api.repositories.ProjectRepository;
import project_management__api.repositories.TaskRepository;
import project_management__api.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;
    private final  MembershipRepository membershipRepository;
    @Autowired
    public TaskService (TaskRepository taskRepository,
                        TaskMapper taskMapper,
                        ProjectRepository projectRepository,
                        UserRepository userRepository,
                        MembershipRepository membershipRepository){
        this.taskRepository=taskRepository;
        this.taskMapper=taskMapper;
        this.projectRepository=projectRepository;
        this.userRepository=userRepository;
        this.membershipRepository=membershipRepository;
    }


    public ApiResponse<TaskResponse> getById(Long id){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user=userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFound(username));
        MembershipEntity membershipEntity=membershipRepository.findByUserMembershipEntities_UserEntity_UserId(user.getUserId())
                .orElseThrow(()-> new RuntimeException("You do not have any membership or any project assigned yet"));
        Long projectId=membershipEntity.getProjectEntity().getProjectId();
        TaskEntity taskEntity=taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException(id));
        if(!taskEntity.getProjectId().equals(projectId)){
            throw new RuntimeException("you cant access this project task");
        }
        TaskResponse taskResponse=taskMapper.transfromToTaskResponse(taskEntity);
        return ApiResponse.<TaskResponse>builder()
                .message("Record fetched successfully by Id")
                .data(taskResponse)
                .build();
    }


    public ApiResponse<List<TaskResponse>> getAllTasks(){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user=userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFound(username));
        MembershipEntity membershipEntity=membershipRepository.findByUserMembershipEntities_UserEntity_UserId(user.getUserId())
                .orElseThrow(()-> new RuntimeException("You do not have any membership or any project assigned yet"));
        Long projectId=membershipEntity.getProjectEntity().getProjectId();
        List<TaskResponse> managerResponse=new ArrayList<>();
        for (TaskEntity taskEntity:taskRepository.findAll()){
            if(taskEntity.getProjectId().equals(projectId)){
            TaskResponse taskResponse=taskMapper.transfromToTaskResponse(taskEntity);
            managerResponse.add(taskResponse);
        }

        }
        return ApiResponse.<List<TaskResponse>>builder()
                .message("All Tasks Records Fetched successfully")
                .data(managerResponse)
                .build();
    }


    public ApiResponse<TaskResponse> createTask(TaskRequest taskRequest){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user=userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFound(username));

        MembershipEntity membershipEntity=membershipRepository.findByUserMembershipEntities_UserEntity_UserId(user.getUserId())
                .orElseThrow(()-> new RuntimeException("You do not have any membership or any project assigned yet"));
        Long projectId=taskRequest.projectId();

        ProjectEntity projectEntity=projectRepository.findById(projectId)
                .orElseThrow(()-> new ProjectNotFoundException(projectId));

        Long managerProjectId = membershipEntity.getProjectEntity().getProjectId();

        TaskEntity taskEntity=taskMapper.transformToTaskEntity(taskRequest,projectId);

        if(!managerProjectId.equals(projectId)){
            throw new RuntimeException("you cant create task for a project you are not assigned to");
        }
        taskRepository.save(taskEntity);
        TaskResponse taskResponse=taskMapper.transfromToTaskResponse(taskEntity);
                    return ApiResponse.<TaskResponse>builder()
                            .message("Task Record Created Successfully")
                            .data(taskResponse)
                            .build();
    }

    public ApiResponse<TaskResponse> updateTask(Long id,TaskRequest taskRequest){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity user=userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFound(username));

        MembershipEntity membershipEntity=membershipRepository.findByUserMembershipEntities_UserEntity_UserId(user.getUserId())
                .orElseThrow(()-> new RuntimeException("You do not have any membership or any project assigned yet"));
        Long projectId=taskRequest.projectId();
        TaskEntity taskEntity=taskRepository.findById(id)
                .orElseThrow(()-> new TaskNotFoundException(id));

        ProjectEntity projectEntity=projectRepository.findById(projectId)
                .orElseThrow(()-> new ProjectNotFoundException(projectId));

        Long managerProjectId = membershipEntity.getProjectEntity().getProjectId();

        TaskEntity updatedEntity=taskMapper.updateTaskEntity(taskEntity,taskRequest,projectId);
        if(!managerProjectId.equals(projectId)){
            throw new RuntimeException("you cant update task for a project you are not assigned to");
        }
        taskRepository.save(updatedEntity);

        TaskResponse taskResponse=taskMapper.transfromToTaskResponse(updatedEntity);
                return ApiResponse.<TaskResponse>builder()
                        .message("Record updated successfully")
                        .data(taskResponse)
                        .build();
            }


    public ApiResponse<TaskResponse> deleteTask(Long id){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user=userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFound(username));
        MembershipEntity membershipEntity=membershipRepository.findByUserMembershipEntities_UserEntity_UserId(user.getUserId())
                .orElseThrow(()-> new RuntimeException("You do not have any membership or any project assigned yet"));
        Long projectId=membershipEntity.getProjectEntity().getProjectId();
        TaskEntity taskEntity=taskRepository.findById(id)
                .orElseThrow(()-> new TaskNotFoundException(id));
        if(!taskEntity.getProjectId().equals(projectId)){
            throw new RuntimeException("you cant delete this project task");
        }
        TaskResponse taskResponse=taskMapper.transfromToTaskResponse(taskEntity);
        taskRepository.deleteById(id);
        return ApiResponse.<TaskResponse>builder()
                .message("Record with id: " +id+ "deleted successfully")
                .data(taskResponse)
                .build();
    }
}
