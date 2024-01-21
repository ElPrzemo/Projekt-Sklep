package com.example.projektsklep.service;//package com.example.projectshopping.service;
//
//
//
//import com.example.projectshopping.model.entities.user.User;
//import com.example.projectshopping.model.repository.UserRepository;
//import com.example.projectshopping.model.form.RegistrationForm;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class RegistrationService {
//
//
//    private UserRepository userRepository;
//
//
//    private PasswordEncoder passwordEncoder;
//
//    public RegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    public void register(RegistrationForm form) {
//        User user = new User();
//        user.setEmail(form.getEmail());
//        user.setPasswordHash(passwordEncoder.encode(form.getPassword())); // Encrypt password before saving
//        user.setFirstName(form.getFirstName());
//        user.setLastName(form.getLastName());
//
//        // Save the user to the database
//        userRepository.save(user);
//    }
//}