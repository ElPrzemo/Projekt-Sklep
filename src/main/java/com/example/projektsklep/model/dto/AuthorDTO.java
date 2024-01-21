package com.example.projektsklep.model.dto;

import lombok.Builder;

import java.util.Objects;

@Builder
public record AuthorDTO(
        Long id,
        String name
) {

    public AuthorDTO {
        Objects.requireNonNull(id);
        Objects.requireNonNull(name);
    }
}