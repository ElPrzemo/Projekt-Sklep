package com.example.projektsklep.service;


import com.example.projektsklep.model.dto.CategoryDTO;
import com.example.projektsklep.model.entities.product.Category;
import com.example.projektsklep.model.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    public List<CategoryDTO> getAllCategoryDTOs() {
        return categoryRepository.findAll().stream()
                .map(this::convertToCategoryDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryDTOById(Long id) {
        return categoryRepository.findById(id)
                .map(this::convertToCategoryDTO)
                .orElse(null);
    }


    public CategoryDTO updateCategoryDTO(Long id, CategoryDTO updatedCategoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        updateCategoryData(category, updatedCategoryDTO);
        return convertToCategoryDTO(categoryRepository.save(category));
    }


    private CategoryDTO convertToCategoryDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .parentCategoryId(category.getParentCategory() != null ? category.getParentCategory().getId() : null)
                .build();
    }

    private void updateCategoryData(Category category, CategoryDTO categoryDTO) {
        category.setName(categoryDTO.name());
        // Ustaw parentCategory, jeśli jest wymagane
    }
}