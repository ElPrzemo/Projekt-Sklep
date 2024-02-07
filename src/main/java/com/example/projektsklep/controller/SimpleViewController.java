package com.example.projektsklep.controller;

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
    public String getUserPanel(Model model, Principal principal) {
        // Pobierz nazwę użytkownika z obiektu Principal
        String email = principal.getName();
        // Pobierz UserDTO na podstawie e-maila (lub innego identyfikatora)
        Optional<UserDTO> userDTO = userService.findUserByEmail(email);

        // Upewnij się, że userDTO jest obecny przed dodaniem do modelu
        userDTO.ifPresent(dto -> model.addAttribute("userDTO", dto));

        // Jeśli userDTO nie jest obecne, możesz przekierować do strony błędu lub dodać obsługę błędów
        if (userDTO.isEmpty()) {
            // Przykład przekierowania do strony błędu
            return "redirect:/errorPage";
        }

        return "userPanel"; // Nazwa widoku Thymeleaf
    }
    @GetMapping("/security")
    public String security() {
        return "security"; // Zwraca userPanel.html
    }
}