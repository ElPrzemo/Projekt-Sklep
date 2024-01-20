package com.example.projektsklep.utils;

import com.example.projektsklep.model.entities.user.Role;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.repository.RoleRepository;
import com.example.projektsklep.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DbInit {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @PostConstruct
    private void postConstruct() {
        Role adminRole =  Role.builder().name("ROLE_ADMIN").build();
        Role savedAdminRole = userRoleRepository.save(adminRole);
        User admin = User.builder()
                .name("Henryk")
                .password(passwordEncoder.encode("haslo"))
                .email("aa@a.pl")
                .roles(Set.of(savedAdminRole))
                .username("Henryk")
                        .build();

        userRepository.save(admin);
    }






}