package com.example.projektsklep.model.entities.product;

import com.example.projektsklep.model.enums.ProductType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductTest {

    @Mock
    private Author mockAuthor;
    @Mock
    private Category mockCategory;
    @InjectMocks
    private Product product;


    @Test
    public void testConstructorWithAllArgs_ShouldCreateProductCorrectly() {
        Product product = new Product(1L, mockAuthor, mockCategory, "Title", "Description",
                "miniature.jpg", BigDecimal.TEN, ProductType.COOKING_EQUIPMENT, true, LocalDate.now());

        assertEquals(1L, product.getId());
        assertEquals(mockAuthor, product.getAuthor());

    }

    @Test
    public void testSetAuthor_ShouldUpdateAuthorCorrectly() {
        Product product = new Product();
        product.setAuthor(mockAuthor);

        assertSame(mockAuthor, product.getAuthor());
    }


    @Test
    public void testSetDescription_ShouldSetDescription() {
        Product product = new Product();
        String newDescription = "New description";
        product.setDescription(newDescription);

        assertEquals(newDescription, product.getDescription());
    }

    @Test
    public void testSetDescription_ShouldSetEmptyDescription() {
        Product product = new Product();
        product.setDescription("");

        assertEquals("", product.getDescription());
    }

    @Test
    public void testIsPublished_ShouldReturnFalseForNewProduct() {
        Product product = new Product();

        assertFalse(product.isPublished());
    }

    @Test
    public void testSetPublished_ShouldSetTrue() {
        Product product = new Product();
        product.setPublished(true);

        assertTrue(product.isPublished());
    }


}
