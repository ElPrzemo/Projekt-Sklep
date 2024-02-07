package com.example.projektsklep.model.entities.order;

import com.example.projektsklep.model.entities.adress.Address;
import com.example.projektsklep.model.entities.product.Author;
import com.example.projektsklep.model.entities.product.Category;
import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.enums.OrderStatus;
import com.example.projektsklep.model.enums.ProductType;
import com.example.projektsklep.model.notification.Observer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class OrderTest {

    @Mock
    private Observer mockObserver;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testNotifyObservers_ShouldNotifyRegisteredObservers() {
        // Given
        Order order = new Order();
        order.registerObserver(mockObserver);

        // When
        order.notifyObservers();

        // Then
        verify(mockObserver).update(order);
    }
    @Test
    public void testChangeOrderStatus_ShouldChangeStatusAndNotifyObservers() {
        // Given
        Order order = new Order();
        order.registerObserver(mockObserver);
        order.setOrderStatus(OrderStatus.PENDING);

        // When
        order.changeOrderStatus(OrderStatus.SHIPPED);

        // Then
        assertEquals(OrderStatus.SHIPPED, order.getOrderStatus());
        verify(mockObserver).update(order);
    }
    @Test
    public void testRegisterObserver_ShouldAddObserverToList() {
        // Given
        Order order = new Order();
        Observer mockObserver = Mockito.mock(Observer.class);

        // When
        order.registerObserver(mockObserver);

        // Then
        List<Observer> observers = order.getRegisteredObservers();
        assertTrue(observers.contains(mockObserver));
    }
    @Test
    public void testUnregisterObserver_ShouldRemoveObserverFromList() {
        // Given
        Order order = new Order();
        Observer mockObserver = Mockito.mock(Observer.class);
        order.registerObserver(mockObserver);

        // When
        order.unregisterObserver(mockObserver);

        // Then
        List<Observer> observers = order.getRegisteredObservers();
        assertFalse(observers.contains(mockObserver));
    }


//    @Test
//    public void testChangeOrderStatus_ShouldChangeStatusAndNotifyObservers() {
//        // Given
//        Order order = new Order();
//        order.registerObserver(mockObserver);
//        order.setOrderStatus(OrderStatus.PENDING);
//
//        // When
//        order.changeOrderStatus(OrderStatus.SHIPPED);
//
//        // Then
//        assertEquals(OrderStatus.SHIPPED, order.getOrderStatus());
//        verify(mockObserver).update(order);
//    }

//    @Test
//    public void testRegisterObserver_ShouldAddObserverToList() {
//        // Given
//        Order order = new Order();
//        Observer mockObserver = Mockito.mock(Observer.class);
//
//        // When
//        order.registerObserver(mockObserver);
//
//        // Then
//        List<Observer> observers = order.getRegisteredObservers();
//        assertTrue(observers.contains(mockObserver));
//    }

//    @Test
//    public void testUnregisterObserver_ShouldRemoveObserverFromList() {
//        // Given
//        Order order = new Order();
//        Observer mockObserver = Mockito.mock(Observer.class);
//        order.registerObserver(mockObserver);
//
//        // When
//        order.unregisterObserver(mockObserver);
//
//        // Then
//        List<Observer> observers = order.getRegisteredObservers();
//        assertFalse(observers.contains(mockObserver));
//    }

    @Test
    public void testSetUser_GivenOrderAndUser_ShouldSetUser() {
        // Given
        User user = new User("user@example.com", "password", "avatar.png", "John", "Doe");
        Order order = new Order();

        // When
        order.setUser(user);

        // Then
        assertEquals(user, order.getUser());
    }

    @Test
    public void testGetUser_WithUserSet_ShouldReturnUser() {
        // Given
        User user = new User("user@example.com", "password", "avatar.png", "John", "Doe");
        Order order = new Order();
        order.setUser(user);

        // When
        User retrievedUser = order.getUser();

        // Then
        assertEquals(user, retrievedUser);
    }

//    @Test
//    public void testGetProducts_WithPositions_ShouldReturnListOfProducts() {
//        // Given
//        Product product1 = new Product("Product 1", "Description of product 1", "miniature1.jpg", BigDecimal.TEN, ProductType.COOKING_EQUIPMENT, true, LocalDate.now(), new Author(), new Category());
//        Product product2 = new Product("Product 2", "Description of product 2", "miniature2.jpg", BigDecimal.FIVE, ProductType.KITCHEN_EQUIPMENT, true, LocalDate.now(), new Author(), new Category());
//        List<Product> products = Arrays.asList(product1, product2);
//
//        List<LineOfOrder> lineOfOrders = new ArrayList<>();
//        lineOfOrders.add(new LineOfOrder(product1, 2, BigDecimal.TEN));
//        lineOfOrders.add(new LineOfOrder(product2, 3, BigDecimal.TEN));
//        Order order = new Order();
//        order.setListOfOrders(lineOfOrders);
//
//        // When
//        List<Product> retrievedProducts = order.getProducts();
//
//        // Then
//        assertEquals(products, retrievedProducts);
//    }

//    @Test
//    public void testSetListOfOrders_WithValidList_ShouldSetList() {
//        // Given
//        List<LineOfOrder> lineOfOrders = createLineOfOrders(); // Create a list of LineOfOrder objects
//        Order order = new Order();
//
//        // When
//        order.setListOfOrders(lineOfOrders);
//
//        // Then
//        assertEquals(lineOfOrders, order.getLineOfOrders());
//    }

//    private List<LineOfOrder> createLineOfOrders() {
//        List<LineOfOrder> lineOfOrders = new ArrayList<>();
//
//        // Example product data
//        Product product1 = new Product(1L, mockAuthor, mockCategory, "Title", "Description",
//                "miniature.jpg", BigDecimal.TEN, ProductType.COOKING_EQUIPMENT, true, LocalDate.now());;
//        Product product2 = new Product(1L, mockAuthor, mockCategory, "Title", "Description",
//                "miniature.jpg", BigDecimal.TEN, ProductType.COOKING_EQUIPMENT, true, LocalDate.now());
//
//        // Create LineOfOrder objects with sample quantities
//        lineOfOrders.add(new LineOfOrder(product1, 2, BigDecimal.TEN));
//        lineOfOrders.add(new LineOfOrder(product2, 3, BigDecimal.TEN));
//
//        return lineOfOrders;
//    }

//    @Test(expected = NullPointerException.class)
//    public void testRegisterObserver_WithNull_ShouldThrowException() {
//        Order order = new Order();
//
//
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void testChangeOrderStatus_WithInvalidStatus_ShouldThrowException() {
//        Order order = new Order();
//
//        order.changeOrderStatus(null);
//    }
}
