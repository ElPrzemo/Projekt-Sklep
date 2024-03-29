package com.example.projektsklep.controller;

import com.example.projektsklep.exception.AdminControllerException;
import com.example.projektsklep.model.dto.*;
import com.example.projektsklep.model.entities.order.Order;
import com.example.projektsklep.model.enums.OrderStatus;
import com.example.projektsklep.model.repository.UserRepository;
import com.example.projektsklep.service.*;
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

    @GetMapping("/edit_user/{userId}")
    public String showEditUserForm(@PathVariable Long userId, Model model) {
        UserDTO userDTO = userService.findUserById(userId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika o id: " + userId));
        AddressDTO addressDTO = userDTO.address() != null ? userDTO.address() :
                new AddressDTO(null, "", "", "", "");
        model.addAttribute("userDTO", userDTO);
        model.addAttribute("addressDTO", addressDTO);
        return "admin_user_edit_form";
    }


    @GetMapping("/user_details/{userId}")
    public String userDetails(@PathVariable Long userId, Model model) {
        userService.findUserById(userId).ifPresentOrElse(userDTO -> {
            model.addAttribute("user", userDTO);
            AddressDTO addressDTO = userDTO.address() != null ? userDTO.address() : new AddressDTO(null, "", "", "", "");
            model.addAttribute("address", addressDTO);
            // Dodajemy atrybut do modelu, który zawiera link do edycji użytkownika
            model.addAttribute("editLink", "/admin/edit_user/" + userId);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId);
        });

        return "user_details"; // Nazwa widoku szczegółów użytkownika
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

    @GetMapping("/products")
    public String listProducts(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDTO> productPage = productService.findAllProductDTOs(pageable);
        model.addAttribute("productPage", productPage);
        return "admin_product_list"; // Nazwa Twojego pliku HTML z listą produktów
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

    @PostMapping("/changeOrderStatus/{orderId}")
    public ResponseEntity<?> changeOrderStatus(@PathVariable Long orderId, @RequestParam("newStatus") String newStatus) {
        try {
            OrderDTO orderDTO = orderService.findOrderDTOById(orderId);
            if (orderDTO == null) {
                return new ResponseEntity<>("Zamówienie nie znalezione.", HttpStatus.NOT_FOUND);
            } else {
                // Założenie: Tworzymy pusty AddressDTO jako zaślepkę
                AddressDTO defaultAddress = new AddressDTO(null, "defaultStreet", "defaultCity", "defaultPostalCode", "defaultCountry");

                // Access dateCreated directly as a field
                orderDTO = new OrderDTO(orderDTO.id(), orderDTO.userId(), newStatus, orderDTO.dateCreated(), orderDTO.sentAt(), orderDTO.totalPrice(), orderDTO.lineOfOrders(), defaultAddress);

                if (!orderService.updateOrderDTO(orderId, orderDTO)) {
                    throw new AdminControllerException("Nie można zaktualizować statusu zamówienia");
                }
                return new ResponseEntity<>("Status zamówienia zaktualizowany.", HttpStatus.OK);
            }
        } catch (Exception e) {
            if (e instanceof AdminControllerException) {
                return new ResponseEntity<>(e.getMessage(), ((AdminControllerException) e).getStatus());
            } else {
                return new ResponseEntity<>("Nieznany błąd podczas aktualizacji statusu zamówienia.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}