package com.example.projektsklep.model.dto;

import com.example.projektsklep.model.enums.ProductType;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
@Builder
public record ProductDTO(
        Long id,
        Long authorId,
        Long categoryId,
        @NotBlank(message = "Tytuł jest wymagany")
        String title,
        @NotBlank(message = "Opis jest wymagany")
        String description,
        String miniature,
        @NotNull(message = "Cena jest wymagana")
        @Min(value = 0, message = "Cena musi być większa lub równa 0")
        BigDecimal price,
        @NotNull(message = "Typ produktu jest wymagany")
        ProductType productType,
        Integer quantity
) {
    // Konstruktor, gettery, itd.
}