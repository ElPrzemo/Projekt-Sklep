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
        Objects.requireNonNull(id);
        Objects.requireNonNull(name);
    }

}