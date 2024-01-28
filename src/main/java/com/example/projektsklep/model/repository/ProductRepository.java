package com.example.projektsklep.model.repository;



import com.example.projektsklep.model.entities.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    Page<Product> findAll(Pageable pageable);


    Optional<Product> findById(long id);

    Product save(Product product);

    void delete(Product product);

    void deleteById(Long id);


    @Query("SELECT p FROM Product p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Product> findAllByTitleContainingIgnoreCase(String searchTerm);


    @Query("SELECT p FROM Product p WHERE " +
            "(:title is null or LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:minPrice is null or p.price >= :minPrice) AND " +
            "(:maxPrice is null or p.price <= :maxPrice) AND " +
            "(:categoryId is null or p.category.id = :categoryId) AND " +
            "(:authorId is null or p.author.id = :authorId)")
    Page<Product> findAllWithCriteria(@Param("title") String title,
                                      @Param("minPrice") BigDecimal minPrice,
                                      @Param("maxPrice") BigDecimal maxPrice,
                                      @Param("categoryId") Long categoryId,
                                      @Param("authorId") Long authorId,
                                      Pageable pageable);
}