package com.example.projektsklep.controller;

import com.example.projektsklep.exception.BasketException;
import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.dto.OrderDTO;
import com.example.projektsklep.model.dto.UserDTO;
import com.example.projektsklep.service.BasketService;
import com.example.projektsklep.service.OrderService;
import com.example.projektsklep.service.UserService;
import com.example.projektsklep.utils.Basket;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

@Controller
@RequestMapping("/basket")
public class BasketController {

    private final BasketService basketService;
    private final OrderService orderService;

    private final UserService userService;

    public BasketController(BasketService basketService, OrderService orderService, UserService userService) {
        this.basketService = basketService;
        this.orderService = orderService;
        this.userService = userService;
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

    @PostMapping("/add/{productId}")
    public String addToBasket(@PathVariable Long productId, @RequestParam("quantity") int quantity, RedirectAttributes redirectAttributes) {
        try {
            basketService.addProductToBasket(productId, quantity);
            redirectAttributes.addFlashAttribute("successMessage", "Produkt dodany do koszyka!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nie udało się dodać produktu do koszyka.");
        }
        return "redirect:/basket";
    }

    @PostMapping("/checkout")
    public String processCheckout(@Valid @ModelAttribute("order") OrderDTO orderDTO, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "checkoutForm";
        }

        // Pobranie ID zalogowanego użytkownika (zakładam użycie Spring Security)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDTO userDTO = userService.findUserByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AddressDTO shippingAddress = orderDTO.shippingAddress(); // Domyslny adres z OrderDTO

        // Sprawdzenie, czy zaznaczono opcję nowego adresu dostawy
        if (request.getParameter("differentAddress") != null) {
            // Utworzenie nowego AddressDTO z danych formularza
            shippingAddress = new AddressDTO(
                    null,
                    request.getParameter("street"),
                    request.getParameter("city"),
                    request.getParameter("postalCode"),
                    request.getParameter("country")
            );
        }

        // Utworzenie nowego OrderDTO z uwzględnieniem nowego adresu dostawy i ID użytkownika
        OrderDTO finalOrderDTO = OrderDTO.builder()
                .id(orderDTO.id()) // Można użyć null jeśli to nowe zamówienie
                .userId(userDTO.id())
                .orderStatus(orderDTO.orderStatus())
                .dateCreated(orderDTO.dateCreated())
                .sentAt(orderDTO.sentAt())
                .totalPrice(orderDTO.totalPrice())
                .lineOfOrders(orderDTO.lineOfOrders())
                .shippingAddress(shippingAddress)
                .build();

        // Zapisanie zamówienia z nowo utworzonym OrderDTO
        orderService.saveOrderDTO(finalOrderDTO);

        return "redirect:/basket/orderSuccess";
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

    @GetMapping("/orderSuccess")
    public String orderSuccess() {
        return "order_success"; //
    }
}