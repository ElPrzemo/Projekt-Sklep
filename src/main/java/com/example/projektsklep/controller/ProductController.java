package com.example.projektsklep.controller;

import com.example.projektsklep.model.dto.ProductDTO;
import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.service.AuthorService;
import com.example.projektsklep.service.BasketService;
import com.example.projektsklep.exception.ProductNotFoundException;
import com.example.projektsklep.service.CategoryService;
import com.example.projektsklep.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@Controller
@ControllerAdvice
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final BasketService basketService;

    private final CategoryService categoryService;
    private final AuthorService authorService;

    public ProductController(ProductService productService, BasketService basketService, CategoryService categoryService, AuthorService authorService) {
        this.productService = productService;
        this.basketService = basketService;
        this.categoryService = categoryService;
        this.authorService = authorService;
    }

// W ProductController





//
//
//
//    @GetMapping("/basket/add/{productId}")
//    public String addProductToBasket(@PathVariable Long productId, RedirectAttributes redirectAttributes) {
//        Product product = productService.findProductById(productId); // Zmieniamy na wyszukiwanie Product
//        if (product != null) {
//            basketService.addProduct(product); // Dodajemy Product do koszyka
//            redirectAttributes.addFlashAttribute("success", "Produkt dodany do koszyka.");
//        } else {
//            throw new ProductNotFoundException("Produkt o podanym ID nie istnieje.");
//        }
//        return "redirect:/products_list";
//    }
//
//    @DeleteMapping("/basket/remove/{productId}")
//    public String removeProductFromBasket(@PathVariable Long id, RedirectAttributes redirectAttributes) {
//        productService.deleteProduct(id);
//        redirectAttributes.addFlashAttribute("successMessage", "Produkt został usunięty.");
//        return "redirect:/products";
//    }








    @ExceptionHandler(ProductNotFoundException.class)
    public String handleProductNotFound(Model model) {
        model.addAttribute("error", "Produkt nie znaleziony");
        return "product_not_found"; // Nazwa widoku błędu
    }


}





