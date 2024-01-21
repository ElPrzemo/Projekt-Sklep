package com.example.projektsklep.utils;


import com.example.projektsklep.model.entities.order.LineOfOrder;
import com.example.projektsklep.model.entities.product.Product;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Data

@Component
@SessionScope
public class Basket {

    private List<LineOfOrder> lineOfOrders;

    // Konstruktor domyślny
    public Basket() {
        this.lineOfOrders = new ArrayList<>();
    }

    // Dodawanie produktu do koszyka
    public void addProduct(Product product, int quantity) {
        LineOfOrder lineOfOrder = findLineByProduct(product);
        if (lineOfOrder == null) {
            lineOfOrders.add(new LineOfOrder(product, quantity));
        } else {
            lineOfOrder.setQuantity(lineOfOrder.getQuantity() + quantity);
        }
    }

    // Usuwanie produktu z koszyka
    public void removeProduct(Product product) {
        LineOfOrder lineOfOrder = findLineByProduct(product);
        if (lineOfOrder != null) {
            if (lineOfOrder.getQuantity() > 1) {
                lineOfOrder.setQuantity(lineOfOrder.getQuantity() - 1);
            } else {
                lineOfOrders.remove(lineOfOrder);
            }
        }
    }

    // Wyszukiwanie linii zamówienia na podstawie produktu
    private LineOfOrder findLineByProduct(Product product) {
        return lineOfOrders.stream()
                .filter(line -> line.getProduct().equals(product))
                .findFirst()
                .orElse(null);
    }

    // Pobieranie listy linii zamówień
    public List<LineOfOrder> getLineOfOrders() {
        return lineOfOrders;
    }

    // Czyszczenie koszyka
    public void clear() {
        lineOfOrders.clear();
    }
}