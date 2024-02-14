package com.example.projektsklep.service;

import com.example.projektsklep.exception.OrderNotFoundException;
import com.example.projektsklep.exception.OrderUpdateException;
import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.dto.LineOfOrderDTO;
import com.example.projektsklep.model.dto.OrderDTO;
import com.example.projektsklep.model.entities.adress.Address;
import com.example.projektsklep.model.entities.order.LineOfOrder;
import com.example.projektsklep.model.entities.order.Order;
import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.enums.OrderStatus;
import com.example.projektsklep.model.repository.OrderRepository;
import com.example.projektsklep.model.repository.UserRepository;
import jakarta.transaction.Transactional;
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

  public Optional<OrderDTO> findOrderDTOById(Long id) {
    return orderRepository.findById(id)
            .map(this::convertToOrderDTO);
  }

  @Transactional
  public OrderDTO saveOrderDTO(OrderDTO orderDTO) {
    User user = userRepository.findById(orderDTO.userId())
            .orElseThrow(() -> new RuntimeException("User not found"));
    Address shippingAddress = convertToAddress(orderDTO.shippingAddress());

    Order order = new Order();
    order.setAccountHolder(user);
    order.setShippingAddress(shippingAddress);
    order.setOrderStatus(OrderStatus.valueOf(orderDTO.orderStatus()));
    order.setDateCreated(orderDTO.dateCreated());
    order.setSentAt(orderDTO.sentAt());
    order.setTotalPrice(orderDTO.totalPrice());
    order.setLineOfOrders(convertLineOfOrdersDTOToList(orderDTO.lineOfOrders(), order));

    Order savedOrder = orderRepository.save(order);
    return convertToOrderDTO(savedOrder);
  }

  public boolean updateOrderStatus(Long orderId, OrderStatus status) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException());
    order.setOrderStatus(status);
    orderRepository.save(order);
    return true;
  }

  private OrderDTO convertToOrderDTO(Order order) {
    // Przygotowanie danych, które są zawsze obecne
    Long id = order.getId();
    Long userId = order.getAccountHolder() != null ? order.getAccountHolder().getId() : null;
    String orderStatus = order.getOrderStatus() != null ? order.getOrderStatus().name() : null;
    LocalDate dateCreated = order.getDateCreated();
    LocalDate sentAt = order.getSentAt();
    BigDecimal totalPrice = order.getTotalPrice();

    // Konwersja linii zamówienia
    List<LineOfOrderDTO> lineOfOrders = order.getLineOfOrders().stream()
            .map(line -> new LineOfOrderDTO(line.getId(), line.getProduct().getId(), line.getQuantity(), line.getUnitPrice()))
            .collect(Collectors.toList());

    // Bezpieczna konwersja adresu wysyłkowego
    AddressDTO shippingAddressDTO = null;
    if (order.getShippingAddress() != null) {
      Address shippingAddress = order.getShippingAddress();
      shippingAddressDTO = new AddressDTO(shippingAddress.getId(), shippingAddress.getStreet(), shippingAddress.getCity(), shippingAddress.getPostalCode(), shippingAddress.getCountry());
    }

    // Tworzenie i zwracanie DTO
    return new OrderDTO(id, userId, orderStatus, dateCreated, sentAt, totalPrice, lineOfOrders, shippingAddressDTO);
  }

  public List<OrderDTO> findAllOrdersByStatus(OrderStatus orderStatus) {
    return orderRepository.findAllByOrderStatus(orderStatus).stream()
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

  public List<OrderDTO> findAllOrdersByUserId(Long userId) {
    return orderRepository.findByAccountHolder_Id(userId).stream()
            .map(this::convertToOrderDTO)
            .collect(Collectors.toList());
  }

  private void updateOrderData(Order order, OrderDTO orderDTO) {
    // Tutaj powinna znaleźć się logika aktualizacji danych zamówienia na podstawie OrderDTO
  }


  private List<LineOfOrder> convertLineOfOrdersDTOToList(List<LineOfOrderDTO> lineOfOrdersDTO, Order order) {
    // Assuming you have a method to convert each LineOfOrderDTO to LineOfOrder
    return lineOfOrdersDTO.stream()
            .map(dto -> convertToLineOfOrder(dto, order))
            .collect(Collectors.toList());
  }

  private LineOfOrder convertToLineOfOrder(LineOfOrderDTO dto, Order order) {
    // You need to implement this method
    Product product = new Product(); // This should be fetched from the database
    return new LineOfOrder(dto.id(), order, product, dto.quantity(), dto.unitPrice());
  }

  private Address convertToAddress(AddressDTO dto) {
    // Implement the conversion logic
    return new Address(dto.id(), dto.street(), dto.city(), dto.postalCode(), dto.country());
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