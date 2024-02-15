package com.example.projektsklep.controller;

import com.example.projektsklep.exception.CategoryException;
import com.example.projektsklep.model.dto.CategoryDTO;
import com.example.projektsklep.model.entities.product.Category;
import com.example.projektsklep.model.entities.product.CategoryTree;
import com.example.projektsklep.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        List<CategoryDTO> categories = categoryService.getAllCategoryDTOs().stream().map(category -> {
            String parentCategoryName = category.parentCategoryId() != null
                    ? categoryService.getCategoryDTOById(category.parentCategoryId()).name()
                    : null;
            return new CategoryDTO(category.id(), category.name(), category.parentCategoryId(), parentCategoryName);
        }).collect(Collectors.toList());
        model.addAttribute("categories", categories);
        return "admin_category_list";
    }




//    @GetMapping("/tree")
//    public String showCategoriesTree(Model model) {
//        List<CategoryTree> categories = categoryService.getCategoriesTree();
//        model.addAttribute("categories", categories);
//        return "categories_tree";
//    }
}