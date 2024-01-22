package com.example.projektsklep.model.dto;

import lombok.Builder;

import java.util.Objects;

@Builder
public record UserDTO(
        Long id,
        String email,
        String firstName,
        String lastName,
        int phoneNumber, // added
        String password, // added
        AddressDTO address
) {
    public UserDTO {
        Objects.requireNonNull(id);
        Objects.requireNonNull(email);
        Objects.requireNonNull(firstName);
        Objects.requireNonNull(lastName);
    }
}