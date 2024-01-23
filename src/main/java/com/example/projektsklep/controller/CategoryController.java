package com.example.projektsklep.controller;



import com.example.projektsklep.model.dto.CategoryDTO;
import com.example.projektsklep.model.entities.product.Category;
import com.example.projektsklep.model.entities.product.CategoryEmbeddable;
import com.example.projektsklep.model.entities.product.CategoryTree;
import com.example.projektsklep.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;





@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listCategories(Model model) {
        List<CategoryDTO> categories = categoryService.getAllCategoryDTOs();
        model.addAttribute("categories", categories);
        return "categories";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("categoryDTO", new CategoryDTO(null, "", null));
        return "category_add";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.name());
        // Ustaw parentCategory, jeśli jest wymagane
        categoryService.addCategory(category);
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable Long id, Model model) {
        CategoryDTO categoryDTO = categoryService.getCategoryDTOById(id);
        model.addAttribute("categoryDTO", categoryDTO);
        return "category_edit";
    }


    @PostMapping("/edit/{id}")
    public String editCategory(@PathVariable Long id, @ModelAttribute CategoryDTO categoryDTO) {
        categoryService.updateCategoryDTO(id, categoryDTO);
        return "redirect:/categories";
    }
    @GetMapping("/categories/tree")
    public String showCategoriesTree(Model model) {
        List<CategoryTree> categories = categoryService.getCategoriesTree();
        model.addAttribute("categories", categories);
        return "categories_tree";
    }
}



