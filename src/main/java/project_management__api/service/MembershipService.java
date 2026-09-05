package project_management__api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project_management__api.dtos.*;
import project_management__api.entities.*;
import project_management__api.exceptions.*;
import project_management__api.mapper.MembershipMapper;
import project_management__api.mapper.UserMembershipMapper;
import project_management__api.repositories.*;

import java.util.*;

@Service
public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final MembershipMapper membershipMapper;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final UserMembershipMapper userMembershipMapper;
    @Autowired
    public MembershipService(MembershipRepository membershipRepository,
                             MembershipMapper membershipMapper,
                             UserRepository userRepository,
                             ProjectRepository projectRepository,
                             TaskRepository taskRepository,
                             UserMembershipMapper userMembershipMapper){
        this.membershipRepository=membershipRepository;
        this.membershipMapper=membershipMapper;
        this.projectRepository=projectRepository;
        this.userRepository=userRepository;
        this.taskRepository=taskRepository;
        this.userMembershipMapper=userMembershipMapper;
    }


    public ApiResponse<MembershipResponse> getMembershipDataById(Long id){
        MembershipEntity membershipEntity=membershipRepository.findById(id).orElseThrow(()-> new MemberShipNotFoundException(id));
        Long projectId=membershipEntity.getProjectEntity().getProjectId();
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity=userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFound(username));
        if(userEntity.getSystemRole()==Role.MANAGER){
            boolean isManagerOfProject=membershipEntity.getUserMembershipEntities()
                    .stream().anyMatch(userMembershipEntity ->
                            userMembershipEntity.getUserEntity().getUserId().equals(userEntity.getUserId())
                                    && userMembershipEntity.getUserEntity().getSystemRole()==Role.MANAGER);

        if(!isManagerOfProject){
            throw new RuntimeException(" you are not allowed to access this membership record");
        }}
        int totalTasks = taskRepository.findAllByProjectId(projectId).size();
        int totalMembers=membershipEntity.getUserMembershipEntities().size();
        MembershipResponse membershipResponse=membershipMapper.transformToResponse(membershipEntity,totalTasks,totalMembers);
        return ApiResponse.<MembershipResponse>builder()
                .message("Membership Record fetched successfully")
                .data(membershipResponse)
                .build();
    }



    public ApiResponse<List<MembershipResponse>> getAllMembershipData(){
        List<MembershipResponse> membershipResponses=new ArrayList<>();
        for (MembershipEntity membershipEntity:membershipRepository.findAll()){
            int totalTasks = taskRepository.findAllByProjectId(membershipEntity.getProjectEntity().getProjectId()).size();
            int totalMembers=membershipEntity.getUserMembershipEntities().size();
            MembershipResponse membershipResponse=membershipMapper.transformToResponse(membershipEntity,totalTasks,totalMembers);
            membershipResponses.add(membershipResponse);
        }
        return ApiResponse.<List<MembershipResponse>>builder()
                .message("All Membership Records Fetched Successfully")
                .data(membershipResponses)
                .build();
    }





    public ApiResponse<MembershipResponse> createMembership(MembershipRequest membershipRequest){
        ProjectEntity projectEntity=projectRepository.findById(membershipRequest.projectId())
                .orElseThrow(()-> new ProjectNotFoundException(membershipRequest.projectId()));


        if(membershipRepository.findByProjectEntity_ProjectId(projectEntity.getProjectId()).isPresent()){
            throw new RuntimeException("the membership with the project id:" +
                    " " + projectEntity.getProjectId() +
                    " already exists. u need to update the membership if u want to add or change something.");
        }
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity loggedInUser=userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFound(username));
        if(loggedInUser.getSystemRole()==Role.MANAGER){
            boolean alreadyAssigned=membershipRepository
                    .findAll()
                    .stream()
                    .flatMap(membershipEntity ->
                            membershipEntity.getUserMembershipEntities().stream())
                    .anyMatch(userMembership ->
                            userMembership.getUserEntity().getUserId()
                                    .equals(loggedInUser.getUserId()));
            if(alreadyAssigned){
                throw new RuntimeException("you already have a project assigned. you can make another post request");
            }
        }

        if(membershipRequest.userMembershipRequestList().size()>10){
            throw new RuntimeException(" Members cant exceed 10");
        }
        Map<Long, Long> assignedTaskIds = new HashMap<>();
        List<UserMembershipEntity> userMembershipEntities=new ArrayList<>();

        int managerCount=0;
        for (UserMembershipRequest userMembershipRequest:membershipRequest.userMembershipRequestList()){
            UserEntity userEntity=userRepository.findById(userMembershipRequest.userId()).orElseThrow(()-> new UserNotFoundException(userMembershipRequest.userId()));
            if(userMembershipRequest.taskIds().size()>3){
                throw new RuntimeException("Members cant have more than 3 tasks");
            }
            List<TaskEntity> userTasks=new ArrayList<>();
            for (Long taskId :userMembershipRequest.taskIds()){
                    TaskEntity task=taskRepository.findById(taskId).orElseThrow(()-> new  TaskNotFoundException(taskId));
                    if(!task.getProjectId().equals(projectEntity.getProjectId())){
                        throw new RuntimeException("Task " +taskId+ " does not belong to project " +projectEntity.getProjectId());
                    }
                Long previousUserId = assignedTaskIds.putIfAbsent(
                        taskId,
                        userEntity.getUserId()
                );
                if (previousUserId != null) {
                    throw new RuntimeException(
                            "Task " + taskId +
                                    " has already been assigned to user " + previousUserId
                    );
                }
                    userTasks.add(task);
                }
            if (userEntity.getSystemRole()== Role.MANAGER){
                managerCount++;
            }
            if(managerCount>1){
                throw new RuntimeException("A project can have only one Manager");
            }
                UserMembershipEntity userMembership=userMembershipMapper.transformToEntity(userEntity,userTasks);
            userMembershipEntities.add(userMembership);

        }
        MembershipEntity membership=membershipMapper.transformToEntity(projectEntity,userMembershipEntities);

                    membershipRepository.save(membership);
        int totalTasks = taskRepository.findAllByProjectId(membership.getProjectEntity().getProjectId()).size();
        int totalMembers=membership.getUserMembershipEntities().size();
        MembershipResponse membershipResponse=membershipMapper.transformToResponse(membership,totalTasks,totalMembers);
        return ApiResponse.<MembershipResponse>builder()
                            .message("Membership Record Created Successfully")
                            .data(membershipResponse)
                            .build();
            }






    public ApiResponse<MembershipResponse> updateMembership(Long id,MembershipRequest membershipRequest){
        MembershipEntity membershipEntity=membershipRepository.findById(id)
                .orElseThrow(()-> new MemberShipNotFoundException(id));

        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user=userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFound(username));
        if(user.getSystemRole()==Role.MANAGER){
            boolean isManagerOfProject=membershipEntity.getUserMembershipEntities()
                    .stream().anyMatch(userMembershipEntity ->
                            userMembershipEntity.getUserEntity().getUserId().equals(user.getUserId())
                                    && userMembershipEntity.getUserEntity().getSystemRole()==Role.MANAGER);

            if(!isManagerOfProject){
                throw new RuntimeException(" you are not allowed to access this membership record");
            }}

        ProjectEntity projectEntity=projectRepository.findById(membershipRequest.projectId())
                .orElseThrow(()-> new ProjectNotFoundException(membershipRequest.projectId()));

        if(membershipRequest.userMembershipRequestList().size()>10){
            throw new RuntimeException(" Members cant exceed 10");
        }

        int managerCount=0;
        Map<Long, Long> assignedTaskIds = new HashMap<>();
        List<UserMembershipEntity> userMembershipEntitiesList=new ArrayList<>();
        for (UserMembershipRequest userMembershipRequest:membershipRequest.userMembershipRequestList()){
            UserEntity userEntity=userRepository.findById(userMembershipRequest.userId())
                    .orElseThrow(()-> new UserNotFoundException(userMembershipRequest.userId()));

            if(userMembershipRequest.taskIds().size()>3){
                throw new RuntimeException("Members cant have more than 3 tasks");
            }
            List<TaskEntity> usertaskEntityList=new ArrayList<>();
            for (Long taskId :userMembershipRequest.taskIds()){
                TaskEntity task=taskRepository.findById(taskId)
                        .orElseThrow(()-> new  TaskNotFoundException(taskId));

                if(!task.getProjectId().equals(projectEntity.getProjectId())){
                    throw new RuntimeException("Task " +taskId+ " does not belong to project " +projectEntity.getProjectId());
                }
                Long previousUserId = assignedTaskIds.putIfAbsent(
                        taskId,
                        userEntity.getUserId()
                );
                if (previousUserId != null) {
                    throw new RuntimeException(
                            "Task " + taskId +
                                    " has already been assigned to user " + previousUserId
                    );
                }
                usertaskEntityList.add(task);
            }
            if (userEntity.getSystemRole()== Role.MANAGER){
                managerCount++;
            }
            if(managerCount>1){
                throw new RuntimeException("A project can have only one Manager");
            }
            UserMembershipEntity userMembership=userMembershipMapper.transformToEntity(userEntity,usertaskEntityList);
            userMembershipEntitiesList.add(userMembership);
        }
        MembershipEntity updatedmembership=membershipMapper.updateMembership(membershipEntity,
                projectEntity,
                userMembershipEntitiesList);
        membershipRepository.save(updatedmembership);
        int totalTasks = taskRepository.findAllByProjectId(membershipEntity.getProjectEntity().getProjectId()).size();
        int totalMembers=membershipEntity.getUserMembershipEntities().size();
        MembershipResponse membershipResponse=membershipMapper.transformToResponse(updatedmembership,totalTasks,totalMembers);
        return ApiResponse.<MembershipResponse>builder()
                .message("Membership Record Updated Successfully")
                .data(membershipResponse)
                .build();

    }



    public ApiResponse<MembershipResponse> deleteMembership( Long id){
        MembershipEntity membershipEntity=membershipRepository.findById(id)
                .orElseThrow(()-> new MemberShipNotFoundException(id));
        int totalTasks = taskRepository.findAllByProjectId(membershipEntity.getProjectEntity().getProjectId()).size();
        int totalMembers=membershipEntity.getUserMembershipEntities().size();

        MembershipResponse membershipResponse=membershipMapper.transformToResponse(membershipEntity,totalTasks,totalMembers);
        membershipRepository.delete(membershipEntity);
        return ApiResponse.<MembershipResponse>builder()
                .message("Membership Record Deleted Successfully")
                .data(membershipResponse)
                .build();
    }}
