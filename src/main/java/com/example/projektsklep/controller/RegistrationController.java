package com.example.projektsklep.controller;//package com.example.projectshopping.controller;
////
////import com.example.projectshopping.model.form.RegistrationForm;
////import com.example.projectshopping.service.RegistrationService;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Controller;
////import org.springframework.web.bind.annotation.PostMapping;
////import org.springframework.web.bind.annotation.RequestMapping;
////
//@Controller
//@RequestMapping("/register")
//public class RegistrationController {
//    private RegistrationService registrationService;
//
//    public RegistrationController(RegistrationService registrationService) {
//        this.registrationService = registrationService;
//    }
//
//    @PostMapping
//    public String register(@ModelAttribute UserDTO userDTO) {
//        try {
//            User user = convertToEntity(userDTO);
//            registrationService.register(user);
//            return "redirect:/login";
//        } catch (RuntimeException e) {
//            return "user_register";
//        }
//    }
//
//    private User convertToEntity(UserDTO userDTO) {
//        // Implementacja konwersji UserDTO na User
//        return new User(userDTO.getId(), userDTO.getEmail(), userDTO.getFirstName(), userDTO.getLastName());
//    }
//}