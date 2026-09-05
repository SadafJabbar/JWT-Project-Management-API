package project_management__api.mapper;

import org.springframework.stereotype.Component;
import project_management__api.dtos.UserRequest;
import project_management__api.dtos.UserResponse;
import project_management__api.entities.UserEntity;

@Component
public class UserMapper {

    public UserEntity transformToUserEntity(UserRequest userRequest,String password){
        return UserEntity.builder()
                .username(userRequest.username())
                .password(password)
                .email(userRequest.email())
                .systemRole(userRequest.systemRole())
                .build();
    }
    public UserResponse transformToUserResponse(UserEntity userEntity){
        return UserResponse.builder()
                .userId(userEntity.getUserId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .systemRole(userEntity.getSystemRole())
                .createdAt(userEntity.getCreatedAt())
                .build();
    }
    public UserEntity updateUserEntity(UserEntity userEntity,UserRequest userRequest,String password){
        userEntity.setUsername(userRequest.username());
        userEntity.setPassword(password);
        userEntity.setEmail(userRequest.email());
        userEntity.setSystemRole(userRequest.systemRole());
        return userEntity;
    }
}
