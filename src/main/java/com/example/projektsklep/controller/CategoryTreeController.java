package com.example.projektsklep.controller;

import com.example.projektsklep.exception.CategoryTreeException;
import com.example.projektsklep.model.entities.product.Category;
import com.example.projektsklep.model.repository.CategoryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CategoryTreeController {

    private final CategoryRepository categoryRepository;

    public CategoryTreeController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/categorytree")
    public List<Object> getCategoryTree() {
        try {
            return categoryRepository.findAll().stream()
                    .map(Category::toTreeDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CategoryTreeException("Error retrieving category tree", e);
        }
    }
}





