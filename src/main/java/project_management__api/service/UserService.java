package project_management__api.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project_management__api.dtos.ApiResponse;
import project_management__api.dtos.UserRequest;
import project_management__api.dtos.UserResponse;
import project_management__api.entities.UserEntity;
import project_management__api.exceptions.UserNotFoundException;
import project_management__api.mapper.UserMapper;
import project_management__api.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Autowired
    public UserService(UserRepository userRepository,
                       UserMapper userMapper){
        this.userRepository=userRepository;
        this.userMapper=userMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    public ApiResponse<UserResponse> getById(Long id){
       UserEntity userEntity= userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
        UserResponse userResponse= userMapper.transformToUserResponse(userEntity);
        return ApiResponse.<UserResponse>builder()
                .message("Record of id:" +id+ " Fetched successfully")
                .data(userResponse)
                .build();
    }


    public ApiResponse<List<UserResponse>> getAllUsers() {
        List<UserResponse> userResponses = new ArrayList<>();
        for (UserEntity userEntity : userRepository.findAll()) {
            UserResponse userResponse = userMapper.transformToUserResponse(userEntity);
            userResponses.add(userResponse);
        }
        return ApiResponse.<List<UserResponse>>builder()
                .message("All User Records Fetched Successfully")
                .data(userResponses).build();
    }

    public ApiResponse<UserResponse> createUser(UserRequest userRequest){
        String encodedPassword=passwordEncoder().encode(userRequest.password());
        UserEntity userEntity=userMapper.transformToUserEntity(userRequest,encodedPassword);
        userRepository.save(userEntity);
        UserResponse userResponse= userMapper.transformToUserResponse(userEntity);
       return ApiResponse.<UserResponse>builder()
                .message("Record Created Successfully of id: "+userEntity.getUserId()).
                data(userResponse).build();
    }


    public ApiResponse<UserResponse> updateUser(Long id,UserRequest userRequest){
        UserEntity userEntity=userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
        String encodedPassword=passwordEncoder().encode(userRequest.password());
        UserEntity updatedEntity=userMapper.updateUserEntity(userEntity,userRequest,encodedPassword);
        userRepository.save(updatedEntity);
        UserResponse userResponse= userMapper.transformToUserResponse(userEntity);
        return ApiResponse.<UserResponse>builder()
                .message("Record Updated successfully of UserId: " +id)
                .data(userResponse).build();
    }


    public ApiResponse<UserResponse> deleteUser(Long id){
        UserEntity userEntity=userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
        userRepository.deleteById(id);
       UserResponse userResponse= userMapper.transformToUserResponse(userEntity);
        return ApiResponse.<UserResponse>builder()
                .message("Record Deleted successfully")
                .data(userResponse).build();
    }
}
