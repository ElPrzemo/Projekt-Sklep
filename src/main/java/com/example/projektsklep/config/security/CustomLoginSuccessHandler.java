//package com.example.projektsklep.config.security;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Component
//public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        Set<String> roles = authentication.getAuthorities().stream()
//                .map(r -> r.getAuthority())
//                .collect(Collectors.toSet());
//
//        if (roles.contains("ROLE_ADMIN")) {
//            setDefaultTargetUrl("/admin/dashboard");
//        } else if (roles.contains("ROLE_USER")) {
//            setDefaultTargetUrl("/user/profile");
//        } else {
//            setDefaultTargetUrl("/home");
//        }
//
//        super.onAuthenticationSuccess(request, response, authentication);
//    }
//}