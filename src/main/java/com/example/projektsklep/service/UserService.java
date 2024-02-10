package com.example.projektsklep.service;

import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.dto.UserDTO;
import com.example.projektsklep.model.entities.adress.Address;
import com.example.projektsklep.model.entities.role.Role;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.enums.AdminOrUser;
import com.example.projektsklep.model.repository.AddressRepository;
import com.example.projektsklep.model.repository.RoleRepository;
import com.example.projektsklep.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AddressService addressService;
    private final AddressRepository addressRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, AddressService addressService, AddressRepository addressRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.addressService = addressService;
        this.addressRepository = addressRepository;
        this.roleRepository = roleRepository;
    }


    // Istniejące metody...

    public Page<UserDTO> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::convertToUserDTO);
    }

    public Optional<UserDTO> findUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToUserDTO);
    }

    public UserDTO saveUser(UserDTO userDTO, AddressDTO addressDTO, AdminOrUser role) {
        // Sprawdzenie, czy addressDTO nie jest null i konwersja na encję Address
        Address address = null;
        if (addressDTO != null) {
            address = addressService.convertToEntity(addressDTO);
            address = addressRepository.save(address);
        }

        // Konwersja UserDTO na encję User
        User user = convertToUser(userDTO);

        // Przypisanie adresu do użytkownika, jeśli istnieje
        if (address != null) {
            user.setAddress(address);
        }

        // Przypisanie roli użytkownikowi
        Set<Role> roles = new HashSet<>();
        if (role != null) {
            Role userRole = roleRepository.findByRoleType(role.name())
                    .orElseThrow(() -> new RuntimeException("Role not found: " + role));
            roles.add(userRole);
        }
        user.setRoles(roles);

        // Zapis użytkownika
        user = userRepository.save(user);

        // Konwersja zapisanego użytkownika z powrotem na UserDTO i zwrócenie
        return convertToUserDTO(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<UserDTO> findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToUserDTO);
    }

    public List<UserDTO> findUsersByLastName(String lastName) {
        return userRepository.findByLastNameIgnoreCaseContaining(lastName).stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO updateUserProfileOrAdmin(Long userId, UserDTO userDTO, boolean isAdmin) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (isAdmin) {
            updateAdminFields(existingUser, userDTO);
        } else {
            updateUserFields(existingUser, userDTO);
        }
        userRepository.save(existingUser);
        return convertToUserDTO(existingUser);
    }

    public void updateUserProfileAndAddress(Long userId, UserDTO userDTO, AddressDTO addressDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Address address = user.getAddress();

        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        user.setEmail(userDTO.email());
        // Zakładamy, że hasło jest już zahaszowane lub null, jeśli nie zmieniamy

        if (address == null) {
            address = new Address();
        }
        address.setStreet(addressDTO.street());
        address.setCity(addressDTO.city());
        address.setPostalCode(addressDTO.postalCode());
        address.setCountry(addressDTO.country());

        if (user.getAddress() == null) {
            address = addressRepository.save(address);
            user.setAddress(address);
        }

        userRepository.save(user);
    }

    // Metody pomocnicze...

    private UserDTO convertToUserDTO(User user) {
        AddressDTO addressDTO = user.getAddress() != null ? addressService.convertToDTO(user.getAddress()) : null;
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .address(addressDTO)
                .build();
    }

    public User convertToUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.id());
        user.setEmail(userDTO.email());
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        user.setAddress(userDTO.address() != null ? addressService.convertToEntity(userDTO.address()) : null);
        return user;
    }

    private void updateUserFields(User user, UserDTO userDTO) {
        user.setEmail(userDTO.email());
        if (userDTO.password() != null) {
            user.setPasswordHash(userDTO.password());
        }
        if (userDTO.address() != null) {
            Address address = addressService.convertToEntity(userDTO.address());
            user.setAddress(address);
        }
    }

    private void updateAdminFields(User user, UserDTO userDTO) {
        user.setEmail(userDTO.email());
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        if (userDTO.address() != null) {
            Address address = addressService.convertToEntity(userDTO.address());
            user.setAddress(address);
        }
    }

    public UserDTO createUserDTO(UserDTO userDTO, AddressDTO addressDTO) {
        // Tutaj można dodać logikę weryfikacji lub transformacji danych, jeśli to konieczne
        return UserDTO.builder()
                .email(userDTO.email())
                .firstName(userDTO.firstName())
                .lastName(userDTO.lastName())
                .address(addressDTO)
                .build();
    }
}