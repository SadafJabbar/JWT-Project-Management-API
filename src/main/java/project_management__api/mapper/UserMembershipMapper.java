package project_management__api.mapper;

import org.springframework.stereotype.Component;
import project_management__api.dtos.UserMembershipResponse;
import project_management__api.entities.TaskEntity;
import project_management__api.entities.UserEntity;
import project_management__api.entities.UserMembershipEntity;

import java.util.ArrayList;
import java.util.List;


@Component
public class UserMembershipMapper {

    public UserMembershipEntity transformToEntity(UserEntity userEntity,List<TaskEntity> taskEntities){
        return UserMembershipEntity.builder()
                .userEntity(userEntity)
                .taskEntities(taskEntities)
                .build();
        }


    public UserMembershipResponse transformToResponse(UserMembershipEntity userMembershipEntity){

        List<Long> taskIds=new ArrayList<>();
        for (TaskEntity task: userMembershipEntity.getTaskEntities()){
            taskIds.add(task.getTaskId());
        }
        return UserMembershipResponse.builder()
                .userId(userMembershipEntity.getUserEntity().getUserId())
                .username(userMembershipEntity.getUserEntity().getUsername())
                .role(userMembershipEntity.getUserEntity().getSystemRole())
                .taskIds(taskIds)
                .build();
    }
}
