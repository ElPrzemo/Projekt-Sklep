package com.example.projektsklep.model.dto;

import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.dto.LineOfOrderDTO;
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
        List<LineOfOrderDTO> lineOfOrders,
        AddressDTO shippingAddress // Upewnij się, że to pole jest obecne
) {
    public OrderDTO {
        Objects.requireNonNull(id);
        Objects.requireNonNull(userId);
        Objects.requireNonNull(orderStatus);
        Objects.requireNonNull(dateCreated);
        // Sprawdzenie, czy lineOfOrders i shippingAddress są niepuste, jeśli jest to wymagane
    }
}