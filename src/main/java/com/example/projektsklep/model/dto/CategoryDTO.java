package com.example.projektsklep.model.dto;

import lombok.Builder;

import java.util.Objects;


@Builder
public record CategoryDTO(
        Long id,
        String name,
        Long parentCategoryId
) {
    public CategoryDTO {
        Objects.requireNonNull(name, "Name cannot be null"); // Tylko jeśli name musi być zawsze niepuste
    }
}