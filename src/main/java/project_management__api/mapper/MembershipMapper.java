package project_management__api.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project_management__api.dtos.*;
import project_management__api.entities.*;

import java.util.ArrayList;
import java.util.List;


@Component
public class MembershipMapper {

    private final UserMembershipMapper userMembershipMapper;
    public final ProjectMapper projectMapper;
    @Autowired
    public MembershipMapper(UserMembershipMapper userMembershipMapper,
                            ProjectMapper projectMapper){
        this.userMembershipMapper=userMembershipMapper;
        this.projectMapper=projectMapper;
    }



    public MembershipResponse transformToResponse(MembershipEntity membershipEntity,int totalTasks,int totalMembers){

        ProjectResponse projectResponse=projectMapper.transformToProjectResponse(membershipEntity.getProjectEntity());

        List<UserMembershipResponse> userMembershipResponseList=new ArrayList<>();
        for (UserMembershipEntity userMembershipEntity:membershipEntity.getUserMembershipEntities()){
           UserMembershipResponse userMembershipResponse= userMembershipMapper.transformToResponse(userMembershipEntity);
            userMembershipResponseList.add(userMembershipResponse);
        }
       return MembershipResponse.builder()
               .membershipId(membershipEntity.getMembershipId())
                .projectResponse(projectResponse)
               .userMembershipResponseList(userMembershipResponseList)
               .totalMembers(totalMembers)
               .totalTasks(totalTasks)
               .build();
    }





        public MembershipEntity transformToEntity(ProjectEntity projectEntity,
                                                  List<UserMembershipEntity> userMembershipEntities){

            return MembershipEntity.builder()
                .projectEntity(projectEntity)
                .userMembershipEntities(userMembershipEntities)
                .build();
        }





        public MembershipEntity updateMembership(MembershipEntity membershipEntity,
                                                   ProjectEntity projectEntity,
                                                List<UserMembershipEntity> userMembershipEntities){
            membershipEntity.getUserMembershipEntities().clear();
            membershipEntity.getUserMembershipEntities().addAll(userMembershipEntities);
        membershipEntity.setProjectEntity(projectEntity);
        return membershipEntity;
        }
       }

