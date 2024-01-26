package com.example.projektsklep.service;


import com.example.projektsklep.model.dto.ProductDTO;
import com.example.projektsklep.model.entities.product.AuthorEmbeddable;
import com.example.projektsklep.model.entities.product.CategoryEmbeddable;
import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.model.enums.ProductType;
import com.example.projektsklep.model.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        AuthorEmbeddable author = new AuthorEmbeddable();
        author.setId(productDTO.authorId());
        product.setAuthor(author);

        CategoryEmbeddable category = new CategoryEmbeddable();
        category.setId(productDTO.categoryId());
        product.setCategory(category);

        return product;
    }
    public Page<ProductDTO> searchProducts(String searchTerm) {
        // Utwórz obiekt Pageable z wartościami domyślnymi (0 strona, 10 elementów na stronę)
        Pageable pageable = PageRequest.of(0, 10);

        // Znajdź wszystkie produkty, których tytuł zawiera wyszukiwany termin
        List<Product> products = productRepository.findAllByTitleContainingIgnoreCase(searchTerm);

        // Konwertuj produkty na obiekty ProductDTO
        List<ProductDTO> productDTOs = products.stream()
                .map(this::convertToProductDTO)
                .collect(Collectors.toList());

        // Utwórz obiekt Page<ProductDTO> z przekonwertowanymi produktami
        Page<ProductDTO> productDTOPage = new PageImpl<>(productDTOs, pageable, products.size());

        // Zwróc obiekt Page<ProductDTO>
        return productDTOPage;
    }



}