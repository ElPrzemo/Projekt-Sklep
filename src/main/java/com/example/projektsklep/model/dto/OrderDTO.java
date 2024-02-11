package com.example.projektsklep.model.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotNull;

@Builder
public record OrderDTO(
        Long id,
        @NotNull(message = "User ID cannot be null") Long userId,
        @NotNull(message = "Order status cannot be null") String orderStatus,
        LocalDate dateCreated,
        LocalDate sentAt,
        BigDecimal totalPrice,
        @NotNull(message = "List of order lines cannot be null") List<LineOfOrderDTO> lineOfOrders,
        AddressDTO shippingAddress
) {}