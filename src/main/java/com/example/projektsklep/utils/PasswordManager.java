package com.example.projektsklep.utils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


    @Component
    public class PasswordManager {

        private final PasswordEncoder passwordEncoder;

        public PasswordManager(PasswordEncoder passwordEncoder) {
            this.passwordEncoder = passwordEncoder;
        }

        public String encode(String password) {
            return passwordEncoder.encode(password);
        }

        public boolean verifyPassword(String rawPassword, String hashedPassword) {
            return passwordEncoder.matches(rawPassword, hashedPassword);
        }

}
