package com.example.projektsklep.controller;

import com.example.projektsklep.model.dto.ProductDTO;
import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.service.BasketService;
import com.example.projektsklep.exception.ProductNotFoundException;
import com.example.projektsklep.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@ControllerAdvice
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final BasketService basketService;

    public ProductController(ProductService productService, BasketService basketService) {
        this.productService = productService;
        this.basketService = basketService;
    }

    // W ProductController
    @GetMapping
    public String listProducts(@RequestParam(name = "viewType", defaultValue = "grid") String viewType,
                               @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                               @RequestParam(name = "gridSize", defaultValue = "20") int gridSize,
                               @RequestParam(name = "page", defaultValue = "0") int page,
                               Model model) {
        Pageable pageable;
        if (viewType.equals("list")) {
            pageable = PageRequest.of(page, pageSize);
        } else { // Dla widoku grid
            pageable = PageRequest.of(page, gridSize);
        }
        Page<ProductDTO> productPage = productService.findAllProductDTOs(pageable);
        model.addAttribute("productsPage", productPage);
        model.addAttribute("currentViewType", viewType);
        return viewType.equals("list") ? "products_list" : "products_grid";
    }






    @GetMapping("/basket/add/{productId}")
    public String addProductToBasket(@PathVariable Long productId, RedirectAttributes redirectAttributes) {
        Product product = productService.findProductById(productId); // Zmieniamy na wyszukiwanie Product
        if (product != null) {
            basketService.addProduct(product); // Dodajemy Product do koszyka
            redirectAttributes.addFlashAttribute("success", "Produkt dodany do koszyka.");
        } else {
            throw new ProductNotFoundException("Produkt o podanym ID nie istnieje.");
        }
        return "redirect:/products_list";
    }

    @DeleteMapping("/basket/remove/{productId}")
    public String removeProductFromBasket(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("successMessage", "Produkt został usunięty.");
        return "redirect:/products";
    }
    @GetMapping("/search")
    public String searchProducts(
            @RequestParam(name = "searchTerm", required = false) String searchTerm,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "minPrice", required = false) Double minPrice,
            @RequestParam(name = "maxPrice", required = false) Double maxPrice,
            @RequestParam(name = "authorId", required = false) Long authorId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            Model model) {

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<ProductDTO> productDTOPage = productService.searchProducts(searchTerm, categoryId, minPrice, maxPrice, authorId, pageable);

        model.addAttribute("productsPage", productDTOPage);
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("authorId", authorId);
        // Dodaj inne atrybuty potrzebne dla formularza

        return "product_search_form"; // Nazwa widoku Thymeleaf
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public String handleProductNotFound(Model model) {
        model.addAttribute("error", "Produkt nie znaleziony");
        return "product_not_found"; // Nazwa widoku błędu
    }


}





