package com.example.projektsklep.model.entities.product;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class CategoryEmbeddable {

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Id
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


    public static Object toTreeDTO(Category category) {
        return null;
    }

}