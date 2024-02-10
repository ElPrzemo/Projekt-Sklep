package com.example.projektsklep.model.repository;

import com.example.projektsklep.model.dto.AuthorDTO;
import com.example.projektsklep.model.entities.product.Author;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String name);

    Page<Author> findAll(Pageable pageable);


}