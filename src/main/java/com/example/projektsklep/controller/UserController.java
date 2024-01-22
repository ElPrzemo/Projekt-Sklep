package com.example.projektsklep.controller;


import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.dto.UserDTO;
import com.example.projektsklep.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {



    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(Model model) {
        List<UserDTO> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users_list"; // Zmieniona nazwa na "user_list"
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<UserDTO> userDTO = userService.findUserById(id);
        userDTO.ifPresent(dto -> model.addAttribute("userDTO", dto));
        return userDTO.isPresent() ? "user_edit" : "redirect:/users"; // Zmieniona nazwa na "user_edit"
    }



    @GetMapping("/new")
    public String showNewUserForm(Model model) {
        model.addAttribute("userDTO", UserDTO.builder()
                .email("")
                .firstName("")
                .lastName("")
                .address(new AddressDTO(null, "", "", "", "")) // Pusty adres
                .build());
        return "user_register";
    }
    @PostMapping("/new")
    public String createUser(@ModelAttribute UserDTO userDTO) {
        userService.saveUser(userDTO);
        return "redirect:/user_list";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/users";
    }

    @GetMapping("/search/email")
    public String findUserByEmail(@RequestParam String email, Model model) {
        Optional<UserDTO> userDTO = userService.findUserByEmail(email);
        userDTO.ifPresent(dto -> model.addAttribute("user", dto));
        return userDTO.isPresent() ? "user_details" : "redirect:/users"; // Zmieniona nazwa na "user_details"
    }


    @GetMapping("/search/name")
    public String findUsersByName(@RequestParam String name, Model model) {
        List<UserDTO> users = userService.findUsersByName(name);
        model.addAttribute("users", users);
        return "users_list"; // Załóżmy, że "users_list" to widok listy użytkowników
    }

}