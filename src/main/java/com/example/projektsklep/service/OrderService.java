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
import com.example.projektsklep.model.repository.AddressRepository;
import com.example.projektsklep.model.repository.OrderRepository;
import com.example.projektsklep.model.repository.ProductRepository;
import com.example.projektsklep.model.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
  private final ProductRepository productRepository; // Założenie, że taka klasa istnieje
  private final AddressRepository addressRepository; // Założenie, że taka klasa istnieje

  private static final Logger log = LoggerFactory.getLogger(OrderService.class);

  public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, AddressRepository addressRepository) {
    this.orderRepository = orderRepository;
    this.userRepository = userRepository;
    this.productRepository = productRepository;
    this.addressRepository = addressRepository;
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
    // Pobierz użytkownika na podstawie userId z orderDTO
    User user = userRepository.findById(orderDTO.userId())
            .orElseThrow(() -> new RuntimeException("User not found"));

    OrderStatus status;
    try {
      status = OrderStatus.valueOf(orderDTO.orderStatus());
    } catch (IllegalArgumentException e) {
      log.error("Invalid OrderStatus value: {}", orderDTO.orderStatus(), e);
      status = OrderStatus.NEW_ORDER; // Przypisz domyślny status, jeśli podano nieprawidłową wartość
    }

    // Tutaj można przekonwertować AddressDTO i LineOfOrderDTO na encje
    Address shippingAddress = convertToAddress(orderDTO.shippingAddress());
    List<LineOfOrder> lineOfOrders = convertLineOfOrdersDTOToList(orderDTO.lineOfOrders(), null); // Przekaz null jako zamówienie, które zostanie ustawione później

    Order order = new Order(); // Stwórz nowe lub znajdź istniejące zamówienie w bazie, jeśli aktualizujesz
    order.setAccountHolder(user);
    order.setOrderStatus(status);
    order.setShippingAddress(shippingAddress);
    order.setLineOfOrders(lineOfOrders);
    order.setDateCreated(LocalDate.now()); // lub użyj orderDTO.dateCreated(), jeśli aktualizujesz
    // Ustaw inne pola zamówienia...

    // Zapisz zamówienie w bazie danych
    Order savedOrder = orderRepository.save(order);

    // Przekonwertuj zapisane zamówienie z powrotem na OrderDTO
    return convertToOrderDTO(savedOrder);
  }

  public List<OrderDTO> findAllOrdersByStatus(OrderStatus orderStatus) {
    return orderRepository.findAllByOrderStatus(orderStatus).stream()
            .map(this::convertToOrderDTO)
            .collect(Collectors.toList());
  }

  public boolean updateOrderStatus(Long id, String orderStatus) {
    Order existingOrder = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));
    // Użyj poprawnej wartości enum, np. OrderStatus.SHIPPED zamiast SENT
    existingOrder.setOrderStatus(OrderStatus.valueOf(orderStatus.toUpperCase()));
    existingOrder = orderRepository.save(existingOrder);
    return existingOrder != null;
  }
  private OrderDTO convertToOrderDTO(Order order) {
    // Initialize variables to avoid null pointer exceptions
    Long id = order.getId();
    Long userId = order.getAccountHolder() != null ? order.getAccountHolder().getId() : null;
    String orderStatus = order.getOrderStatus() != null ? order.getOrderStatus().name() : null;
    LocalDate dateCreated = order.getDateCreated();
    LocalDate sentAt = order.getSentAt();
    BigDecimal totalPrice = order.getTotalPrice();

    // Handle potential null ShippingAddress
    AddressDTO shippingAddressDTO = null;
    if (order.getShippingAddress() != null) {
      Address shippingAddress = order.getShippingAddress();
      shippingAddressDTO = new AddressDTO(shippingAddress.getId(), shippingAddress.getStreet(), shippingAddress.getCity(), shippingAddress.getPostalCode(), shippingAddress.getCountry());
    }

    List<LineOfOrderDTO> lineOfOrdersDTO = order.getLineOfOrders().stream()
            .map(line -> new LineOfOrderDTO(line.getId(), line.getProduct().getId(), line.getQuantity(), line.getUnitPrice()))
            .collect(Collectors.toList());

    // Construct and return the OrderDTO
    return new OrderDTO(
            id,
            userId,
            orderStatus,
            dateCreated,
            sentAt,
            totalPrice,
            lineOfOrdersDTO,
            shippingAddressDTO
    );
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
    return lineOfOrdersDTO.stream()
            .map(dto -> convertToLineOfOrder(dto, order))
            .collect(Collectors.toList());
  }

  private LineOfOrder convertToLineOfOrder(LineOfOrderDTO dto, Order order) {
    Product product = productRepository.findById(dto.productId())
            .orElseThrow(() -> new RuntimeException("Product not found"));
    return new LineOfOrder(dto.id(), order, product, dto.quantity(), dto.unitPrice());
  }
  private Address convertToAddress(AddressDTO dto) {
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