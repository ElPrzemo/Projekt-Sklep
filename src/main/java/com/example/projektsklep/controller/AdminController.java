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
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        List<UserDTO> users = userService.findUsersByName(lastName);
        model.addAttribute("users", users);
        return "admin_user_list"; // widok z listą użytkowników pasujących do kryteriów wyszukiwania
    }


//
//    @GetMapping("/edit_user/{userId}")
//    public String showEditUserForm(@PathVariable Long userId, Model model) {
//        UserDTO userDTO = userService.findUserById(userId)
//                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika o id: " + userId));
//
//        AddressDTO addressDTO = userDTO.address() != null ? userDTO.address() :
//                new AddressDTO(null, "", "", "", ""); // Utwórz instancję AddressDTO z pustymi stringami
//
//        model.addAttribute("userDTO", userDTO);
//        model.addAttribute("addressDTO", addressDTO);
//
//        return "admin_user_edit_form"; // Nazwa Twojego pliku HTML formularza edycji użytkownika
//    }
//    @PostMapping("/edit_user/{userId}")
//    public String updateUserAndAddress(@PathVariable Long userId,
//                                       @ModelAttribute UserDTO userDTO,
//                                       @ModelAttribute AddressDTO addressDTO) {
//        userService.updateUserProfileAndAddress(userId, userDTO, addressDTO);
//        return "redirect:/admin/user_search"; // Uwaga: Zaktualizuj ścieżkę zgodnie z Twoją logiką nawigacji
//    }

    @GetMapping("/author")
    public String showAuthorForm(Model model) {
        model.addAttribute("author", new AuthorDTO());
        return "admin_author_form"; // Widok formularza dla Author
    }

    @PostMapping("/author")
    public String saveAuthor(@ModelAttribute AuthorDTO authorDTO) {
        authorService.saveAuthor(authorDTO);
        return "redirect:/admin/author"; // Przekierowanie po zapisaniu
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
    public String addProduct(@ModelAttribute ProductDTO productDTO) {
        productService.saveProductDTO(productDTO);
        return "redirect:/admin/products"; // Przekierowanie do listy produktów po pomyślnym dodaniu
    }

    @GetMapping("/ordersByStatus")
    public String getOrdersByStatus(@RequestParam(required = false) OrderStatus orderStatus, Model model) {
        // Ustaw domyślny status, jeśli nie podano
        if (orderStatus == null) {
            orderStatus = OrderStatus.NEW_ORDER;
        }

        List<Order> orders = orderService.findAllOrdersByStatus(orderStatus);
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
    }}