package com.example.projektsklep.controller;

import com.example.projektsklep.exception.DataAccessException;
import com.example.projektsklep.exception.InvalidUserDataException;
import com.example.projektsklep.exception.UserAlreadyExistsException;
import com.example.projektsklep.exception.UserNotFoundException;
import com.example.projektsklep.model.dto.AddressDTO;

import com.example.projektsklep.model.dto.UserDTO;
import com.example.projektsklep.model.enums.AdminOrUser;
import com.example.projektsklep.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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


//    @GetMapping("/new")
//    public String showNewUserForm(Model model) {
//        // Utworzenie pustego obiektu AddressDTO
//        AddressDTO addressDTO = new AddressDTO(null, "", "", "", "");
//
//        // Przygotowanie pustego zestawu ról
//        Set<RoleDTO> roles = Collections.emptySet();
//
//        // Utworzenie nowego obiektu UserDTO z null jako ID i null jako phoneNumber
//        UserDTO userDTO = new UserDTO(null, "", "", "", null, "", addressDTO, roles);
//
//        model.addAttribute("userDTO", userDTO);
//        model.addAttribute("addressDTO", addressDTO);
//        model.addAttribute("roles", AdminOrUser.values());
//        return "user_register";
//    }
//    @PostMapping("/new")
//    public String registerUser(@ModelAttribute UserDTO userDTO, Model model) {
//        // Pobieramy AddressDTO i rolę z modelu
//        AddressDTO addressDTO = (AddressDTO) model.getAttribute("addressDTO");
//        AdminOrUser role = (AdminOrUser) model.getAttribute("role");
//        try {
//            userService.saveUser(userDTO, addressDTO, role);
//        } catch (UserAlreadyExistsException e) {
//            // Dodanie obsługi użytkownika o tym samym emailu
//            model.addAttribute("error", e.getMessage());
//            return "user_register";
//        } catch (InvalidUserDataException e) {
//            // Dodanie obsługi błędnych danych użytkownika
//            model.addAttribute("error", e.getMessage());
//            return "user_register";
//        } catch (DataAccessException e) {
//            // Dodanie obsługi błędów dostępu do bazy danych
//            model.addAttribute("error", "Wystąpił błąd podczas rejestracji");
//            return "user_register";
//        }
//
//        // Używamy przekazanych obiektów userDTO, addressDTO i roli
//        userService.saveUser(userDTO, addressDTO, role);
//
//        return "redirect:/users/registration-success";
//    }

    @GetMapping("/registration-success")
    public String registrationSuccess(Model model) {
        model.addAttribute("message", "Użytkownik zarejestrowany pomyślnie");
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