package com.example.projektsklep.controller;


import com.example.projektsklep.exception.CategoryException;
import com.example.projektsklep.exception.OrderNotFoundException;
import com.example.projektsklep.model.dto.*;
import com.example.projektsklep.model.entities.order.Order;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.enums.OrderStatus;
import com.example.projektsklep.model.repository.UserRepository;
import com.example.projektsklep.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final OrderService orderService;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    private final ProductService productService;
    private final UserRepository userRepository;


    public AdminController(UserService userService, OrderService orderService, AuthorService authorService, CategoryService categoryService, ProductService productService, UserRepository userRepository) {
        this.userService = userService;
        this.orderService = orderService;
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.productService = productService;
        this.userRepository = userRepository;
    }

    @GetMapping("/panel")
    public String showAdminPanel() {
        return "adminPanel"; // Zwraca adminPanel.html
    }

    @GetMapping("/user_search")
    public String showUserSearchForm() {
        return "admin_user_search"; // widok z formularzem wyszukiwania użytkowników
    }

    @PostMapping("/user_search")
    public String showUserOrders(@RequestParam String lastName, Model model) {
        List<UserDTO> users = userService.findUsersByLastName(lastName);
        model.addAttribute("users", users);
        return "admin_user_list"; // widok z listą użytkowników pasujących do kryteriów wyszukiwania
    }
