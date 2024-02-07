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
    public void givenNewCategory_whenSave_thenCategoryIsPersisted() {
        // Given
        String categoryName = "Test Category";
        Category category = new Category(categoryName);

        // When
        categoryRepository.save(category);

        // Then
        verify(categoryRepository).save(category);
        Optional<Category> foundCategory = categoryRepository.findById(category.getId());
        assertTrue(foundCategory.isPresent());
        assertEquals(categoryName, foundCategory.get().getName());
    }
    @Test
    public void givenParentAndChildCategories_whenRetrieveParent_thenChildrenAreIncluded() {
        // Given
        Category parentCategory = new Category("Parent Category");
        Category childCategory1 = new Category("Child Category 1");
        Category childCategory2 = new Category("Child Category 2");
        childCategory1.setParentCategory(parentCategory);
        childCategory2.setParentCategory(parentCategory);
        categoryRepository.saveAll(List.of(parentCategory, childCategory1, childCategory2));

        // When
        Category retrievedParent = categoryRepository.findById(parentCategory.getId()).get();

        // Then
        assertEquals(2, retrievedParent.getChildren().size());
        assertTrue(retrievedParent.getChildren().contains(childCategory1));
        assertTrue(retrievedParent.getChildren().contains(childCategory2));
    }
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
