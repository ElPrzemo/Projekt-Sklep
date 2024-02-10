package com.example.projektsklep.model.dto;

import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Builder
public record AuthorDTO(
        Long id,

        @NotBlank(message = "Nazwa nie może być pusta")
        @Size(min = 2, max = 50, message = "Nazwa musi zawierać od 2 do 50 znaków")
        String name
) {}