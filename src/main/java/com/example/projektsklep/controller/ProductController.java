package com.example.projektsklep.controller;

import com.example.projektsklep.model.dto.ProductDTO;
import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.service.BasketService;
import com.example.projektsklep.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final BasketService basketService;

    public ProductController(ProductService productService, BasketService basketService) {
        this.productService = productService;
        this.basketService = basketService;
    }

    @GetMapping
    public String listProducts(@RequestParam(name = "viewType", defaultValue = "grid") String viewType, Model model) {
        model.addAttribute("products", productService.findAllProductDTOs());
        return viewType.equals("list") ? "products_list" : "products_grid";
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("productDTO", new ProductDTO(null, null, null, "", "", "", null, null)); // Użyj wartości domyślnych lub null
        return "product_add";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute ProductDTO productDTO) {
        productService.saveProductDTO(productDTO);
        return "redirect:/products";
    }


    @GetMapping("/basket/add/{productId}")
    public String addProductToBasket(@PathVariable Long productId, RedirectAttributes redirectAttributes) {
        Product product = productService.findProductById(productId); // Zmieniamy na wyszukiwanie Product
        if (product != null) {
            basketService.addProduct(product); // Dodajemy Product do koszyka
            redirectAttributes.addFlashAttribute("success", "Produkt dodany do koszyka.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Produkt nie znaleziony.");
        }
        return "redirect:/products_list";
    }
    @DeleteMapping("/basket/remove/{productId}")
    public String removeProductFromBasket(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("successMessage", "Produkt został usunięty.");
        return "redirect:/products";
    }


}

