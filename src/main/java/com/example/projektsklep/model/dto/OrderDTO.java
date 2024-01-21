package com.example.projektsklep.model.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


@Builder
public record OrderDTO(
        Long id,
        Long userId,
        String orderStatus,
        LocalDate dateCreated,
        LocalDate sentAt,
        BigDecimal totalPrice,
        List<LineOfOrderDTO> lineOfOrders
) {

    public OrderDTO {
        Objects.requireNonNull(id);
        Objects.requireNonNull(userId);
        Objects.requireNonNull(orderStatus);
        Objects.requireNonNull(dateCreated);
        Objects.requireNonNull(lineOfOrders);
    }
}