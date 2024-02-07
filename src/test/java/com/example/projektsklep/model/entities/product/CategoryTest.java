package com.example.projektsklep.model.entities.product;

import com.example.projektsklep.model.dto.CategoryTreeDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DataJpaTest
public class CategoryTest {

    @Mock
    private Category parentCategoryMock;

    @Test
    public void testCategoryToTreeDTO() {
        // Given
        Category category = new Category("Test Category");
        category.setId(1L);

        // When
        CategoryTreeDTO result = Category.toTreeDTO(category);

        // Then
        assertEquals(category.getId(), result.getId());
        assertEquals(category.getName(), result.getName());
    }

    @Test
    public void testGetChildren() {
        // Given
        Category category = new Category("Parent Category");
        Category child1 = new Category("Child 1");
        Category child2 = new Category("Child 2");
        List<Category> children = new ArrayList<>();
        children.add(child1);
        children.add(child2);
        category.setChildren(children);

        // When
        List<Category> result = category.getChildren();

        // Then
        assertEquals(2, result.size());
        assertEquals(child1, result.get(0));
        assertEquals(child2, result.get(1));
    }

    @Test
    public void testGetParent() {
        // Given
        Category category = new Category("Child Category");
        when(parentCategoryMock.getName()).thenReturn("Parent Category");
        category.setParentCategory(parentCategoryMock);

        // When
        Category result = category.getParent();

        // Then
        assertEquals("Parent Category", result.getName());
    }
}