package com.example.projektsklep.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SimpleViewController {

    @GetMapping("/home")
    public String showHomePage() {
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
}