package com.example.projektsklep.model.entities.product;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parentCategory;

}