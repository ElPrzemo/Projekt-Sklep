package com.example.projektsklep.controller;

import com.example.projektsklep.model.dto.OrderDTO;
import com.example.projektsklep.service.BasketService;
import com.example.projektsklep.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/checkout")
    public String checkoutForm(Model model) {
        OrderDTO orderDTO = basketService.createInitialOrderDTO();
        model.addAttribute("orderDTO", orderDTO);
        return "order_checkout_form";
    }

    @PostMapping("/checkout")
    public String processCheckout(@ModelAttribute OrderDTO currentOrderDTO) {
        OrderDTO updatedOrderDTO = basketService.createOrderDTOFromBasket(currentOrderDTO);
        orderService.saveOrderDTO(updatedOrderDTO);
        basketService.clear();
        return "redirect:/orders/success";
    }

    @GetMapping
    public String viewBasket(Model model) {
        model.addAttribute("products", basketService.getProducts());
        return "basket_view"; // nazwa pliku HTML Thymeleaf
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