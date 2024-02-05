package com.example.projektsklep.model.entities.order;

import com.example.projektsklep.model.entities.adress.Address;
import com.example.projektsklep.model.entities.product.Author;
import com.example.projektsklep.model.entities.product.Category;
import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.enums.ProductType;
import com.example.projektsklep.model.notification.Observer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        Order order = new Order();

        // Then
        Assertions.assertEquals(user, order.getUser());
        Assertions.assertEquals(address, order.getAddress());
        Assertions.assertEquals(products, order.getProducts());
    }


//    @Test
//    public void testCalculateTotalPrice_WithProducts_ShouldCalculateCorrectTotal() {
//        // Given
//        User user = new User("user@example.com", "password", "avatar.png", "John", "Doe"); // Create a User object
//        Address address = new Address("Aleje Ujazdowskie 13", "Warszawa", "00-587", "Polska"); // Create an Address object
//        List<Product> products = Arrays.asList(new Product(), new Product()); // Create a list of Products
//
//        // Utwórz produkty przy użyciu odpowiedniego konstruktora (zgodnie z Twoją klasą Product)
//        Product product1 = new Product("Produkt 1", "Opis produktu 1", "miniature1.jpg", BigDecimal.TEN, ProductType.COOKING_EQUIPMENT, true, LocalDate.now(), new Author(), new Category());
//        Product product2 = new Product("Produkt 2", "Opis produktu 2", "miniature2.jpg", BigDecimal.FIVE, ProductType.KITCHEN_EQUIPMENT, true, LocalDate.now(), new Author(), new Category());
//
//        List<Product> products = Arrays.asList(product1, product2);
//
//        // Zakładając konstruktor przyjmujący User, Address i List<Product>
//        Order order = new Order();
//
//        List<LineOfOrder> lineOfOrders = new ArrayList<>();
//        lineOfOrders.add(new LineOfOrder(product1, 2, BigDecimal.TEN)); // Użyj rzeczywistych produktów
//        lineOfOrders.add(new LineOfOrder(product2, 3, BigDecimal.TEN)); // Użyj rzeczywistych produktów
//        order.setListOfOrders(lineOfOrders);
//
//        // Obliczanie
//        order.calculateTotalPrice();
//
//        // Weryfikacja
//        assertEquals(BigDecimal.valueOf(35), order.getTotalPrice());
//    }



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