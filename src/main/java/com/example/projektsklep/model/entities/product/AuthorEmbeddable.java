package com.example.projektsklep.model.entities.product;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class AuthorEmbeddable {

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
    @Id
    private Long id;

    public AuthorEmbeddable() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}