package com.example.projektsklep.model.dto;


import com.example.projektsklep.model.entities.product.AuthorEmbeddable;
import com.example.projektsklep.model.entities.product.CategoryEmbeddable;
import com.example.projektsklep.model.enums.ProductType;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Objects;

@Builder

public record ProductDTO(
        Long id,
        Long authorId,
        Long categoryId,
        String title,
        String description,
        String miniature,
        BigDecimal price,
        ProductType productType
) {

    public ProductDTO {
        Objects.requireNonNull(id);
        Objects.requireNonNull(title);
        Objects.requireNonNull(description);
        Objects.requireNonNull(miniature);
        Objects.requireNonNull(price);
        Objects.requireNonNull(productType);
    }
    public AuthorEmbeddable getAuthor() {
        return authorId != null ? new AuthorEmbeddable() : null;
    }

    public CategoryEmbeddable getCategory() {
        return categoryId != null ? new CategoryEmbeddable(categoryId) : null;
    }
}
