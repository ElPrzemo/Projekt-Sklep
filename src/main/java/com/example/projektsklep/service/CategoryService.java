package com.example.projektsklep.service;

import com.example.projektsklep.model.dto.CategoryDTO;
import com.example.projektsklep.model.dto.CategoryTreeDTO;
import com.example.projektsklep.model.entities.product.Category;
import com.example.projektsklep.model.entities.product.CategoryTree;
import com.example.projektsklep.model.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public CategoryDTO addCategoryWithParent(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.name());

        if (categoryDTO.parentCategoryId() != null) {
            Category parentCategory = categoryRepository.findById(categoryDTO.parentCategoryId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParentCategory(parentCategory);
        }

        Category savedCategory = categoryRepository.save(category);
        return convertToCategoryDTO(savedCategory);
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
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public CategoryDTO updateCategoryDTO(Long id, CategoryDTO updatedCategoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        updateCategoryData(category, updatedCategoryDTO);
        return convertToCategoryDTO(categoryRepository.save(category));
    }

    public List<CategoryTree> getCategoriesTree() {
        List<Category> categories = categoryRepository.findAll();
        Map<Long, CategoryTree> categoryTreesMap = new HashMap<>();

        for (Category category : categories) {
            CategoryTreeDTO categoryTreeDTO = new CategoryTreeDTO(category);
            CategoryTree categoryTree = new CategoryTree();
            categoryTree.setId(category.getId());
            categoryTree.setName(category.getName());
            categoryTree.setParent(category.getParent() != null ? new CategoryTree(category.getParent().getId(), category.getParent().getName(), null) : null);
            categoryTree.setChildren(new ArrayList<>());

            categoryTreesMap.put(category.getId(), categoryTree);
        }

        for (Category category : categories) {
            if (category.getParent() != null) {
                CategoryTree parentCategoryTree = categoryTreesMap.get(category.getParent().getId());
                parentCategoryTree.getChildren().add(categoryTreesMap.get(category.getId()));
            }
        }

        return new ArrayList<>(categoryTreesMap.values());
    }

    public CategoryDTO saveOrUpdateCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.name());
        if (categoryDTO.parentCategoryId() != null) {
            Category parent = categoryRepository.findById(categoryDTO.parentCategoryId()).orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParentCategory(parent);
        }
        // Additional logic for setting other fields or handling update scenarios
        Category savedCategory = categoryRepository.save(category);
        return convertToCategoryDTO(savedCategory);
    }

    @Transactional
    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }

    private CategoryDTO convertToCategoryDTO(Category category) {
        String parentCategoryName = category.getParentCategory() != null ? category.getParentCategory().getName() : null;
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getParentCategory() != null ? category.getParentCategory().getId() : null,
                parentCategoryName
        );
    }
    private void updateCategoryData(Category category, CategoryDTO categoryDTO) {
        category.setName(categoryDTO.name());
        // Ustaw parentCategory, jeśli jest wymagane
    }
}