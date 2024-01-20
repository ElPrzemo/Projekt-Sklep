//package com.example.projektsklep.utils;
//
//import com.example.projektsklep.model.entities.user.Role;
//import com.example.projektsklep.model.entities.user.User;
//import com.example.projektsklep.repository.UserRepository;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DbInit {
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private UserRoleRepository userRoleRepository;
//    @PostConstruct
//    private void postConstruct() {
//        Role adminRole = new UserRole(“ROLE_ADMIN”);
//        userRoleRepository.save(adminRole);
//        User admin = new User(“admin”, “admin password”, adminRole);
//        userRepository.save(admin);
//    }
//}