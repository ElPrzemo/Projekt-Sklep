package com.example.projektsklep.model.entities.product;

import com.example.projektsklep.model.enums.ProductType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "products")
public class Product {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 @ManyToOne // Establish the many-to-one relationship with Author
 @JoinColumn(name = "author_id")
 private Author author;

 @ManyToOne // Establish the many-to-one relationship with Category
 @JoinColumn(name = "category_id")
 private Category category;

 private String title;
 private String description;
 private String miniature;
 private BigDecimal price;
 private ProductType productType;
 private boolean published;

 @Column(name = "date_created")
 @DateTimeFormat(pattern = "yyyy-MM-dd")
 private LocalDate dateCreated;

 // Constructors, getters, and setters...
}