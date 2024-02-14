package com.example.projektsklep.controller;



import com.example.projektsklep.exception.OrderNotFoundException;
import com.example.projektsklep.exception.OrderRetrievalException;
import com.example.projektsklep.exception.OrderUpdateException;
import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.dto.OrderDTO;
import com.example.projektsklep.model.dto.UserDTO;
import com.example.projektsklep.model.enums.OrderStatus;
import com.example.projektsklep.service.BasketService;
import com.example.projektsklep.service.OrderService;
import com.example.projektsklep.service.UserService;
import com.example.projektsklep.utils.AddressDTOInitializer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final BasketService basketService;
    private final UserService userService;

    public OrderController(OrderService orderService, BasketService basketService, UserService userService) {
        this.orderService = orderService;
        this.basketService = basketService;
        this.userService = userService;
    }

    @GetMapping
    public String listOrders(Model model,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDTO> orderPage = orderService.findAllOrders(pageable);
        model.addAttribute("orderPage", orderPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);

        return "order_list";
    }

    @GetMapping("/{orderId}")
    public String orderDetails(@PathVariable Long orderId, Model model) {
        Optional<OrderDTO> orderDTO = orderService.findOrderDTOById(orderId);
        if (orderDTO.isPresent()) {
            model.addAttribute("order", orderDTO.get());
            return "order_details";
        } else {
            return "redirect:/error";
        }
    }

    @GetMapping("/edit/{orderId}")
    public String showEditOrderForm(@PathVariable Long orderId, Model model) {
        Optional<OrderDTO> orderDTO = orderService.findOrderDTOById(orderId);
        if (orderDTO.isPresent()) {
            model.addAttribute("order", orderDTO.get());
            model.addAttribute("statuses", OrderStatus.values());
            return "order_edit_form";
        } else {
            return "redirect:/error";
        }
    }

    @PostMapping("/edit/{orderId}")
    public String updateOrderStatus(@PathVariable Long orderId, @ModelAttribute("order") OrderDTO orderDTO, RedirectAttributes redirectAttributes) {
        boolean updated = orderService.updateOrderStatus(orderId, OrderStatus.valueOf(orderDTO.orderStatus()));
        if (updated) {
            redirectAttributes.addFlashAttribute("success", "Status zamówienia został zaktualizowany.");
            return "redirect:/orders/" + orderId;
        } else {
            redirectAttributes.addFlashAttribute("error", "Nie udało się zaktualizować statusu zamówienia.");
            return "redirect:/orders/edit/" + orderId;
        }
    }

    @PostMapping("/create")
    public String createOrderFromBasket(RedirectAttributes redirectAttributes, Authentication authentication) {
        String email = authentication.getName(); // Pobierz nazwę użytkownika (email)
        UserDTO userDTO = userService.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        OrderDTO orderDTO = basketService.createOrderDTOFromBasket(userDTO.id());
        orderService.saveOrderDTO(orderDTO);
        basketService.clear();

        redirectAttributes.addFlashAttribute("success", "Zamówienie zostało złożone pomyślnie.");
        return "redirect:/orders";
    }

}




