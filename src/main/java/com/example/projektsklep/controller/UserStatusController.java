package com.example.projektsklep.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class UserStatusController {

    @GetMapping("/api/user/status")
    public Map<String, Object> getUserStatus() {
        Map<String, Object> status = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            status.put("loggedIn", true);
            status.put("roles", authentication.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .collect(Collectors.toSet())); // Zwraca zbiór ról
        } else {
            status.put("loggedIn", false);
            status.put("roles", Collections.emptySet());
        }

        return status;
    }
}
