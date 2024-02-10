package com.example.projektsklep.service;

import com.example.projektsklep.model.dto.LineOfOrderDTO;
import com.example.projektsklep.model.dto.OrderDTO;
import com.example.projektsklep.model.dto.UserDTO;
import com.example.projektsklep.model.entities.order.LineOfOrder;
import com.example.projektsklep.model.entities.order.Order;
import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.enums.OrderStatus;
import com.example.projektsklep.model.repository.OrderRepository;
import com.example.projektsklep.utils.Basket;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@SessionScope
public class BasketService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ProductService productService;
    private final Map<Long, Integer> products = new HashMap<>();
    private  Basket basket;

    // Konstruktor z wstrzyknięciem zależności

    public BasketService(OrderRepository orderRepository, UserService userService, ProductService productService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.productService = productService;
        this.basket = new Basket(); // Inicjalizacja koszyka
    }


    public void addProduct(Product product) {
        products.put(product.getId(), products.getOrDefault(product.getId(), 0) + 1);
    }

    public void removeProduct(Long productId) {
        products.computeIfPresent(productId, (k, v) -> v > 1 ? v - 1 : null);
    }

    public Map<Long, Integer> getProducts() {
        return new HashMap<>(products);
    }

    public void clear() {
        products.clear();
    }

    public Basket getCurrentBasket() {
        if (this.basket == null) {
            this.basket = new Basket();
        }
        return this.basket;
    }

    public void updateProductQuantity(Long productId, int quantity) {
        if (products.containsKey(productId)) {
            if (quantity > 0) {
                products.put(productId, quantity);
            } else {
                removeProduct(productId);
            }
        }
    }

    public OrderDTO createInitialOrderDTO() {
        // Tutaj tworzymy początkowe DTO, możemy ustawić domyślne wartości lub puste listy
        return OrderDTO.builder()
                .lineOfOrders(new ArrayList<>())
                // Ustaw pozostałe pola, jeśli są wymagane
                .build();
    }

    public OrderDTO createOrderDTOFromBasket(OrderDTO currentOrderDTO) {
        List<LineOfOrderDTO> lineOfOrders = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : products.entrySet()) {
            // Tu tworzymy LineOfOrderDTO z każdego produktu w koszyku
            lineOfOrders.add(new LineOfOrderDTO(null, entry.getKey(), entry.getValue(), null)); // Uzupełnij odpowiednimi danymi
        }

        // Tworzenie nowej instancji OrderDTO z aktualizowanymi danymi
        return OrderDTO.builder()
                .id(currentOrderDTO.id())
                .userId(currentOrderDTO.userId())
                .orderStatus(currentOrderDTO.orderStatus())
                .dateCreated(currentOrderDTO.dateCreated())
                .sentAt(currentOrderDTO.sentAt())
                .totalPrice(currentOrderDTO.totalPrice())
                .lineOfOrders(lineOfOrders)
                .build();
    }

    public void placeOrder(OrderDTO orderDTO) {
        Order newOrder = new Order();
        newOrder.setOrderStatus(OrderStatus.NEW_ORDER);
        newOrder.setDateCreated(LocalDate.now());

        // Pobieranie UserDTO i konwersja na User
        UserDTO userDTO = userService.findUserById(orderDTO.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        User user = userService.convertToUser(userDTO);
        newOrder.setAccountHolder(user);

        // Przekształcenie linii koszyka na linie zamówienia
        List<LineOfOrder> lineOfOrders = convertBasketToLineOfOrders();
        newOrder.setLineOfOrders(lineOfOrders);

        newOrder.calculateTotalPrice();

        orderRepository.save(newOrder);

        clear();
    }

    private List<LineOfOrder> convertBasketToLineOfOrders() {
        List<LineOfOrder> lineOfOrders = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : products.entrySet()) {
            Product product = productService.findProductById(entry.getKey());
            LineOfOrder lineOfOrder = new LineOfOrder(product, entry.getValue());
            lineOfOrders.add(lineOfOrder);
        }
        return lineOfOrders;
    }

}