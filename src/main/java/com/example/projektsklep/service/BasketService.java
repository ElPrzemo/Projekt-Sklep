package com.example.projektsklep.service;

import com.example.projektsklep.model.dto.LineOfOrderDTO;
import com.example.projektsklep.model.dto.OrderDTO;
import com.example.projektsklep.model.entities.product.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@SessionScope
public class BasketService {

    private final Map<Long, Integer> products = new HashMap<>();

    public void addProduct(Product product) {
        products.put(product.getId(), products.getOrDefault(product.getId(), 0) + 1);
    }

    public void removeProduct(Product product) {
        products.computeIfPresent(product.getId(), (k, v) -> v > 1 ? v - 1 : null);
    }

    public Map<Long, Integer> getProducts() {
        return new HashMap<>(products);
    }

    public void clear() {
        products.clear();
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
}