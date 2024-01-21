package com.example.projektsklep.controller;

import com.example.projektsklep.model.dto.OrderDTO;
import com.example.projektsklep.model.dto.UserDTO;
import com.example.projektsklep.service.OrderService;
import com.example.projektsklep.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final OrderService orderService;

    @Autowired
    public AdminController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
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
}