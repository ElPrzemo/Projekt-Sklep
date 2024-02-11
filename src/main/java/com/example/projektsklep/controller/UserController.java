package com.example.projektsklep.controller;


import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.dto.UserDTO;
import com.example.projektsklep.model.enums.AdminOrUser;
import com.example.projektsklep.service.UserService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;


@ControllerAdvice
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(Model model, @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> userPage = userService.findAllUsers(pageable);
        model.addAttribute("userPage", userPage);
        return "user_list";
    }


    @GetMapping("/new")
    public String showNewUserForm(Model model) {
        // Tworzenie pustego lub domyślnie wypełnionego AddressDTO
        AddressDTO addressDTO = new AddressDTO(0L, "", "", "", "");

        // Inicjalizacja UserDTO z pustym AddressDTO
        UserDTO userDTO = new UserDTO(0L, "", "", "", "", "", addressDTO, new HashSet<>());

        model.addAttribute("userDTO", userDTO);
        return "user_register";
    }
    @PostMapping("/new")
    public String registerUser(@ModelAttribute("userDTO") UserDTO userDTO, BindingResult result, Model model, @RequestParam("roleType") String roleTypeStr) {
        if (result.hasErrors()) {
            return "user_register";
        }

        // Konwersja roleTypeStr na enum AdminOrUser
        AdminOrUser roleType = AdminOrUser.valueOf(roleTypeStr.toUpperCase());

        try {
            // Zakładam, że metoda saveUser wymaga przekazania enuma roleType jako argumentu
            userService.saveUser(userDTO, userDTO.address(), roleType);
        } catch (Exception e) {
            model.addAttribute("registrationError", "Nie udało się zarejestrować użytkownika: " + e.getMessage());
            return "user_register";
        }

        return "redirect:/users/registration-success";
    }

    @GetMapping("/search")
    public String searchUsersByLastName(@RequestParam String lastName, Model model) {
        List<UserDTO> users = userService.findUsersByLastName(lastName);
        model.addAttribute("users", users);
        return "admin_user_list"; // Upewnij się, że "admin_user_list" to nazwa Twojego szablonu Thymeleaf
    }

    @PostMapping("/search")
    public String searchUsersByName(@RequestParam String name, Model model) {
        List<UserDTO> users = userService.findUsersByLastName(name);
        model.addAttribute("users", users); // Dopasuj klucz do tego, co jest używane w Thymeleaf
        return "admin_user_list"; // Nazwa widoku zawierającego listę użytkowników
    }

    // Tutaj można dodać inne metody potrzebne dla kontrolera
}