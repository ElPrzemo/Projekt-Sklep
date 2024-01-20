package com.example.config.security;

import com.example.utils.PasswordManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final PasswordManager passwordManager;
    private final CustomLoginSuccessHandler customLoginSuccessHandler;

    public SecurityConfig(PasswordManager passwordManager, CustomLoginSuccessHandler customLoginSuccessHandler) {
        this.passwordManager = passwordManager;
        this.customLoginSuccessHandler = customLoginSuccessHandler;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/", "/home").permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(customLoginSuccessHandler)
                .permitAll()
                .and()
                .logout()
                .permitAll();
        return http.build();
    }
}