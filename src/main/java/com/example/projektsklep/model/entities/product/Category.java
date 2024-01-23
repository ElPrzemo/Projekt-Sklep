package com.example.projektsklep.model.entities.product;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

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

    public List<Category> getChildren() {

        return null;
    }

    public CategoryTree getParent() {
        return null;
    }
}