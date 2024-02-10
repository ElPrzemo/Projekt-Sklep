package com.example.projektsklep.model.entities.product;

import com.example.projektsklep.model.dto.CategoryTreeDTO;
import com.example.projektsklep.model.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CategoryIntegrationTest {
    @Mock
    private CategoryRepository categoryRepository; // Mock the repository


    @Test
    public void givenCategory_whenConvertToDTO_thenCorrectDTOIsReturned() {
        // Given
        Category category = new Category("Test Category");
        category.setId(1L); // Set a specific ID for testing

        // When
        CategoryTreeDTO categoryDTO = Category.toTreeDTO(category);

        // Then
        assertEquals(category.getId(), ((CategoryTreeDTO) categoryDTO).getId());
        assertEquals(category.getName(), categoryDTO.getName());
        // Additional assertions for other DTO properties if needed
    }

}
