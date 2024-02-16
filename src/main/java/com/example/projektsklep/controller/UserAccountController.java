package com.example.projektsklep.controller;

import com.example.projektsklep.exception.UserNotFoundException;
import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.dto.OrderDTO;
import com.example.projektsklep.model.dto.UserDTO;
import com.example.projektsklep.service.BasketService;
import com.example.projektsklep.service.OrderService;
import com.example.projektsklep.service.UserService;
import com.example.projektsklep.utils.Basket;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
@ControllerAdvice
@Controller
@RequestMapping("/account")
public class UserAccountController {

    private final OrderService orderService;
    private final UserService userService;
    private final BasketService basketService;

    public UserAccountController(OrderService orderService, UserService userService, BasketService basketService) {
        this.orderService = orderService;
        this.userService = userService;
        this.basketService = basketService;
    }

    @GetMapping("/panel")
    public String showUserPanel(Model model, Authentication authentication) {
        String email = authentication.getName();
        UserDTO userDTO = userService.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika."));
        model.addAttribute("userDTO", userDTO);

        // Dodajemy logowanie danych użytkownika i adresu
        System.out.println("UserDTO: " + userDTO);
        if(userDTO.address() != null) {
            AddressDTO address = userDTO.address();
            System.out.println("Address: " + address.street() + ", " + address.city() + ", " + address.postalCode() + ", " + address.country());
        } else {
            System.out.println("Address is null");
        }

        return "userPanel";
    }

    @GetMapping("/my_orders")
    public String listUserOrders(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Pobierz adres e-mail zalogowanego użytkownika

        Optional<UserDTO> userDTO = userService.findUserByEmail(email);
        if (!userDTO.isPresent()) {
            throw new UserNotFoundException("Użytkownik o podanym adresie e-mail nie istnieje.");
        }
        userDTO.ifPresent(u -> {
            List<OrderDTO> orders = orderService.findAllOrdersByUserId(u.id());
            model.addAttribute("orders", orders);
        });

        return "user_orders"; // Strona z zamóupdateProfileAndAddresswieniami użytkownika
    }

    @GetMapping("/edit")
    public String showEditForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Pobierz adres e-mail zalogowanego użytkownika

        UserDTO userDTO = userService.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika."));

        // Jeśli użytkownik nie ma przypisanego adresu, tworzymy pusty obiekt AddressDTO
        if (userDTO.address() == null) {
            AddressDTO emptyAddress = new AddressDTO(null, "", "", "", ""); // Pusty obiekt AddressDTO
            userDTO = new UserDTO(userDTO.id(), userDTO.firstName(), userDTO.lastName(), userDTO.email(),
                    userDTO.password(), emptyAddress, userDTO.roles());
        }

        model.addAttribute("userDTO", userDTO);
        return "user_edit";
    }


    @PostMapping("/edit")
    public String updateProfileAndAddress(@Valid @ModelAttribute("userDTO") UserDTO userDTO,
                                          BindingResult result,
                                          Model model, Authentication authentication) {
        if (result.hasErrors()) {
            return "user_edit";
        }

        String email = authentication.getName(); // Pobierz adres e-mail zalogowanego użytkownika
        userService.updateUserProfileAndAddress(email, userDTO); // Zakładając, że ta metoda obsługuje zarówno użytkownika, jak i adres
        return "redirect:/account/panel"; // Przekierowanie do panelu użytkownika po pomyślnej aktualizacji
    }

//BASKET
//BASKET
//BASKET
//BASKET

    @GetMapping("/basket")
    public String viewBasket(Model model) {
        Basket basket = basketService.getCurrentBasket(); // Pobierz aktualny koszyk
        model.addAttribute("basket", basket);
        return "basket_view"; // nazwa pliku HTML Thymeleaf
    }


    @GetMapping("/checkoutBasket")
    public String showCheckoutForm(Model model) {
        OrderDTO orderDTO = basketService.createInitialOrderDTO(); // Załóżmy, że ta metoda istnieje i przygotowuje DTO.
        model.addAttribute("order", orderDTO);
        return "order_checkout_form";
    }

    @PostMapping("/checkoutBasket")
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

        return "redirect:/account/orderSuccess";
    }

    @PostMapping("/removeFromBasket/{productId}")
    public String removeProductFromBasket(@PathVariable Long productId) {
        basketService.removeProduct(productId);
        return "redirect:/basket";
    }

    @PostMapping("/updateProductQuantity/{productId}")
    public String updateProductQuantity(@PathVariable Long productId, @RequestParam("quantity") int quantity) {
        basketService.updateProductQuantity(productId, quantity);
        return "redirect:/basket";
    }


    @GetMapping("/orderSuccess")
    public String orderSuccess() {
        return "order_success"; //
    }


}