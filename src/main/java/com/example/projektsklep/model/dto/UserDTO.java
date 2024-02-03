package com.example.projektsklep.model.dto;


import lombok.Builder;

import java.util.Objects;
import java.util.Set;

@Builder
public record UserDTO(
        Long id,
        String email,
        String firstName,
        String lastName,
        int phoneNumber, // already added
        String password, // already added
        AddressDTO address,
        Set<RoleDTO> roles // now added
) {
    public UserDTO {
        Objects.requireNonNull(id);
        Objects.requireNonNull(email);
        Objects.requireNonNull(firstName);
        Objects.requireNonNull(lastName);
        // roles field can be null if you want to allow users without roles
    }
}