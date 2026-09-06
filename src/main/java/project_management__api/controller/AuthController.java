package project_management__api.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project_management__api.dtos.AuthResponse;
import project_management__api.service.AuthService;
import project_management__api.service.TokenBlackListService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    @Autowired
    public AuthController(AuthService authService){
        this.authService=authService;
    }

    @Operation(summary = "Login to Account")
    @PostMapping("/Login")
    public AuthResponse loginUser(@RequestParam String username, @RequestParam String password){
         return authService.loginUser(username,password);
    }

    @Operation(summary = "Logout From Account")
    @PostMapping("/Logout")
    public String logoutUser(@RequestHeader ("Authorization") String accessToken){
       return authService.revokeToken(accessToken);
    }
}
