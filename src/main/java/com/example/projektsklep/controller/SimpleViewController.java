package com.example.projektsklep.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Collection;

@Controller
public class SimpleViewController {

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
    public String showUserPanel() {
        return "userPanel"; // Zwraca userPanel.html
    }
    @GetMapping("/security")
    public String securityCheck() {
        return "security_check"; // Zwraca userPanel.html
    }
}