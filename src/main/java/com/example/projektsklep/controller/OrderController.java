package com.example.projektsklep.controller;



import com.example.projektsklep.exception.OrderNotFoundException;
import com.example.projektsklep.exception.OrderRetrievalException;
import com.example.projektsklep.exception.OrderUpdateException;
import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.dto.OrderDTO;
import com.example.projektsklep.model.enums.OrderStatus;
import com.example.projektsklep.service.BasketService;
import com.example.projektsklep.service.OrderService;
import com.example.projektsklep.utils.AddressDTOInitializer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final BasketService basketService;

    public OrderController(OrderService orderService, BasketService basketService) {
        this.orderService = orderService;
        this.basketService = basketService;
    }

    @GetMapping
    public String listOrders(Model model,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderDTO> orderPage = orderService.findAllOrders(pageable);
        model.addAttribute("orderPage", orderPage);

        // Add pagination navigation elements to the model
        model.addAttribute("hasPrevious", orderPage.hasPrevious());
        model.addAttribute("firstPage", 0);
        model.addAttribute("hasNext", orderPage.hasNext());
        model.addAttribute("lastPage", orderPage.getTotalPages() - 1);
        model.addAttribute("nextPage", Math.max(orderPage.getNumber() + 1, 0));
        model.addAttribute("previousPage", Math.max(orderPage.getNumber() - 1, 0));

        return "order_list";
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @GetMapping("/{orderId}")
    public String orderDetails(@PathVariable Long orderId, Model model) {
        try {
            OrderDTO orderDTO = orderService.findOrderDTOById(orderId);
            model.addAttribute("order", orderDTO);
            return "order_details";
        } catch (OrderNotFoundException e) {
            // Obsłuż błąd "Zamówienie nie znalezione"
            model.addAttribute("error", "Zamówienie nie znalezione");
            return "error"; // Lub przekieruj do strony błędu
        }
    }

    @GetMapping("/edit/{orderId}")
    public String showEditOrderForm(@PathVariable Long orderId, Model model) {
        try {
            OrderDTO orderDTO = orderService.findOrderDTOById(orderId);
            model.addAttribute("order", orderDTO);
            // Dodanie listy dostępnych statusów zamówień
            model.addAttribute("statuses", OrderStatus.values());
            return "order_edit_form";
        } catch (OrderNotFoundException e) {
            model.addAttribute("error", "Zamówienie nie znalezione");
            return "error";
        }
    }

    @PostMapping("/edit/{orderId}")
    public String updateOrderStatus(@PathVariable Long orderId, @ModelAttribute("order") OrderDTO orderDTO, Model model) {
        try {
            orderService.updateOrderStatus(orderId, orderDTO.orderStatus());
            return "redirect:/orders";
        } catch (OrderNotFoundException e) {
            model.addAttribute("error", "Zamówienie nie znalezione");
            return "error";
        } catch (Exception e) {
            model.addAttribute("error", "Błąd podczas aktualizacji statusu zamówienia");
            return "order_edit_form";
        }
    }

    @GetMapping("/user/{userId}")
    public String listOrdersByUser(@PathVariable long userId, Model model) {
        try {
            List<OrderDTO> orders = orderService.findAllOrdersByUserId(userId);
            model.addAttribute("orders", orders);
            return "orders_by_user";
        } catch (OrderRetrievalException e) {
            // Obsłuż błąd pobierania zamówień
            model.addAttribute("error", "Błąd podczas pobierania zamówień");
            return "error"; // Lub przekieruj do strony błędu
        }
    }

    @PostMapping("/create")
    public String createOrderFromBasket(RedirectAttributes redirectAttributes) {
        // Utwórz OrderDTO na podstawie aktualnego stanu koszyka
        OrderDTO orderDTO = basketService.createOrderDTOFromBasket(basketService.createInitialOrderDTO());

        // Utwórz zamówienie i wyczyść koszyk
        basketService.placeOrder(orderDTO);
        basketService.clear();

        redirectAttributes.addFlashAttribute("success", "Zamówienie zostało złożone.");
        return "redirect:/orders/confirmation";
    }

}




