package com.example.projektsklep.model.entities.product;

import com.example.projektsklep.model.dto.CategoryTreeDTO;
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

    @OneToMany(mappedBy = "parentCategory")
    private List<Category> children;

    public List<Category> getChildren() {
        return children;
    }

    public Category getParent() {
        return parentCategory;
    }

    public static CategoryTreeDTO toTreeDTO(Category category) {
        CategoryTreeDTO categoryTreeDTO = new CategoryTreeDTO();
        categoryTreeDTO.setId(category.getId());
        categoryTreeDTO.setName(category.getName());
        // You can set other properties of the DTO as needed
        return categoryTreeDTO;
    }
}