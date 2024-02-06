package com.example.projektsklep.model.dto;

import java.util.Objects;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Builder

public record AddressDTO(
        Long id,
        @NotBlank(message = "Ulica jest wymagana")
        String street,
        @NotBlank(message = "Miasto jest wymagane")
        String city,
        @NotBlank(message = "Kod pocztowy jest wymagany")
        String postalCode,
        @NotBlank(message = "Kraj jest wymagany")
        String country
) {
}