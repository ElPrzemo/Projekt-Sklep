package com.example.projektsklep.repository;


import com.example.projektsklep.model.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;



import java.util.Optional;




public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsernameOrEmail(String username, String email);
}