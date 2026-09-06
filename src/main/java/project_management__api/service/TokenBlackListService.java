package project_management__api.service;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlackListService {
    private final Set<String> revokedAcessTokens= ConcurrentHashMap.newKeySet();
    private final Set<String> revokedRefreshTokens=ConcurrentHashMap.newKeySet();

    public void revokeAccessToken(String token){
        revokedAcessTokens.add(token);
    }
    public void revokeRefreshToken(String token){
        revokedRefreshTokens.add(token);
    }
    public boolean isRevoked(String token){
        return revokedAcessTokens.contains(token);
    }
    public boolean isRefreshTokenRevoked(String token)
    {return revokedRefreshTokens.contains(token);}


}
