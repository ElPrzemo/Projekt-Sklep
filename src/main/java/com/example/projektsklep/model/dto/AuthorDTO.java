package com.example.projektsklep.model.dto;

import lombok.Builder;

import java.util.Objects;

@Builder
public record AuthorDTO(
        Long id,
        String name
) {
    public AuthorDTO() {
        this(null, "");
    }

    public AuthorDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}