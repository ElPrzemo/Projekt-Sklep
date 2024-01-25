package com.example.projektsklep.model.dto;

import java.util.Objects;
import lombok.Builder;
import lombok.Data;

@Builder

public record AddressDTO(
        Long id,
        String street,
        String city,
        String postalCode,
        String country
) {
    public AddressDTO {
        Objects.requireNonNull(street);
        Objects.requireNonNull(city);
        Objects.requireNonNull(postalCode);
        Objects.requireNonNull(country);
    }
}