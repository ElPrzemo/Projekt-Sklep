
package com.example.projektsklep.service;


import com.example.projektsklep.exception.OrderUpdateException;
import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.dto.LineOfOrderDTO;
import com.example.projektsklep.model.dto.OrderDTO;
import com.example.projektsklep.model.entities.adress.Address;
import com.example.projektsklep.model.entities.order.Order;
import com.example.projektsklep.model.enums.OrderStatus;
import com.example.projektsklep.model.repository.OrderRepository;
import com.example.projektsklep.utils.AddressDTOInitializer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

  private final OrderRepository orderRepository;

  public OrderService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  // Istniejące metody...

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
    return convertToOrderDTO(orderRepository.save(order));
  }

  public List<OrderDTO> findAllOrdersByUserId(long userId) {
    return orderRepository.findByAccountHolder_Id(userId).stream()
            .map(this::convertToOrderDTO)
            .collect(Collectors.toList());
  }


  public boolean updateOrderDTO(Long id, OrderDTO orderDTO) {
    Order existingOrder = orderRepository.findById(id)
            .orElseThrow(() -> new OrderUpdateException("Order not found"));
    updateOrderData(existingOrder, orderDTO);

    AddressDTO shippingAddressDTO = null;
    if (orderDTO != null) {
      shippingAddressDTO = orderDTO.shippingAddress();
    }
    if (shippingAddressDTO == null) {
      shippingAddressDTO = AddressDTOInitializer.createDefault();
    }

    existingOrder.setShippingAddress(convertToAddress(shippingAddressDTO));

    boolean updated = orderRepository.save(existingOrder) != null;
    return updated;
  }

  public List<Order> findAllOrdersByStatus(OrderStatus orderStatus) {
    return orderRepository.findAllByOrderStatus(orderStatus);
  }

  private OrderDTO convertToOrderDTO(Order order) {
    List<LineOfOrderDTO> lineOfOrdersDTO = order.getLineOfOrders().stream()
            .map(line -> new LineOfOrderDTO(
                    line.getId(),
                    line.getProduct().getId(),
                    line.getQuantity(),
                    line.getUnitPrice()))
            .collect(Collectors.toList());

    // Przykład, jak możesz uzyskać AddressDTO. Dostosuj logikę zgodnie z Twoim modelem.
    AddressDTO shippingAddressDTO = null;
    if (order.getShippingAddress() != null) {
      shippingAddressDTO = new AddressDTO(
              order.getShippingAddress().getId(),
              order.getShippingAddress().getStreet(),
              order.getShippingAddress().getCity(),
              order.getShippingAddress().getPostalCode(),
              order.getShippingAddress().getCountry());
    }

    return new OrderDTO(
            order.getId(),
            order.getAccountHolder().getId(),
            order.getOrderStatus().name(),
            order.getDateCreated(),
            order.getSentAt(),
            order.getTotalPrice(),
            lineOfOrdersDTO,
            shippingAddressDTO); // Teraz przekazujesz AddressDTO jako argument
  }

  private Order convertToOrder(OrderDTO orderDTO) {
    // Znajdź istniejące zamówienie lub stwórz nowe
    Order order = orderRepository.findById(orderDTO.id())
            .orElse(new Order());

    // Ustawienie użytkownika i pozostałych pól
    // Zakładam, że obiekt User jest już związany z Order
    order.setOrderStatus(OrderStatus.valueOf(orderDTO.orderStatus()));
    // Lista LineOfOrder musi być zaimplementowana w odpowiedni sposób
    // order.setLineOfOrders(convertLineOfOrdersDTO(orderDTO.lineOfOrders()));

    order.calculateTotalPrice(); // Oblicz cenę całkowitą
    return order;
  }

  private void updateOrderData(Order order, OrderDTO orderDTO) {
    order.setOrderStatus(OrderStatus.valueOf(orderDTO.orderStatus()));
    // Aktualizacja innych pól, jeśli jest to konieczne
  }
  private Address convertToAddress(AddressDTO addressDTO) {
    Address address = new Address();
    address.setStreet(addressDTO.street());
    address.setCity(addressDTO.city());
    address.setPostalCode(addressDTO.postalCode());
    address.setCountry(addressDTO.country());
    return address;
  }
}