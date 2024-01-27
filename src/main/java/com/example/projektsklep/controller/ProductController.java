package com.example.projektsklep.controller;

import com.example.projektsklep.model.dto.ProductDTO;
import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.model.repository.ProductRepository;
import com.example.projektsklep.service.BasketService;
import com.example.projektsklep.service.ProductNotFoundException;
import com.example.projektsklep.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
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
    public String searchProducts(@RequestParam(name = "searchTerm", required = false) String searchTerm,
                                 @RequestParam(name = "viewType", defaultValue = "grid") String viewType,
                                 @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                 @RequestParam(name = "gridSize", defaultValue = "20") int gridSize,
                                 @RequestParam(name = "page", defaultValue = "0")int page,
                                 Model model) {

        Page<ProductDTO> results;
        if (searchTerm != null && !searchTerm.isEmpty()) {
            // Wyszukaj produkty pasujące do wyszukiwanego terminu
            results = productService.searchProducts(searchTerm);
        } else {
            // Pobierz wszystkie produkty, jeśli nie podano terminu wyszukiwania
            results = productService.findAllProductDTOs(PageRequest.of(page, pageSize));
        }

        Pageable pageable;
        if (viewType.equals("list")) {
            pageable = PageRequest.of(page, pageSize);
        } else { // Dla widoku grid
            pageable = PageRequest.of(page, gridSize);
        }
        Page<ProductDTO> productPage = productService.findAllProductDTOs(pageable);
        List<ProductDTO> productsPage = productPage.toList();
        model.addAttribute("productsPage", productsPage);
        model.addAttribute("currentViewType", viewType);
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("page", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("gridSize", gridSize);

        return "products_list";
    }









}

