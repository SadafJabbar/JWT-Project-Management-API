package project_management__api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import project_management__api.Security.JwtUtil;
import project_management__api.dtos.AuthResponse;
import project_management__api.entities.RefreshTokenEntity;
import project_management__api.entities.UserEntity;
import project_management__api.exceptions.UsernameNotFound;
import project_management__api.repositories.RefreshTokenRepository;
import project_management__api.repositories.UserRepository;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final TokenBlackListService tokenBlackListService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil,
                       RefreshTokenService refreshTokenService,
                       TokenBlackListService tokenBlackListService,
                       RefreshTokenRepository refreshTokenRepository){
        this.authenticationManager=authenticationManager;
        this.jwtUtil=jwtUtil;
        this.refreshTokenService=refreshTokenService;
        this.tokenBlackListService=tokenBlackListService;
        this.refreshTokenRepository=refreshTokenRepository;

    }


    public AuthResponse loginUser(String username,String password){
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                ));
        UserDetails userDetails=(UserDetails) authentication.getPrincipal();
        String accessToken= jwtUtil.generateAccessToken(userDetails.getUsername());
        String refreshToken=jwtUtil.generateRefreshToken(userDetails.getUsername());
        refreshTokenService.createRefreshTokenEntity(refreshToken,userDetails.getUsername(),accessToken);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    public String revokeToken(String accessToken){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        RefreshTokenEntity refreshToken=refreshTokenRepository.findByUser_Username(username)
                .orElseThrow(()-> new RuntimeException("cant find your Refresh token in repo"));
        String refresh=refreshToken.getToken();
        String access=accessToken.substring(7);
        tokenBlackListService.revokeRefreshToken(refresh);
        tokenBlackListService.revokeAccessToken(access);
        return "You have been Logged out";


    }
}
