package com.example.projektsklep.controller;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public String listUserOrders(Model model, Authentication authentication) {
        String email = authentication.getName();

        // Znajdź ID użytkownika na podstawie e-maila. Zakładamy, że użytkownik zawsze istnieje.
        UserDTO userDTO = userService.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Użytkownik o podanym adresie e-mail nie istnieje."));

        // Pobierz zamówienia dla zalogowanego użytkownika
        List<OrderDTO> orders = orderService.findAllOrdersByUserId(userDTO.id());
        model.addAttribute("orders", orders);

        return "user_orders";
    }
    @GetMapping("/edit")
    public String showEditForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Pobierz adres e-mail zalogowanego użytkownika

        UserDTO userDTO = userService.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika."));

        // Jeśli użytkownik nie ma przypisanego adresu, tworzymy pusty obiekt AddressDTO
        if (userDTO.address() == null) {
            AddressDTO emptyAddress = new AddressDTO(null, "", "", "", ""); // Pusty obiekt AddressDTO
            userDTO = new UserDTO(userDTO.id(), userDTO.firstName(), userDTO.lastName(), userDTO.email(),
                    userDTO.password(), emptyAddress, userDTO.roles());
        }

        model.addAttribute("userDTO", userDTO);
        return "user_edit";
    }


    @PostMapping("/edit")
    public String updateProfileAndAddress(@Valid @ModelAttribute("userDTO") UserDTO userDTO, BindingResult result, Model model, Authentication authentication) {
        if (result.hasErrors()) {
            return "user_edit";
        }
        String email = authentication.getName();
        userService.updateUserProfileAndAddress(email, userDTO);
        return "redirect:/userPanel";
    }

}