package com.example.projektsklep.service;

import com.example.projektsklep.model.dto.AuthorDTO;
import com.example.projektsklep.model.entities.product.Author;
import com.example.projektsklep.model.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<AuthorDTO> findAllAuthors(Pageable pageable) {
        return authorRepository.findAll(pageable)
                .map(this::convertToAuthorDTO);
    }

    private AuthorDTO convertToAuthorDTO(Author author) {
        // Przykładowa implementacja, dostosuj do swoich potrzeb
        return new AuthorDTO(author.getId(), author.getName());
    }
}