package com.example.projektsklep.model.repository;

import com.example.projektsklep.model.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(String firstName, String lastName);

    User save(User user);

    void delete(User user);

    List<User> findAll();

    Optional<User> findById(Long id);

    void deleteById(Long id);
}

