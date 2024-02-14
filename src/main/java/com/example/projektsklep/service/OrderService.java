package com.example.projektsklep.service;

import com.example.projektsklep.exception.OrderUpdateException;
import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.dto.LineOfOrderDTO;
import com.example.projektsklep.model.dto.OrderDTO;
import com.example.projektsklep.model.entities.adress.Address;
import com.example.projektsklep.model.entities.order.Order;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.enums.OrderStatus;
import com.example.projektsklep.model.repository.OrderRepository;
import com.example.projektsklep.model.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final UserRepository userRepository;

  public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
    this.orderRepository = orderRepository;
    this.userRepository = userRepository;
  }

  public Page<OrderDTO> findAllOrders(Pageable pageable) {
    return orderRepository.findAll(pageable)
            .map(this::convertToOrderDTO);
  }

  public OrderDTO findOrderDTOById(Long id) {
    return orderRepository.findById(id)
            .map(this::convertToOrderDTO)
            .orElse(null);
  }

  public OrderDTO saveOrderDTO(OrderDTO orderDTO) {
    Order order = convertToOrder(orderDTO);
    if (orderDTO.userId() != null) {
      User user = userRepository.findById(orderDTO.userId())
              .orElseThrow(() -> new RuntimeException("User not found"));
      order.setAccountHolder(user);
    }

    if (orderDTO.shippingAddress() != null) {
      // Tutaj powinna znaleźć się logika konwersji AddressDTO na Address i ustawienia go w zamówieniu
    }

    order = orderRepository.save(order);
    return convertToOrderDTO(order);
  }

  public List<OrderDTO> findAllOrdersByUserId(Long userId) {
    return orderRepository.findByAccountHolder_Id(userId).stream()
            .map(this::convertToOrderDTO)
            .collect(Collectors.toList());
  }

  public boolean updateOrderDTO(Long id, OrderDTO orderDTO) {
    Order existingOrder = orderRepository.findById(id)
            .orElseThrow(() -> new OrderUpdateException("Order not found"));
    updateOrderData(existingOrder, orderDTO);
    existingOrder = orderRepository.save(existingOrder);
    return existingOrder != null;
  }

  public List<OrderDTO> findAllOrdersByStatus(OrderStatus orderStatus) {
    return orderRepository.findAllByOrderStatus(orderStatus).stream()
            .map(this::convertToOrderDTO)
            .collect(Collectors.toList());
  }

  private OrderDTO convertToOrderDTO(Order order) {
    String statusName = (order.getOrderStatus() != null) ? order.getOrderStatus().name() : "UNDEFINED";

    List<LineOfOrderDTO> lineOfOrdersDTO = Optional.ofNullable(order.getLineOfOrders())
            .orElseGet(Collections::emptyList)
            .stream()
            .map(line -> new LineOfOrderDTO(
                    line.getId(),
                    line.getProduct().getId(),
                    line.getQuantity(),
                    line.getUnitPrice()))
            .collect(Collectors.toList());

    AddressDTO shippingAddressDTO = Optional.ofNullable(order.getShippingAddress())
            .map(address -> new AddressDTO(
                    address.getId(),
                    address.getStreet(),
                    address.getCity(),
                    address.getPostalCode(),
                    address.getCountry()))
            .orElse(null);

    return new OrderDTO(
            order.getId(),
            order.getAccountHolder().getId(),
            statusName,
            order.getDateCreated(),
            order.getSentAt(),
            order.getTotalPrice(),
            lineOfOrdersDTO,
            shippingAddressDTO);
  }

  public boolean updateOrderStatus(Long id, String orderStatus) {
    Order existingOrder = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));
    // Użyj poprawnej wartości enum, np. OrderStatus.SHIPPED zamiast SENT
    existingOrder.setOrderStatus(OrderStatus.valueOf(orderStatus.toUpperCase()));
    existingOrder = orderRepository.save(existingOrder);
    return existingOrder != null;
  }
  private Order convertToOrder(OrderDTO orderDTO) {
    Order order = new Order();
    // Implementacja logiki konwersji DTO na encję Order, w tym obsługa AddressDTO
    return order;
  }


  private void updateOrderData(Order order, OrderDTO orderDTO) {
    // Tutaj powinna znaleźć się logika aktualizacji danych zamówienia na podstawie OrderDTO
  }


  private Address convertToAddress(AddressDTO addressDTO) {
    Address address = new Address();
    // Implementacja logiki konwersji DTO na encję Address
    return address;
  }

  public OrderDTO createOrderDTO(Long userId, String orderStatus, List<LineOfOrderDTO> lineOfOrders, AddressDTO shippingAddress) {
    LocalDate now = LocalDate.now();
    BigDecimal totalPrice = calculateTotalPrice(lineOfOrders); // Implementacja metody obliczającej cenę

    return new OrderDTO(null, userId, orderStatus, now, null, totalPrice, lineOfOrders, shippingAddress);
  }

  private BigDecimal calculateTotalPrice(List<LineOfOrderDTO> lineOfOrders) {
    // przykładowa implementacja
    return BigDecimal.ZERO; // Zwróć rzeczywistą wartość
  }
}