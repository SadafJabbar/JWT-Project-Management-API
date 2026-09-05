package project_management__api.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import project_management__api.service.CustomUserDetailService;
import project_management__api.service.TokenBlackListService;

import java.io.IOException;

@Component
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailService customUserDetailService;
    private final TokenBlackListService tokenBlackListService;
    @Autowired
    public AuthTokenFilter(JwtUtil jwtUtil,
                           CustomUserDetailService customUserDetailService,
                           TokenBlackListService tokenBlackListService){
        this.customUserDetailService=customUserDetailService;
        this.jwtUtil=jwtUtil;
        this.tokenBlackListService=tokenBlackListService;
    }

    private String parseJwt(HttpServletRequest request){
        String header=request.getHeader("Authorization");
        if (header!=null && header.startsWith("Bearer ")){
            return  header.substring(7);
        }
        return null;
    }

    @Override
    protected  void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
        throws IOException, ServletException{
        try {
            String jwt=parseJwt(request);
            if (jwt!=null && jwtUtil.validateToken(jwt) && !tokenBlackListService.isRevoked(jwt)){
                String username=jwtUtil.getUsernameFromToken(jwt);
                UserDetails userDetails= customUserDetailService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);

            }
        }catch (Exception e){
            log.error("Cannot set user Authentication");
        }
        filterChain.doFilter(request,response);
    }


}
