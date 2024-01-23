package com.example.projektsklep.controller;



import com.example.projektsklep.model.dto.OrderDTO;
import com.example.projektsklep.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String listOrders(Model model) {
        List<OrderDTO> orderDTOs = orderService.findAllOrderDTOs();
        model.addAttribute("orders", orderDTOs);
        return "order_list";
    }

    @GetMapping("/{orderId}")
    public String orderDetails(@PathVariable Long orderId, Model model) {
        OrderDTO orderDTO = orderService.findOrderDTOById(orderId);
        model.addAttribute("order", orderDTO);
        return "order_details";
    }

    @GetMapping("/edit/{orderId}")
    public String editOrderForm(@PathVariable Long orderId, Model model) {
        OrderDTO orderDTO = orderService.findOrderDTOById(orderId);
        model.addAttribute("orderDTO", orderDTO);
        return "order_edit_form";
    }

    @PostMapping("/edit/{orderId}")
    public String updateOrder(@PathVariable Long orderId, @ModelAttribute OrderDTO orderDTO) {
        orderService.updateOrderDTO(orderId, orderDTO);
        return "redirect:/orders";
    }
    @GetMapping("/user/{userId}")
    public String listOrdersByUser(@PathVariable long userId, Model model) {
        List<OrderDTO> orders = orderService.findAllOrdersByUserId(userId);
        model.addAttribute("orders", orders);
        return "orders_by_user";
    }

}

