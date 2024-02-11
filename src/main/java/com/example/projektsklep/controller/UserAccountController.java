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

    @GetMapping("/edit")
    public String showEditForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Pobierz adres e-mail zalogowanego użytkownika

        UserDTO userDTO = userService.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika."));

        // Jeśli użytkownik nie ma przypisanego adresu, tworzymy pusty obiekt AddressDTO
        if (userDTO.address() == null) {
            userDTO = new UserDTO(userDTO.id(), userDTO.email(), userDTO.firstName(), userDTO.lastName()
                   , userDTO.password(),
                    "", new AddressDTO(null, "", "", "", ""), userDTO.roles());
        }

        model.addAttribute("userDTO", userDTO);
        return "user_edit"; // Nazwa Twojego widoku Thymeleaf
    }

    @PostMapping("/edit")
    public String updateProfileAndAddress(@Valid @ModelAttribute("userDTO") UserDTO userDTO,
                                          BindingResult userResult,
                                          @Valid @ModelAttribute("addressDTO") AddressDTO addressDTO,
                                          BindingResult addressResult,
                                          Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Pobierz adres e-mail zalogowanego użytkownika

        if (userResult.hasErrors() || addressResult.hasErrors()) {
            model.addAttribute("userDTO", userDTO); // Przekazanie ponownie użytkownika do widoku, jeśli wystąpią błędy
            model.addAttribute("addressDTO", addressDTO); // Przekazanie ponownie adresu do widoku, jeśli wystąpią błędy
            return "user_edit"; // Nazwa widoku formularza edycji, który ma być wyświetlony ponownie w przypadku błędów
        }

        try {
            // Teraz używamy e-maila zalogowanego użytkownika jako identyfikatora
            userService.updateUserProfileAndAddress(email, userDTO, addressDTO);
        } catch (Exception e) {
            model.addAttribute("updateError", "Wystąpił błąd podczas aktualizacji profilu: " + e.getMessage());
            return "user_edit"; // Powrót do formularza edycji z komunikatem o błędzie
        }
        return "redirect:/userPanel"; // Przekierowanie do panelu użytkownika po pomyślnej aktualizacji
    }}