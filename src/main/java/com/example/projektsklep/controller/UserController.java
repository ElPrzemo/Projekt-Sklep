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

import javax.validation.Valid;
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
        UserDTO userDTO = userService.createEmptyUserDTO();
        model.addAttribute("userDTO", userDTO);
        return "user_register";
    }

    @PostMapping("/new")
    public String registerUser(@Valid @ModelAttribute("userDTO") UserDTO userDTO,
                               BindingResult result, Model model,
                               @RequestParam("roleType") String roleTypeStr) {
        if (result.hasErrors()) {
            return "user_register";
        }

        try {
            AdminOrUser roleType = AdminOrUser.valueOf(roleTypeStr.toUpperCase());
            userService.registerNewUser(userDTO, roleType);
            return "redirect:/users/registrationSuccess";
        } catch (Exception e) {
            model.addAttribute("registrationError", "Nie udało się zarejestrować użytkownika: " + e.getMessage());
            return "user_register";
        }
    }

    @GetMapping("/registrationSuccess")
    public String registrationSuccess() {
        return "registration_success";
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