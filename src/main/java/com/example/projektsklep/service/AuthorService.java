package com.example.projektsklep.service;

import com.example.projektsklep.model.dto.AuthorDTO;
import com.example.projektsklep.model.entities.product.Author;
import com.example.projektsklep.model.repository.AuthorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class AuthorService {
    private static final Logger log = LoggerFactory.getLogger(AuthorService.class);
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Transactional
    public AuthorDTO saveAuthor(AuthorDTO authorDTO) {
        log.info("Attempting to save author: {}", authorDTO);
        Optional<Author> existingAuthor = authorRepository.findByName(authorDTO.name());
        if (existingAuthor.isPresent()) {
            log.info("Author already exists: {}", authorDTO.name());
            return convertToAuthorDTO(existingAuthor.get());
        }

        Author author = new Author();
        author.setName(authorDTO.name());
        author = authorRepository.save(author);
        log.info("Author saved successfully: {}", author.getName());
        return convertToAuthorDTO(author);
    }

    public Page<AuthorDTO> findAllAuthorsPageable(Pageable pageable) {
        log.info("Fetching authors with pageable: {}", pageable);
        return authorRepository.findAll(pageable)
                .map(this::convertToAuthorDTO);
    }

    private AuthorDTO convertToAuthorDTO(Author author) {
        log.info("Converting author entity to DTO: {}", author.getName());
        return new AuthorDTO(author.getId(), author.getName());
    }

    public List<AuthorDTO> findAll() {
        log.info("Fetching all authors");
        return authorRepository.findAll().stream().map(this::convertToAuthorDTO).collect(Collectors.toList());
    }
}