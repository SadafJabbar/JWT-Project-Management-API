package project_management__api.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project_management__api.service.RefreshTokenService;

@RestController
@RequestMapping("/api/v1/refresh")
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;
    @Autowired
    public RefreshTokenController(RefreshTokenService refreshTokenService){
        this.refreshTokenService=refreshTokenService;
    }


    @Operation(summary = "Refresh Access Token")
    @PostMapping
    public String refreshToken(@RequestParam String refreshToken){
        return refreshTokenService.generateAccess(refreshToken);
    }
}
