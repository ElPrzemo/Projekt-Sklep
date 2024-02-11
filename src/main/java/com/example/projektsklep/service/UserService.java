package com.example.projektsklep.service;

import com.example.projektsklep.model.dto.AddressDTO;
import com.example.projektsklep.model.dto.RoleDTO;
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
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
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
            Role userRole = roleRepository.findByRoleType(role)
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

    public UserDTO updateUserProfileOrAdmin(Long userId, UserDTO userDTO, boolean isAdmin, String authenticatedUserEmail) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Ustawienie emaila na adres email zalogowanego użytkownika
        userDTO = new UserDTO(userDTO.id(), userDTO.firstName(), userDTO.lastName(), authenticatedUserEmail, userDTO.password(),  userDTO.address(), userDTO.roles());

        if (isAdmin) {
            updateAdminFields(existingUser, userDTO);
        } else {
            // Zaktualizuj pola użytkownika, ale pomiń aktualizację emaila, ponieważ jest on już ustawiony
            updateUserFields(existingUser, userDTO);
        }
        userRepository.save(existingUser);
        return convertToUserDTO(existingUser);
    }

    public UserDTO updateUserProfileAndAddress(String email, UserDTO userDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Aktualizacja danych użytkownika
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        // Zakładam, że hasło jest odpowiednio obsługiwane

        Address address = user.getAddress() != null ? user.getAddress() : new Address();
        AddressDTO addressDTO = userDTO.address();
        if (addressDTO != null) {
            address.setStreet(addressDTO.street());
            address.setCity(addressDTO.city());
            address.setPostalCode(addressDTO.postalCode());
            address.setCountry(addressDTO.country());
            address = addressRepository.save(address); // Zapisz lub zaktualizuj adres
            user.setAddress(address);
        }

        userRepository.save(user); // Zapisz zmiany użytkownika

        return convertToUserDTO(user); // Konwersja na UserDTO i zwrócenie
    }

    private UserDTO convertToUserDTO(User user) {
        AddressDTO addressDTO = null;
        if (user.getAddress() != null) {
            Address address = user.getAddress();
            addressDTO = AddressDTO.builder()
                    .id(address.getId())
                    .street(address.getStreet())
                    .city(address.getCity())
                    .postalCode(address.getPostalCode())
                    .country(address.getCountry())
                    .build();
        }

        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(null) // Nie zwracaj hasła
                .address(addressDTO)
                .roles(user.getRoles().stream().map(role -> new RoleDTO(role.getId(), role.getRoleType().name())).collect(Collectors.toSet()))
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