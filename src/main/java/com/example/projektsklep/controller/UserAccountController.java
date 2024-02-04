package com.example.projektsklep.controller;

import com.example.projektsklep.exception.AddressUpdateException;
import com.example.projektsklep.exception.UserNotFoundException;
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
@ControllerAdvice
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
        if (!userDTO.isPresent()) {
            throw new UserNotFoundException("Użytkownik o podanym adresie e-mail nie istnieje.");
        }
        userDTO.ifPresent(u -> {
            List<OrderDTO> orders = orderService.findAllOrdersByUserId(u.id());
            model.addAttribute("orders", orders);
        });

        return "user_orders"; // Strona z zamóupdateProfileAndAddresswieniami użytkownika
    }

    @PostMapping("/edit")
    public String updateProfile(@ModelAttribute UserDTO userDTO,
                                @ModelAttribute AddressDTO addressDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Long userId = userService.findUserByEmail(email)
                .map(UserDTO::id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        userService.updateUserProfile(userId, userDTO); // Używamy nowej metody dedykowanej dla użytkownika
        // Obsługa adresu może wymagać osobnej logiki, zależnie od struktury aplikacji
        return "redirect:/account/profile";
    }

}