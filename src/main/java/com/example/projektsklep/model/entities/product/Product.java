package com.example.projektsklep.model.entities.product;



import com.example.projektsklep.model.enums.ProductType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;


@Entity
@Data
@AllArgsConstructor
@Table(name = "products")
public class Product {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 @ManyToOne
 @JoinColumn(name = "author_id")
 private AuthorEmbeddable author;

 @ManyToOne
 @JoinColumn(name = "category_id")
 private CategoryEmbeddable category;

 private String title;

 private String description;

 private String miniature;

 private BigDecimal price;

 private ProductType productType;

 private boolean published;

 private int quantity;

 public Product() {
 }

 public Product(String title, String description, String miniature, AuthorEmbeddable author, CategoryEmbeddable category, BigDecimal price, ProductType productType, int quantity) {
  this.title = title;
  this.description = description;
  this.miniature = miniature;
  this.author = author;
  this.category = category;
  this.price = price;
  this.productType = productType;
  this.quantity = quantity;
 }
}