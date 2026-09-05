package project_management__api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import project_management__api.entities.UserEntity;
import project_management__api.exceptions.UserNotFoundException;
import project_management__api.exceptions.UsernameNotFound;
import project_management__api.repositories.UserRepository;

import java.util.Collections;

@Service
public class CustomUserDetailService  implements UserDetailsService {

    private final UserRepository userRepository;
    @Autowired
    public CustomUserDetailService(UserRepository userRepository){
        this.userRepository=userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username){
        UserEntity userEntity=userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFound(username));
        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(userEntity.getSystemRole().name()).build();
    }

}
