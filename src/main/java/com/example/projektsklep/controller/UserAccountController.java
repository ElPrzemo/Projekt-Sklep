package com.example.projektsklep.controller;

import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.dto.OrderDTO;
import com.example.projektsklep.model.dto.UserDTO;
import com.example.projektsklep.service.OrderService;
import com.example.projektsklep.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/edit")
    public String updateProfileAndAddress(@ModelAttribute UserDTO userDTO,
                                          @ModelAttribute AddressDTO addressDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long userId = userService.findUserByEmail(email)
                .map(UserDTO::id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Tutaj tworzymy nowy UserDTO z danymi, które użytkownik ma prawo zmienić
        UserDTO updatedUserDTO = new UserDTO(
                userId,
                userDTO.email(),         // Email może być zmieniony
                userDTO.firstName(),     // Imię pozostaje bez zmian
                userDTO.lastName(),      // Nazwisko pozostaje bez zmian
                userDTO.phoneNumber(), // Numer telefonu zmieniony
                null,                    // Hasło nie jest przekazywane w tej metodzie
                addressDTO               // Zaktualizowany adres
        );

        userService.updateUserProfileOrAdmin(userId, updatedUserDTO, false); // false, ponieważ to nie jest admin
        return "redirect:/account/profile";
    }
}