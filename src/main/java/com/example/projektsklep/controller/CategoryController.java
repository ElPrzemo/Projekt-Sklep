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
            // Uzyskaj nazwę kategorii nadrzędnej, jeśli istnieje
            String parentCategoryName = category.parentCategoryId() != null
                    ? categoryService.getCategoryDTOById(category.parentCategoryId()).name()
                    : null;
            return new CategoryDTO(category.id(), category.name(), category.parentCategoryId(), parentCategoryName);
        }).collect(Collectors.toList());
        model.addAttribute("categories", categories);
        return "admin_category_list";
    }



    @GetMapping("/add")
    public String showAddForm(Model model) {
        // Tworzenie nowego DTO dla formularza, jeśli jeszcze nie istnieje
        if (!model.containsAttribute("categoryDTO")) {
            model.addAttribute("categoryDTO", new CategoryDTO(null, "", null, null));
        }
        List<CategoryDTO> allCategories = categoryService.getAllCategoryDTOs(); // Załóżmy, że ta metoda istnieje i zwraca listę wszystkich kategorii
        model.addAttribute("allCategories", allCategories); // Dodaj listę wszystkich kategorii do modelu
        return "category_add"; // Nazwa Twojego pliku HTML formularza dodawania kategorii
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute CategoryDTO categoryDTO) {
        Category category;
        try {
            category = new Category();
            category.setName(categoryDTO.name());
            // You may need to set the parentCategory if required
            categoryService.addCategory(category);
        } catch (Exception e) {
            throw new CategoryException("Error adding category", e);
        }
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable Long id, Model model) {
        CategoryDTO categoryDTO = categoryService.getCategoryDTOById(id);
        model.addAttribute("category", categoryDTO); // Zmieniłem nazwę atrybutu na "category" dla spójności z formularzem Thymeleaf
        return "category_edit"; // Nazwa pliku widoku do edycji kategorii
    }

    @PostMapping("/edit/{id}")
    public String editCategory(@PathVariable Long id, @ModelAttribute("category") CategoryDTO categoryDTO) {
        categoryService.updateCategoryDTO(id, categoryDTO);
        return "redirect:/categories";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return "redirect:/categories";
    }

    @GetMapping("/tree")
    public String showCategoriesTree(Model model) {
        List<CategoryTree> categories = categoryService.getCategoriesTree();
        model.addAttribute("categories", categories);
        return "categories_tree";
    }
}