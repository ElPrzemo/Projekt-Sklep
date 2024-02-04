package com.example.projektsklep.model.entities.order;

import com.example.projektsklep.model.entities.adress.Address;
import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.notification.Observer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    @Test
    public void testOrderConstructor_GivenValidData_ShouldCreateOrder() {
        // Given
        User user = new User("user@example.com", "password", "avatar.png", "John", "Doe");
        Address address = new Address("Aleje Ujazdowskie 13", "Warszawa", "00-587", "Polska");
        List<Product> products = Arrays.asList(new Product(), new Product());

        // When
        Order order = new Order(user, address, products);

        // Then
        Assertions.assertEquals(user, order.getUser());
        Assertions.assertEquals(address, order.getAddress());
        Assertions.assertEquals(products, order.getProducts());
    }


    @Test
    public void testCalculateTotalPrice_WithProducts_ShouldCalculateCorrectTotal() {
        // Given
        Order order = new Order();
        List<LineOfOrder> lineOfOrders = new ArrayList<>();
        lineOfOrders.add(new LineOfOrder(new Product(10), 2, BigDecimal.TEN));
        lineOfOrders.add(new LineOfOrder(new Product(5), 3, BigDecimal.FIVE));
        order.setListOfOrders(lineOfOrders);

        // When
        order.calculateTotalPrice();

        // Then
        assertEquals(BigDecimal.valueOf(35), order.getTotalPrice());
    }
    @Test
    public void testCalculateTotalPrice_WithProducts_ShouldCalculateCorrectTotal() {
        // Given
        Order order = new Order();
        List<LineOfOrder> lineOfOrders = new ArrayList<>();
        lineOfOrders.add(new LineOfOrder(new Product(10), 2, BigDecimal.TEN));
        lineOfOrders.add(new LineOfOrder(new Product(5), 3, BigDecimal.FIVE));
        order.setListOfOrders(lineOfOrders);

        // When
        order.calculateTotalPrice();

        // Then
        assertEquals(BigDecimal.valueOf(35), order.getTotalPrice());
    }



    @Test
    public void testSetUser_GivenOrderAndUser_ShouldSetUser() {
        // Given
        User user = new User("user@example.com", "password", "avatar.png", "John", "Doe");
        Order order = new Order();

        // When
        order.setUser(user);

        // Then
        Assertions.assertEquals(user, order.getUser());
    }

}