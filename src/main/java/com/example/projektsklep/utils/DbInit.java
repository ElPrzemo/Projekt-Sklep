package com.example.projektsklep.utils;


import com.example.projektsklep.model.entities.role.Role;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.enums.AdminOrUser;
import com.example.projektsklep.model.repository.RoleRepository;
import com.example.projektsklep.model.repository.UserRepository;
import jakarta.annotation.PostConstruct;
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
        Role adminRole = Role.fromAdminOrUser(AdminOrUser.ADMIN);
        Role userRole = Role.fromAdminOrUser(AdminOrUser.USER);

        // Save roles
        Role savedAdminRole = roleRepository.save(adminRole);
        Role savedUserRole = roleRepository.save(userRole);

        // Create user Henryk with ROLE_USER
        User henryk = User.builder()
                .firstName("Henryk")
                .lastName("Wąsik")
                .email("henryk@example.com")
                .passwordHash(passwordEncoder.encode("henryk"))
                .roles(Set.of(savedUserRole))
                .build();

        // Create user Karol with ROLE_ADMIN
        User karol = User.builder()
                .firstName("Mariusz")
                .lastName("Kamiński")
                .email("karol@example.com")
                .passwordHash(passwordEncoder.encode("maciej"))
                .roles(Set.of(savedAdminRole))
                .build();

        // Save users
        userRepository.save(henryk);
        userRepository.save(karol);
    }
}