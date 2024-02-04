package com.example.projektsklep.service;

import com.example.projektsklep.exception.UserNotFoundException;
import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.dto.UserDTO;
import com.example.projektsklep.model.entities.adress.Address;
import com.example.projektsklep.model.entities.role.Role;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.enums.AdminOrUser;
import com.example.projektsklep.model.repository.AddressRepository;
import com.example.projektsklep.model.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Autowired
    public UserService(UserRepository userRepository, AddressService addressService) {
        this.userRepository = userRepository;
        this.addressService = addressService;
    }

    public Page<UserDTO> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getPhoneNumber(),
                        null,
                        user.getAddress() != null ? addressService.convertToDTO(user.getAddress()) : null
                ));
    }

    public Optional<UserDTO> findUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getPhoneNumber(),
                        null,
                        user.getAddress() != null ? addressService.convertToDTO(user.getAddress()) : null
                ));
    }

    @Transactional
    public UserDTO saveUser(UserDTO userDTO, AddressDTO addressDTO, AdminOrUser role) {
        User user = new User();
        user.setEmail(userDTO.email());
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        // Ustawienia dotyczące hasła i roli zostały pominięte dla uproszczenia
        if (addressDTO != null) {
            Address address = addressService.convertToEntity(addressDTO);
            user.setAddress(address);
        }
        user = userRepository.save(user);
        return convertToUserDTO(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<UserDTO> findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToUserDTO);
    }

    public List<UserDTO> findUsersByName(String name) {
        return userRepository.findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(name, name)
                .stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDTO updateUserProfile(Long userId, UserDTO updatedUserDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Aktualizuj dane użytkownika na podstawie updatedUserDTO
        user.setFirstName(updatedUserDTO.firstName());
        user.setLastName(updatedUserDTO.lastName());
        user.setEmail(updatedUserDTO.email());

        // Jeśli dostępny jest nowy adres, zaktualizuj go
        if (updatedUserDTO.address() != null) {
            Address address = addressService.convertToEntity(updatedUserDTO.address());
            user.setAddress(address);
        }

        // Zapisz zmienionego użytkownika
        user = userRepository.save(user);

        // Zwróć zaktualizowany obiekt UserDTO
        return convertToUserDTO(user);
    }

    public void updateUserDataByAdmin(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setEmail(userDTO.email());
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        if (userDTO.address() != null) {
            Address address = addressService.convertToEntity(userDTO.address());
            user.setAddress(address);
        }
        userRepository.save(user);
    }

    private UserDTO convertToUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                null,
                user.getAddress() != null ? addressService.convertToDTO(user.getAddress()) : null
        );
    }

    public User convertToUser(UserDTO userDTO) {
        // Logika konwersji UserDTO na User
        User user = new User();
        // Implementacja ustawiania pól
        return user;
    }

    private void updateUserFields(User user, UserDTO userDTO) {
        // Aktualizacja pól użytkownika na podstawie userDTO
    }

    private void updateAddressFields(Address address, AddressDTO addressDTO) {
        // Aktualizacja pól adresu na podstawie addressDTO
        if (address == null) {
            address = new Address();
            // Konieczne może być dodatkowe zapisanie adresu, jeśli jest nowy
        }
        // Ustawianie pól adresu
    }
}