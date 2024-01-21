package com.example.projektsklep.controller;

import com.example.projektsklep.model.dto.OrderDTO;
import com.example.projektsklep.model.dto.UserDTO;
import com.example.projektsklep.service.OrderService;
import com.example.projektsklep.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/account")
public class UserAccountController {

    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public UserAccountController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/my_orders")
    public String listUserOrders(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Pobierz adres e-mail zalogowanego użytkownika

        Optional<UserDTO> userDTO = userService.findUserByEmail(email);
        userDTO.ifPresent(u -> {
            List<OrderDTO> orders = orderService.findAllOrdersByUserId(u.id());
            model.addAttribute("orders", orders);
        });

        return "user_orders"; // Strona z zamówieniami użytkownika
    }
}