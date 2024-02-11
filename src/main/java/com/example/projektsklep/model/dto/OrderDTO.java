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
        AddressDTO shippingAddress // Tutaj dodajemy AddressDTO jako opcjonalne pole
) {
    public OrderDTO(long id, UserDTO user, String orderStatus) {
        // Wywołaj kanoniczny konstruktor w pierwszej linii, przekazując wymagane argumenty:
        this(id, user.getId(), orderStatus, LocalDate.now(), null, BigDecimal.ZERO, List.of(), null);
        // Uzupełnij pozostałe pola, jeśli to konieczne
    }

}