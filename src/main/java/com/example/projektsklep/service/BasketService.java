package com.example.projektsklep.service;

import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.dto.LineOfOrderDTO;
import com.example.projektsklep.model.dto.OrderDTO;
import com.example.projektsklep.model.dto.UserDTO;
import com.example.projektsklep.model.entities.order.LineOfOrder;
import com.example.projektsklep.model.entities.order.Order;
import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.enums.OrderStatus;
import com.example.projektsklep.model.repository.BasketRepository;
import com.example.projektsklep.model.repository.OrderRepository;
import com.example.projektsklep.model.repository.ProductRepository;
import com.example.projektsklep.utils.Basket;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
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
    private final ProductRepository productRepository;


    private final Map<Long, Integer> products = new HashMap<>();

    private BasketRepository basketRepository;
    private ProductService productService;
    private  Basket basket;

    // Konstruktor z wstrzyknięciem zależności


    public BasketService(OrderRepository orderRepository, UserService userService, ProductRepository productRepository, BasketRepository basketRepository, ProductService productService, Basket basket) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.productRepository = productRepository;
        this.basketRepository = basketRepository;
        this.productService = productService;
        this.basket = basket;
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
        // Inicjalizacja pustego AddressDTO
        AddressDTO addressDTO = new AddressDTO(null, "", "", "", "");

        return OrderDTO.builder()
                .lineOfOrders(new ArrayList<>())
                .shippingAddress(addressDTO) // Dodanie pustego AddressDTO
                .build();
    }
    public OrderDTO createOrderDTOFromBasket(Long userId) {
        List<LineOfOrderDTO> lineOfOrdersDTO = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (LineOfOrder lineOfOrder : basket.getLineOfOrders()) {
            Product product = lineOfOrder.getProduct();
            LineOfOrderDTO lineOfOrderDTO = new LineOfOrderDTO(
                    lineOfOrder.getId(),
                    product.getId(),
                    lineOfOrder.getQuantity(),
                    product.getPrice());
            lineOfOrdersDTO.add(lineOfOrderDTO);

            // Obliczanie ceny dla każdej linii zamówienia i dodawanie do całkowitej ceny
            BigDecimal linePrice = product.getPrice().multiply(BigDecimal.valueOf(lineOfOrder.getQuantity()));
            totalPrice = totalPrice.add(linePrice);
        }

        // Utwórz OrderDTO z uzyskanymi danymi
        OrderDTO orderDTO = OrderDTO.builder()
                .userId(userId)
                .lineOfOrders(lineOfOrdersDTO)
                .totalPrice(totalPrice)
                // Możesz dodać więcej pól zgodnie z wymaganiami
                .build();

        return orderDTO;
    }

    public void addProductToBasket(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono produktu o ID: " + productId));

        // Tutaj logika dodawania produktu do koszyka.
        // Możesz potrzebować dodatkowych informacji, jak zarządzasz koszykiem (np. sesja użytkownika, model koszyka).

        // Przykład dodawania produktu do modelu koszyka (zakładając, że masz taki model i repozytorium).
        // Musisz dostosować ten kod do swojej implementacji.
        Basket basket = getCurrentUserBasket(); // Załóżmy, że ta metoda pobiera aktualny koszyk użytkownika
        basket.addProduct(product, quantity); // Załóżmy, że istnieje taka metoda w klasie Basket
        basketRepository.save(basket); // Zapisz zmiany w koszyku
    }

    private Basket getCurrentUserBasket() {
        // Implementacja metody zależy od sposobu przechowywania koszyka
        // Może to być sesja użytkownika, baza danych itp.
        return new Basket(); // Przykładowa implementacja, dostosuj do swoich potrzeb
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