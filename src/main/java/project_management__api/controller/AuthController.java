package project_management__api.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project_management__api.service.AuthService;
import project_management__api.service.TokenBlackListService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final TokenBlackListService tokenBlackListService;
    @Autowired
    public AuthController(AuthService authService,
                          TokenBlackListService tokenBlackListService){
        this.authService=authService;
        this.tokenBlackListService=tokenBlackListService;
    }

    @Operation(summary = "Login to Account")
    @PostMapping("/Login")
    public String loginUser(@RequestParam String username,@RequestParam String password){
         return authService.loginUser(username,password);
    }

    @Operation(summary = "Logout From Account")
    @PostMapping("/Logout")
    public String logoutUser(@RequestHeader ("Authorization") String authorization){
        String jwt=authorization.substring(7);
        tokenBlackListService.revokeToken(jwt);
        return "you have been logged out successffully";
    }
}
