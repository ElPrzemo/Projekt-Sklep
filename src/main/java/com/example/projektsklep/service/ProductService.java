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

        Author author = new Author();
        author.setId(productDTO.authorId());
        product.setAuthor(author);

        // Pass categoryId as a parameter to this method
        Category category = new Category();
        category.setId(productDTO.categoryId());
        product.setCategory(category);

        return product;
    }
    public Page<ProductDTO> searchProducts(String searchTerm, Long categoryId, Double minPrice, Double maxPrice, Long authorId, Pageable pageable) {
        BigDecimal minPriceDecimal = minPrice != null ? BigDecimal.valueOf(minPrice) : null;
        BigDecimal maxPriceDecimal = maxPrice != null ? BigDecimal.valueOf(maxPrice) : null;

        // Metoda zwraca teraz Page<Product>
        Page<Product> productPage = productRepository.findAllWithCriteria(
                searchTerm, minPriceDecimal, maxPriceDecimal, categoryId, authorId, pageable
        );

        // Konwertowanie wyników do Page<ProductDTO>
        return productPage.map(this::convertToProductDTO);
    }

    public ProductDTO createDefaultProductDTO() {
        return ProductDTO.builder()
                .id(null)
                .title("")
                .description("")
                .miniature("")
                .price(BigDecimal.ZERO)
                .productType(ProductType.DEFAULT_TYPE) // Zakładam, że ProductType.DEFAULT_TYPE to jakaś domyślna wartość.
                .build();
    }


}