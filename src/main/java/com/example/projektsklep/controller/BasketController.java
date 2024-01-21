package com.example.projektsklep.controller;//package com.example.projectshopping.controller;
//
//import com.example.projectshopping.mapper.OrderMapper;
//import com.example.projectshopping.model.dto.OrderDTO;
//import com.example.projectshopping.model.entities.order.Order;
//import com.example.projectshopping.service.BasketService;
//import com.example.projectshopping.service.OrderService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//@RequestMapping("/basket")
//public class BasketController {
//
//    // Wstrzyknięcie zależności BasketService i OrderService
//    private final BasketService basketService;
//    private final OrderService orderService;
//
//
//    public BasketController(BasketService basketService, OrderService orderService) {
//        this.basketService = basketService;
//        this.orderService = orderService;
//    }
//
//    @GetMapping("/checkout")
//    public String checkoutForm(Model model) {
//        model.addAttribute("order", new OrderDTO()); // Używamy DTO zamiast encji
//        return "order_checkout_form";
//    }
//
//    @PostMapping("/checkout")
//    public String processCheckout(@ModelAttribute OrderDTO orderDTO) {
//        Order order = OrderMapper.toEntity(orderDTO);
//        orderService.saveOrder(order); // Może być konieczna dodatkowa logika do obsługi LineOfOrderDTO
//        basketService.clear();
//        return "redirect:/orders/success";
//    }
//}