package project_management__api.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project_management__api.dtos.ApiResponse;
import project_management__api.dtos.UserRequest;
import project_management__api.dtos.UserResponse;
import project_management__api.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService){
        this.userService=userService;
    }

    @Operation(summary="Get User By Id")
    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getById(@PathVariable Long id){
        return userService.getById(id);
    }


    @Operation(summary = "Get All Users Records")
    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers(){
        return userService.getAllUsers();
    }

    @Operation(summary = "Create a Users")
    @PostMapping
    public ApiResponse<UserResponse> createUsers(@Valid @RequestBody UserRequest userRequest){
        return userService.createUser(userRequest);
    }

    @Operation(summary = "Update a Users")
    @PutMapping("/{id}")
    public  ApiResponse<UserResponse> updateUsers(@PathVariable Long id
            ,@Valid @RequestBody UserRequest userRequest){
        return userService.updateUser(id,userRequest);
    }

    @Operation(summary = "Delete a Users")
    @DeleteMapping("/{id}")
    public ApiResponse<UserResponse> deleteUsers(@PathVariable Long id){
        return userService.deleteUser(id);
    }
}
