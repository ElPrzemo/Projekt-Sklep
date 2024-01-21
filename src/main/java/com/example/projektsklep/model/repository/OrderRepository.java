package com.example.projektsklep.model.repository;


import com.example.projektsklep.model.entities.order.Order;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository  extends JpaRepository<Order, Long> {


    // Filter orders by customer ID
    List<Order> findAllOrdersByUserId(long userId);

    // Filter orders by status
    List<Order> findAllByOrderStatus(OrderStatus orderStatus);

    // Filter orders by date created between specified dates


    // Retrieves an order by its ID
    Optional<Order> findById(long id);

    // Saves an order to the database
    Order save(Order order);

    // Deletes an order from the database
    void delete(Order order);

    // Filters orders by total amount greater than a specified value


    List<Order> findAll();

    // Filters orders by total amount greater than a specified value and sorts them by date created in descending order

}