package com.example.projektsklep.controller;



import com.example.projektsklep.exception.OrderNotFoundException;
import com.example.projektsklep.exception.OrderRetrievalException;
import com.example.projektsklep.exception.OrderUpdateException;
import com.example.projektsklep.model.dto.OrderDTO;
import com.example.projektsklep.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@ControllerAdvice
@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
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

    @PostMapping("/edit/{orderId}")
    public String updateOrder(@PathVariable Long orderId, @ModelAttribute OrderDTO orderDTO, Model model) {
        try {
            orderService.updateOrderDTO(orderId, orderDTO);
            return "redirect:/orders";
        } catch (OrderUpdateException e) {
            // Obsłuż błąd aktualizacji zamówienia
            model.addAttribute("error", "Błąd podczas aktualizacji zamówienia");
            return "order_edit_form"; // Lub przekieruj do strony błędu
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
}




