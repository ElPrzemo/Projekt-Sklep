package com.example.projektsklep.controller;

import com.example.projektsklep.exception.BasketException;
import com.example.projektsklep.model.dto.OrderDTO;
import com.example.projektsklep.service.BasketService;
import com.example.projektsklep.service.OrderService;
import com.example.projektsklep.utils.Basket;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

@Controller
@RequestMapping("/basket")
public class BasketController {

    private final BasketService basketService;
    private final OrderService orderService;

    public BasketController(BasketService basketService, OrderService orderService) {
        this.basketService = basketService;
        this.orderService = orderService;
    }

    @GetMapping
    public String viewBasket(Model model) {
        Basket basket = basketService.getCurrentBasket(); // Pobierz aktualny koszyk
        model.addAttribute("basket", basket);
        return "basket_view"; // nazwa pliku HTML Thymeleaf
    }

    @GetMapping("/checkout")
    public String showCheckoutForm(Model model) {
        OrderDTO orderDTO = basketService.createInitialOrderDTO(); // Załóżmy, że ta metoda istnieje i przygotowuje DTO.
        model.addAttribute("order", orderDTO);
        return "order_checkout_form";
    }

    @PostMapping("/checkout")
    public String processCheckout(@Valid @ModelAttribute("order") OrderDTO orderDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "order_checkout_form";
        }

        // Tutaj możesz dodać logikę przekazującą aktualny stan koszyka do DTO, jeśli jest to wymagane.
        // Przykładowo, możemy założyć, że stan koszyka jest już zawarty w `orderDTO`.

        OrderDTO savedOrder = orderService.saveOrderDTO(orderDTO); // Zakładając, że metoda zapisuje zamówienie i zwraca zapisane DTO.

        if (savedOrder != null) {
            basketService.clear(); // Oczyść koszyk po pomyślnym złożeniu zamówienia.
            return "redirect:/order_succes"; // Przekieruj do strony sukcesu.
        } else {
            model.addAttribute("error", "Nie udało się złożyć zamówienia.");
            return "order_checkout_form";
        }
    }


    @PostMapping("/update/{productId}")
    public String updateProductQuantity(@PathVariable Long productId, @RequestParam("quantity") int quantity) {
        basketService.updateProductQuantity(productId, quantity);
        return "redirect:/basket";
    }

    @PostMapping("/remove/{productId}")
    public String removeProductFromBasket(@PathVariable Long productId) {
        basketService.removeProduct(productId);
        return "redirect:/basket";
    }
}