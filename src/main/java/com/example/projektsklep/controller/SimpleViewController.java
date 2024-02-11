package com.example.projektsklep.controller;

import com.example.projektsklep.exception.UserNotFoundException;
import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.dto.UserDTO;

import com.example.projektsklep.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Collection;
import java.util.Optional;

@Controller
public class SimpleViewController {

    private final UserService userService;

    public SimpleViewController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String showHomePage(Model model, Principal principal) {
        boolean isAdmin = false;
        boolean isUser = false;
        if (principal != null) {
            Collection<? extends GrantedAuthority> authorities = ((Authentication) principal).getAuthorities();
            isAdmin = authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
            isUser = authorities.contains(new SimpleGrantedAuthority("ROLE_USER"));
        }

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isUser", isUser);

        return "home"; // Zwraca home.html
    }
    @GetMapping("/adminPanel")
    public String showAdminPanel() {
        return "adminPanel"; // Zwraca adminPanel.html
    }

    @GetMapping("/userPanel")
    public String showUserPanel(Model model, Authentication authentication) {
        String email = authentication.getName();
        UserDTO userDTO = userService.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika."));
        model.addAttribute("userDTO", userDTO);

        // Dodajemy logowanie danych użytkownika i adresu
        System.out.println("UserDTO: " + userDTO);
        if(userDTO.address() != null) {
            AddressDTO address = userDTO.address();
            System.out.println("Address: " + address.street() + ", " + address.city() + ", " + address.postalCode() + ", " + address.country());
        } else {
            System.out.println("Address is null");
        }

        return "userPanel";
    }
    @GetMapping("/security")
    public String security() {
        return "security"; // Zwraca userPanel.html
    }

}