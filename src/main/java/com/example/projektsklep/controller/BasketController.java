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
            return "checkoutForm";
        }

        // Here, you would include any logic needed to handle the shippingAddress
        // For example, if the form allows for entering an optional new shipping address:
        if (orderDTO.shippingAddress() != null) {
            // Process the shipping address as needed
        }

        orderService.saveOrderDTO(orderDTO);
        return "redirect:/orderSuccess";
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