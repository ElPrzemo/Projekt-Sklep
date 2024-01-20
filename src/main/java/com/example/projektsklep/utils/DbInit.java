package com.example.projektsklep.utils;

import com.example.projektsklep.model.entity.Role;
import com.example.projektsklep.model.entity.User;
import com.example.projektsklep.model.repository.RoleRepository;
import com.example.projektsklep.model.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DbInit {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    public DbInit(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void postConstruct() {
        // Create roles
        Role adminRole = Role.builder().name("ROLE_ADMIN").build();
        Role userRole = Role.builder().name("ROLE_USER").build();

        // Save roles
        Role savedAdminRole = roleRepository.save(adminRole);
        Role savedUserRole = roleRepository.save(userRole);

        // Create user Henryk with ROLE_USER
        User henryk = User.builder()
                .username("Henryk")
                .email("henryk@example.com")
                .password(passwordEncoder.encode("henrykspassword"))
                .roles(Set.of(savedUserRole))
                .build();

        // Create user Karol with ROLE_ADMIN
        User karol = User.builder()
                .username("Karol")
                .email("karol@example.com")
                .password(passwordEncoder.encode("karolspassword"))
                .roles(Set.of(savedAdminRole))
                .build();

        // Save users
        userRepository.save(henryk);
        userRepository.save(karol);
    }
}