//
//    @PostMapping("/user_search")
//    public String searchUsersByLastNamePost(@RequestParam String lastName, Model model) {
//        List<UserDTO> users = userService.findUsersByLastName(lastName);
//        model.addAttribute("users", users);
//        return "admin_user_list"; // Widok z listą użytkowników pasujących do kryteriów wyszukiwania
//    }



    @GetMapping("/user_details/{userId}")
    public String userDetails(@PathVariable("userId") Long userId, Model model) {
        // Pobieranie szczegółów użytkownika na podstawie identyfikatora użytkownika
        Optional<UserDTO> user = userService.findUserById(userId);
        model.addAttribute("user", user);
        return "admin/user_details"; // Zwraca widok szczegółów użytkownika
    }

    @GetMapping("/author")
    public String showAuthorForm(Model model) {
        model.addAttribute("author", new AuthorDTO(null, ""));
        return "admin_author_form";
    }

    @PostMapping("/author")
    public String saveAuthor(@Valid @ModelAttribute("author") AuthorDTO authorDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "admin_author_form";
        }
        authorService.saveAuthor(authorDTO);
        return "redirect:/admin/authors";
    }

    @GetMapping("/authors")
    public String listAuthors(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AuthorDTO> authorPage = authorService.findAllAuthorsPageable(pageable);
        model.addAttribute("authorPage", authorPage);
        return "admin_author_list";
    }

    @GetMapping("/addProduct")
    public String showAddProductForm(Model model) {
        ProductDTO productDTO = productService.createDefaultProductDTO();
        model.addAttribute("product", productDTO);

        // Dodaj listy do modelu
        model.addAttribute("productTypes", productService.findAllProductTypes());
        model.addAttribute("categories", categoryService.findAll()); // Używamy nowej metody serwisu
        model.addAttribute("authors", authorService.findAll());

        return "admin_add_product";
    }


    // Metoda POST do przetwarzania dodawania produktu
    @PostMapping("/addProduct")
    public String addProduct(@ModelAttribute ProductDTO productDTO, Model model) {
        productService.saveProductDTO(productDTO);
        // Przygotuj model dla nowego formularza
        ProductDTO newProductDTO = productService.createDefaultProductDTO();
        model.addAttribute("product", newProductDTO);
        model.addAttribute("productTypes", productService.findAllProductTypes());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("successMessage", "Produkt został dodany.");
        return "admin_add_product"; // Zostaje na stronie z formularzem
    }

    @GetMapping("/user_orders/{userId}")
    public String listUserOrders(@PathVariable Long userId, Model model) {
        List<OrderDTO> orders = orderService.findAllOrdersByUserId(userId);
        model.addAttribute("orders", orders);
        return "admin_user_orders"; // Nazwa pliku HTML z listą zamówień
    }


    @GetMapping("/ordersByStatus")
    public String getOrdersByStatus(@RequestParam(required = false) OrderStatus orderStatus, Model model) {
        // Ustaw domyślny status, jeśli nie podano
        if (orderStatus == null) {
            orderStatus = OrderStatus.NEW_ORDER;
        }

        List<OrderDTO> orders = orderService.findAllOrdersByStatus(orderStatus);
        model.addAttribute("orders", orders);
        model.addAttribute("selectedStatus", orderStatus.name()); // Dodaj wybrany status do modelu
        return "orders_by_status"; // Zwraca nazwę widoku Thymeleafa
    }


    //CATEGORIES
    // CATEGORIES
    // CATEGORIES
    // CATEGORIES
    // CATEGORIES


    @GetMapping("/addCategory")
    public String showAddForm(Model model) {
        // Tworzenie nowego DTO dla formularza, jeśli jeszcze nie istnieje
        if (!model.containsAttribute("categoryDTO")) {
            model.addAttribute("categoryDTO", new CategoryDTO(null, "", null, null));
        }
        List<CategoryDTO> allCategories = categoryService.getAllCategoryDTOs(); // Załóżmy, że ta metoda istnieje i zwraca listę wszystkich kategorii
        model.addAttribute("allCategories", allCategories); // Dodaj listę wszystkich kategorii do modelu
        return "category_add"; // Nazwa Twojego pliku HTML formularza dodawania kategorii
    }

    @PostMapping("/addCategory")
    public String addCategory(@ModelAttribute CategoryDTO categoryDTO) {
        try {
            categoryService.addCategoryWithParent(categoryDTO);
            return "redirect:/categories";
        } catch (Exception e) {
            throw new CategoryException("Error adding category", e);
        }
    }

    @GetMapping("/editCategory/{id}")
    public String editCategoryForm(@PathVariable Long id, Model model) {
        CategoryDTO categoryDTO = categoryService.getCategoryDTOById(id);
        model.addAttribute("category", categoryDTO); // Zmieniłem nazwę atrybutu na "category" dla spójności z formularzem Thymeleaf
        return "category_edit"; // Nazwa pliku widoku do edycji kategorii
    }

    @PostMapping("/editCategory/{id}")
    public String editCategory(@PathVariable Long id, @ModelAttribute("category") CategoryDTO categoryDTO) {
        categoryService.updateCategoryDTO(id, categoryDTO);
        return "redirect:/categories";
    }

    @PostMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return "redirect:/categories";
    }


    // ORDERS
    // ORDERS
    // ORDERS
    // ORDERS
    // ORDERS


    @GetMapping("/editOrderStatus/{orderId}")
    public String showEditOrderForm(@PathVariable Long orderId, Model model) {
        try {
            OrderDTO orderDTO = orderService.findOrderDTOById(orderId);
            model.addAttribute("order", orderDTO);
            model.addAttribute("statuses", OrderStatus.values());
            return "order_edit_form";
        } catch (OrderNotFoundException e) {
            model.addAttribute("error", "Zamówienie nie znalezione");
            return "error";
        }
    }

    @PostMapping("/editOrderStatus/{orderId}")
    public String updateOrderStatus(@PathVariable Long orderId, @ModelAttribute("order") OrderDTO orderDTO, Model model, HttpServletRequest request) {
        try {
            orderService.updateOrderStatus(orderId, orderDTO.orderStatus());
            String referer = request.getHeader("Referer");
            return "redirect:" + (referer != null ? referer : "/user_orders");
        } catch (OrderNotFoundException e) {
            model.addAttribute("error", "Zamówienie nie znalezione");
            return "error";
        } catch (Exception e) {
            model.addAttribute("error", "Błąd podczas aktualizacji statusu zamówienia");
            return "order_edit_form"; // Tutaj możemy dodać ponownie atrybuty do modelu, jeśli potrzebujemy
        }
    }
}
