//package com.example.projektsklep.controller;
//
//import com.example.projektsklep.exception.BasketException;
//import com.example.projektsklep.model.dto.AddressDTO;
//import com.example.projektsklep.model.dto.OrderDTO;
//import com.example.projektsklep.model.dto.UserDTO;
//import com.example.projektsklep.service.BasketService;
//import com.example.projektsklep.service.OrderService;
//import com.example.projektsklep.service.UserService;
//import com.example.projektsklep.utils.Basket;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import javax.validation.Valid;
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.ArrayList;
//
//@Controller
//@RequestMapping("/basket")
//public class BasketController {
//
//    private final BasketService basketService;
//    private final OrderService orderService;
//
//    private final UserService userService;
//
//    public BasketController(BasketService basketService, OrderService orderService, UserService userService) {
//        this.basketService = basketService;
//        this.orderService = orderService;
//        this.userService = userService;
//    }
//
//
//
//
//
//    @PostMapping("/add/{productId}")
//    public String addToBasket(@PathVariable Long productId, @RequestParam("quantity") int quantity, RedirectAttributes redirectAttributes) {
//        try {
//            basketService.addProductToBasket(productId, quantity);
//            redirectAttributes.addFlashAttribute("successMessage", "Produkt dodany do koszyka!");
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Nie udało się dodać produktu do koszyka.");
//        }
//        return "redirect:/basket";
//    }
//
//
//
//
//}