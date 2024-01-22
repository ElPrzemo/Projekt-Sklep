package com.example.projektsklep.service;

import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.dto.UserDTO;
import com.example.projektsklep.model.entities.adress.Address;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
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

    // Istniejące metody...

    public List<UserDTO> findAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> findUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToUserDTO);
    }

    public UserDTO saveUser(UserDTO userDTO) {
        User user = convertToUser(userDTO);
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
        return userRepository.findByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContaining(name, name).stream()
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

    private User convertToUser(UserDTO userDTO) {
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
        user.setPhoneNumber(userDTO.phoneNumber());
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
        user.setPhoneNumber(userDTO.phoneNumber());
        if (userDTO.address() != null) {
            Address address = addressService.convertToEntity(userDTO.address());
            user.setAddress(address);
        }
    }
}