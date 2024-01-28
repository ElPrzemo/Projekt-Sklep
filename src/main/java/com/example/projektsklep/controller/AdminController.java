package com.example.projektsklep.controller;

import com.example.projektsklep.model.dto.*;
import com.example.projektsklep.model.entities.order.Order;
import com.example.projektsklep.model.enums.OrderStatus;
import com.example.projektsklep.service.AuthorService;
import com.example.projektsklep.service.OrderService;
import com.example.projektsklep.service.ProductService;
import com.example.projektsklep.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final ProductService productService;


    public AdminController(UserService userService, OrderService orderService, AuthorService authorService, ProductService productService) {
        this.userService = userService;
        this.orderService = orderService;
        this.authorService = authorService;
        this.productService = productService;
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

    @GetMapping("/user_orders/{userId}")
    public String listOrdersByUser(@PathVariable long userId, Model model) {
        List<OrderDTO> orders = orderService.findAllOrdersByUserId(userId);
        model.addAttribute("orders", orders);
        return "admin_user_orders"; // widok zawierający listę zamówień danego użytkownika
    }

    @PostMapping("/edit_user/{userId}")
    public String updateUserAndAddress(@PathVariable Long userId, @ModelAttribute UserDTO userDTO,
                                       @ModelAttribute AddressDTO addressDTO) {
        userService.updateUserProfileOrAdmin(userId, userDTO, true); // isAdmin ustawione na true
        return "redirect:/admin/users";
    }

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
        Page<AuthorDTO> authorPage = authorService.findAllAuthors(pageable);
        model.addAttribute("authorPage", authorPage);
        return "admin_author_list";
    }
    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        ProductDTO productDTO = productService.createDefaultProductDTO();
        model.addAttribute("product", productDTO);
        return "add_product";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute ProductDTO productDTO) {
        productService.saveProductDTO(productDTO);
        return "redirect:/products";
    }

    @GetMapping("/ordersByStatus")
    public String getOrdersByStatus(@RequestParam OrderStatus orderStatus, Model model) {
        List<Order> orders = orderService.findAllOrdersByStatus(orderStatus);
        model.addAttribute("orders", orders);
        return "orders_by_status"; // Zwraca nazwę widoku Thymeleafa
    }


    @PostMapping("/changeOrderStatus/{orderId}")
    public ResponseEntity<?> changeOrderStatus(@PathVariable Long orderId, @RequestParam("newStatus") String newStatus) {
        try {
            OrderDTO orderDTO = orderService.findOrderDTOById(orderId);
            if (orderDTO != null) {
                orderDTO = new OrderDTO(orderDTO.id(), orderDTO.userId(), newStatus, orderDTO.dateCreated(), orderDTO.sentAt(), orderDTO.totalPrice(), orderDTO.lineOfOrders());
                orderService.updateOrderDTO(orderId, orderDTO);
                return new ResponseEntity<>("Status zamówienia zaktualizowany.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Zamówienie nie znalezione.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Błąd przy zmianie statusu zamówienia.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}