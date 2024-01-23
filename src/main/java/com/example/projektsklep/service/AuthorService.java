package com.example.projektsklep.service;

import com.example.projektsklep.model.dto.AuthorDTO;
import com.example.projektsklep.model.entities.product.Author;
import com.example.projektsklep.model.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public AuthorDTO saveAuthor(AuthorDTO authorDTO) {
        Optional<Author> existingAuthor = authorRepository.findByName(authorDTO.name());
        if (existingAuthor.isPresent()) {
            return new AuthorDTO(existingAuthor.get().getId(), existingAuthor.get().getName());
        }

        Author author = new Author();
        author.setName(authorDTO.name());
        author = authorRepository.save(author);
        return new AuthorDTO(author.getId(), author.getName());
    }

    public List<AuthorDTO> findAllAuthors() {
        return authorRepository.findAll().stream()
                .map(author -> new AuthorDTO(author.getId(), author.getName()))
                .collect(Collectors.toList());
    }
}