package com.example.projektsklep.controller;

import com.example.projektsklep.exception.DataAccessException;
import com.example.projektsklep.exception.InvalidUserDataException;
import com.example.projektsklep.exception.UserAlreadyExistsException;
import com.example.projektsklep.exception.UserNotFoundException;
import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.dto.RoleDTO;
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

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<UserDTO> userDTO = userService.findUserById(id);if (!userDTO.isPresent()) {
            throw new UserNotFoundException("Użytkownik o podanym ID nie istnieje");
        }
        userDTO.ifPresent(dto -> model.addAttribute("userDTO", dto));
        return userDTO.isPresent() ? "user_edit" : "redirect:/users";
    }

    @PostMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, @ModelAttribute UserDTO userDTO,Model model) {
        try {
            userService.updateUserProfileOrAdmin(id, userDTO, false);
        } catch (InvalidUserDataException e) {
            // Dodanie obsługi błędnych danych użytkownika
            model.addAttribute("error", e.getMessage());
            return "user_edit";
        } catch (DataAccessException e) {
            // Dodanie obsługi błędów dostępu do bazy danych
            model.addAttribute("error", "Wystąpił błąd podczas aktualizacji profilu");
            return "user_edit";
        } // false oznacza, że nie jest to admin
        return "redirect:/users";
    }

    @GetMapping("/new")
    public String showNewUserForm(Model model) {
        // Tworzenie obiektu AddressDTO z dostępnym konstruktorem
        AddressDTO addressDTO = new AddressDTO(1L, "Ulica", "Miasto", "Kod pocztowy", "Kraj");

        // Przygotowanie pustego zestawu ról
        Set<RoleDTO> roles = Collections.emptySet(); // Lub null, jeśli Twoja logika to akceptuje

        // Tworzenie obiektu UserDTO z dostępnym konstruktorem, uwzględniając puste rôle
        UserDTO userDTO = new UserDTO(1L, "Imię", "Nazwisko", "Email", 123, "Hasło", addressDTO, roles);

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("addressDTO", addressDTO);
        model.addAttribute("roles", AdminOrUser.values()); // Możesz potrzebować listy RoleDTO zamiast AdminOrUser, zależnie od Twojej implementacji
        return "user_register";
    }
    @PostMapping("/new")
    public String registerUser(@ModelAttribute UserDTO userDTO, Model model) {
        // Pobieramy AddressDTO i rolę z modelu
        AddressDTO addressDTO = (AddressDTO) model.getAttribute("addressDTO");
        AdminOrUser role = (AdminOrUser) model.getAttribute("role");
        try {
            userService.saveUser(userDTO, addressDTO, role);
        } catch (UserAlreadyExistsException e) {
            // Dodanie obsługi użytkownika o tym samym emailu
            model.addAttribute("error", e.getMessage());
            return "user_register";
        } catch (InvalidUserDataException e) {
            // Dodanie obsługi błędnych danych użytkownika
            model.addAttribute("error", e.getMessage());
            return "user_register";
        } catch (DataAccessException e) {
            // Dodanie obsługi błędów dostępu do bazy danych
            model.addAttribute("error", "Wystąpił błąd podczas rejestracji");
            return "user_register";
        }

        // Używamy przekazanych obiektów userDTO, addressDTO i roli
        userService.saveUser(userDTO, addressDTO, role);

        return "redirect:/users/registration-success";
    }

    @GetMapping("/registration-success")
    public String registrationSuccess(Model model) {
        model.addAttribute("message", "Użytkownik zarejestrowany pomyślnie");
        return "registration_success";
    }



//    @GetMapping("/search/email")
//    public String findUserByEmail(@RequestParam String email, Model model) {
//        Optional<UserDTO> userDTO = userService.findUserByEmail(email);
//        userDTO.ifPresent(dto -> model.addAttribute("user", dto));
//        return userDTO.isPresent() ? "user_details" : "redirect:/users";
//    }



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