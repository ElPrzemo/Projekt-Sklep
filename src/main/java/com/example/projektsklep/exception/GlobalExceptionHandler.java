package com.example.projektsklep.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public String handleProductNotFound(Model model) {
        // ... obsłuż błąd
        return null;
    }
    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(Model model) {
        model.addAttribute("error", "Użytkownik nie znaleziony");
        return "user_not_found";
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public String handleOrderNotFound(Model model) {
        model.addAttribute("error", "Zamówienie nie znalezione");
        return "order_not_found";
    }

    @ExceptionHandler(AddressUpdateException.class)
    public String handleAddressUpdateError(Model model) {
        model.addAttribute("error", "Wystąpił błąd podczas aktualizacji adresu");
        return "update_profile_error";
    }

}