package com.example.projektsklep.model.entities.product;

import com.example.projektsklep.model.dto.CategoryTreeDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

class CategoryTest {
    @Mock
    Category parentCategory;
    @Mock
    List<Category> children;
    @InjectMocks
    Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSetId() {
        category.setId(Long.valueOf(1));
    }

    @Test
    void testSetName() {
        category.setName("name");
    }

    @Test
    void testSetParentCategory() {
        category.setParentCategory(new Category("name"));
    }

    @Test
    void testSetChildren() {
        category.setChildren(List.of(new Category("name")));
    }
    @Test
    public void shouldReturnChildrenList() {
        // Given
        Category parentCategory = new Category("Książki");
        Category childCategory1 = new Category("Fantastyka");
        Category childCategory2 = new Category("Kryminał");
        parentCategory.addChild(childCategory1);
        parentCategory.addChild(childCategory2);

        // When
        List<Category> children = parentCategory.getChildren();

        // Then
        assertThat(children).containsExactlyInAnyOrder(childCategory1, childCategory2);
    }
//    @Test
//    public void shouldReturnParentCategory() {
//        // Given
//        Category parentCategory = new Category("Książki");
//        Category childCategory = new Category("Fantastyka", parentCategory);
//
//        // When
//        Category parent = childCategory.getParent();
//
//        // Then
//        assertThat(parent).isSameAs(parentCategory);
//    }
    @Test
    public void shouldConvertCategoryToTreeDTO() {
        // Given
        Category category = new Category("Książki");

        // When
        CategoryTreeDTO treeDTO = Category.toTreeDTO(category);

        // Then
        assertThat(treeDTO.getId()).isEqualTo(category.getId());
        assertThat(treeDTO.getName()).isEqualTo(category.getName());
    }


}
