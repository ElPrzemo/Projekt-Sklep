package com.example.projektsklep.service;


import com.example.projektsklep.model.dto.ProductDTO;

import com.example.projektsklep.model.entities.product.Author;
import com.example.projektsklep.model.entities.product.Category;
import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.model.enums.ProductType;
import com.example.projektsklep.model.repository.ProductRepository;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // W ProductService
    public Page<ProductDTO> findAllProductDTOs(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::convertToProductDTO);
    }

    public ProductDTO findProductDTOById(Long id) {
        return productRepository.findById(id)
                .map(this::convertToProductDTO)
                .orElse(null);
    }

    public ProductDTO saveProductDTO(ProductDTO productDTO) {
        Product product = convertToProduct(productDTO);
        Product savedProduct = productRepository.save(product);
        return convertToProductDTO(savedProduct);
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private ProductDTO convertToProductDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .miniature(product.getMiniature())
                .price(product.getPrice())
                .productType(product.getProductType())
                .authorId(product.getAuthor() != null ? product.getAuthor().getId() : null)
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .build();
    }

    private Product convertToProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.id());
        product.setTitle(productDTO.title());
        product.setDescription(productDTO.description());
        product.setMiniature(productDTO.miniature());
        product.setPrice(productDTO.price());
        product.setProductType(productDTO.productType());

        // Handle authorId and categoryId
        if (productDTO.authorId() != null) {
            Author author = new Author();
            author.setId(productDTO.authorId());
            product.setAuthor(author);
        } else {
            product.setAuthor(null);
        }

        if (productDTO.categoryId() != null) {
            Category category = new Category();
            category.setId(productDTO.categoryId());
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }

        return product;
    }

    public Page<ProductDTO> findProductsByTitle(String title, Pageable pageable) {
        return productRepository.findByTitleContainingIgnoreCase(title, pageable)
                .map(this::convertToProductDTO);
    }

    public Page<ProductDTO> findProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable)
                .map(this::convertToProductDTO);
    }

    public Page<ProductDTO> findProductsByAuthor(Long authorId, Pageable pageable) {
        return productRepository.findByAuthorId(authorId, pageable)
                .map(this::convertToProductDTO);
    }
    public Page<ProductDTO> searchProducts(String title, Long categoryId, Long authorId, Pageable pageable) {
        Page<Product> products;

        if (title != null) {
            products = productRepository.findByTitleContainingIgnoreCase(title, pageable);
        } else if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId, pageable);
        } else if (authorId != null) {
            products = productRepository.findByAuthorId(authorId, pageable);
        } else {
            products = productRepository.findAll(pageable);
        }

        return products.map(this::convertToProductDTO);
    }

    public ProductDTO createDefaultProductDTO() {
        // Ustaw domyślne wartości lub prawidłowe dane
        Long id = 1L; // Przykładowe ID
        String title = "Default Title"; // Tytuł
        String description = "Default Description"; // Opis
        String miniature = "Default Miniature"; // Miniatura
        BigDecimal price = BigDecimal.valueOf(10.0); // Cena
        ProductType productType = ProductType.DEFAULT_TYPE; // Typ produktu

        // Utwórz instancję ProductDTO
        ProductDTO productDTO = ProductDTO.builder()
                .id(id)
                .title(title)
                .description(description)
                .miniature(miniature)
                .price(price)
                .productType(productType)
                .build();

        return productDTO;
    }
    public List<ProductType> findAllProductTypes() {
        return Arrays.asList(ProductType.values());
    }

}


