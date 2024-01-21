package com.example.projektsklep.service;


import com.example.projektsklep.model.entities.product.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
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
        return products;
    }

    public void clear() {
        products.clear();
    }


}