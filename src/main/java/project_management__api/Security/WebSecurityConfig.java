package project_management__api.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import project_management__api.dtos.Role;

@Configuration
public class WebSecurityConfig {

    @Autowired
    private AuthEntryPoint authEntryPoint;

    @Autowired
    private AuthTokenFilter authTokenFilter;

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
    http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .exceptionHandling(e-> e.authenticationEntryPoint(authEntryPoint))
            .sessionManagement(s-> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(a-> a
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                    .requestMatchers("/api/v1/auth","/api/v1/auth/**").permitAll()
                    .requestMatchers("/api/v1/refresh","/api/v1/refresh/**").permitAll()


                    .requestMatchers("/api/v1/users","/api/v1/users/**").hasRole(Role.ADMIN.name())

                    .requestMatchers("/api/v1/projects","/api/v1/projects/**").hasRole(Role.ADMIN.name())

                    .requestMatchers(HttpMethod.GET,"/api/v1/membership").hasRole(Role.ADMIN.name())
                    .requestMatchers(HttpMethod.DELETE,"/api/v1/membership/**").hasRole(Role.ADMIN.name())
                    .requestMatchers(HttpMethod.GET,"/api/v1/membership/**").hasAnyRole(Role.MANAGER.name(),Role.ADMIN.name())
                    .requestMatchers(HttpMethod.PUT,"/api/v1/membership/**").hasAnyRole(Role.MANAGER.name(),Role.ADMIN.name())
                    .requestMatchers(HttpMethod.POST,"/api/v1/membership").hasAnyRole(Role.MANAGER.name(),Role.ADMIN.name())

                    .requestMatchers("/api/v1/tasks","/api/v1/tasks/**").hasRole(Role.MANAGER.name())
                    .anyRequest().authenticated());


    http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
    }}
