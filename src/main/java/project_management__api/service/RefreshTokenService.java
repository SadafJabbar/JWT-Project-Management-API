package project_management__api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project_management__api.Security.JwtUtil;
import project_management__api.entities.RefreshTokenEntity;
import project_management__api.entities.UserEntity;
import project_management__api.exceptions.RefreshTokenNotFound;
import project_management__api.exceptions.UsernameNotFound;
import project_management__api.repositories.RefreshTokenRepository;
import project_management__api.repositories.UserRepository;

import javax.xml.crypto.Data;
import java.time.Instant;
import java.util.Date;

@Service
public class RefreshTokenService {

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final TokenBlackListService tokenBlackListService;
    private final UserRepository userRepository;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               JwtUtil jwtUtil,
                               TokenBlackListService tokenBlackListService,
                               UserRepository userRepository){
        this.refreshTokenRepository=refreshTokenRepository;
        this.jwtUtil=jwtUtil;
        this.tokenBlackListService=tokenBlackListService;
        this.userRepository=userRepository;
    }

    public RefreshTokenEntity createRefreshTokenEntity(String refreshtoken,String username,String accessToken){
        UserEntity user=userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFound(username));
        RefreshTokenEntity refreshTokenEntity= RefreshTokenEntity.builder().
                user(user)
                .token(refreshtoken)
                .validAccessToken(accessToken)
                .expiresAt(new Date(System.currentTimeMillis()+refreshExpiration).toInstant())
                .build();
        refreshTokenRepository.save(refreshTokenEntity);
        return refreshTokenEntity;
    }

    public String generateAccess(String token){
            RefreshTokenEntity refreshTokenEntity=refreshTokenRepository.findByToken(token)
                    .orElseThrow(()-> new RefreshTokenNotFound("this Refresh token was found in repo"));

            if (!jwtUtil.validateToken(token)) {
                throw new RuntimeException("Invalid or expired refresh token");
            }
            if(tokenBlackListService.isRefreshTokenRevoked(token)){
                throw  new RuntimeException("this Refresh Token has Revoked");
            }
            if(refreshTokenEntity.getExpiresAt().isBefore(Instant.now())){
                throw new RuntimeException("your Refresh Token has been expired");
            }

            String username =refreshTokenEntity.getUser().getUsername();
            String invalidAccessToken=refreshTokenEntity.getValidAccessToken();
            tokenBlackListService.revokeAccessToken(invalidAccessToken);
            String accessToken= jwtUtil.generateAccessToken(username);
            refreshTokenEntity.setValidAccessToken(accessToken);
            refreshTokenRepository.save(refreshTokenEntity);
            return accessToken;
        }

    }
