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


    @GetMapping
    public String listProducts(@RequestParam(name = "viewType", defaultValue = "list") String viewType,
                               @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                               @RequestParam(name = "gridSize", defaultValue = "20") int gridSize,
                               @RequestParam(name = "page", defaultValue = "0") int page,
                               Model model) {
        Pageable pageable = viewType.equals("list") ? PageRequest.of(page, pageSize) : PageRequest.of(page, gridSize);
        Page<ProductDTO> productsPage = productService.findAllProductDTOs(pageable);

        model.addAttribute("productsPage", productsPage);
        model.addAttribute("currentViewType", viewType);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("gridSize", gridSize);

        return viewType.equals("list") ? "products_list" : "products_grid";
    }


    @GetMapping("/{productId}")
    public String productDetails(@PathVariable Long productId, Model model) {
        ProductDTO productDTO = productService.findProductDTOById(productId);
        if (productDTO == null) {
            // Można tu zwrócić stronę błędu lub przekierować na stronę z informacją, że produkt nie istnieje
            return "error"; // Przykład nazwy widoku dla strony "Produkt nie znaleziony"
        }
        model.addAttribute("product", productDTO);
        return "product_details";
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
    public String showSearchForm(Model model, Pageable pageable) {
        // Upewnij się, że `productsPage` jest dostępny w modelu nawet jeśli jest pusty
        Page<ProductDTO> emptyPage = Page.empty(pageable);
        model.addAttribute("productsPage", emptyPage);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("authors", authorService.findAll());
        return "product_search_form"; // Zwróć widok formularza wyszukiwania
    }

    @PostMapping("/search")
    public String handleSearch(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, // Możesz usunąć tę wartość domyślną, jeśli chcesz, aby była dynamicznie zmieniana przez użytkownika
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDTO> products = productService.searchProducts(title, categoryId, authorId, pageable);

        model.addAttribute("productsPage", products);
        model.addAttribute("selectedPageSize", size); // Dodajemy to do modelu, aby móc odtworzyć wybraną liczbę rekordów na stronie
        model.addAttribute("title", title); // Przekazujemy te parametry z powrotem do widoku, aby można było je użyć w URL paginacji
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("authorId", authorId);
        return "product_search_results"; // Zwróć nazwę widoku z wynikami wyszukiwania
    }


    @ExceptionHandler(ProductNotFoundException.class)
    public String handleProductNotFound(Model model) {
        model.addAttribute("error", "Produkt nie znaleziony");
        return "product_not_found"; // Nazwa widoku błędu
    }


}





