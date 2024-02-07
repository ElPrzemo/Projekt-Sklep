package com.example.projektsklep.model.dto;


import lombok.Builder;

import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.Set;

@Builder
public record UserDTO(
        Long id,
        @NotBlank String email,
        @NotBlank String firstName,
        @NotBlank String lastName,
        String password,
        AddressDTO address,
        Set<RoleDTO> roles
) {}