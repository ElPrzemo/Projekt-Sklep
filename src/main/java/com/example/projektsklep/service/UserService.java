package com.example.projektsklep.service;

import com.example.projektsklep.exception.UserNotFoundException;
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

    @Autowired
    public UserService(UserRepository userRepository, AddressService addressService, AddressRepository addressRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.addressService = addressService;
        this.addressRepository = addressRepository;
        this.roleRepository = roleRepository;
    }

    public Page<UserDTO> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::convertToUserDTO);
    }

    public Optional<UserDTO> findUserById(Long id) {
        return userRepository.findById(id).map(this::convertToUserDTO);
    }

    @Transactional
    public UserDTO saveUser(UserDTO userDTO, AddressDTO addressDTO, AdminOrUser role) {
        Optional<Address> optionalAddress = Optional.ofNullable(addressDTO)
                .map(addressService::convertToEntity)
                .map(addressRepository::save);

        User user = convertToUser(userDTO);
        optionalAddress.ifPresent(user::setAddress);

        // Poprawiona część dotycząca roli:
        Set<Role> roles = new HashSet<>();
        roleRepository.findByRoleType(role).ifPresent(roles::add);

        user.setRoles(roles);
        user = userRepository.save(user);
        return convertToUserDTO(user);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<UserDTO> findUserByEmail(String email) {
        return userRepository.findByEmail(email).map(this::convertToUserDTO);
    }

    public List<UserDTO> findUsersByLastName(String lastName) {
        return userRepository.findByLastNameIgnoreCaseContaining(lastName).stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDTO updateUserProfileOrAdmin(Long userId, UserDTO userDTO, boolean isAdmin, String authenticatedUserEmail) {
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        updateUserFields(existingUser, userDTO, isAdmin, userDTO.password());
        existingUser = userRepository.save(existingUser);
        return convertToUserDTO(existingUser);
    }

    @Transactional
    public UserDTO updateUserProfileAndAddress(String email, UserDTO userDTO) {
        return userRepository.findByEmail(email)
                .map(user -> {
                    if (userDTO.password() != null && !userDTO.password().isBlank()) {
                        user.setPasswordHash(userDTO.password()); // Założenie: hasło jest już zahashowane
                    }
                    // Aktualizacja danych użytkownika (bez zmiany e-mail)
                    user.setFirstName(userDTO.firstName());
                    user.setLastName(userDTO.lastName());
                    // Aktualizacja adresu
                    Address address = addressService.convertToEntity(userDTO.address());
                    user.setAddress(address);
                    return userRepository.save(user);
                })
                .map(this::convertToUserDTO)
                .orElseThrow(() -> new UserNotFoundException("Nie znaleziono użytkownika o adresie e-mail: " + email));
    }

    private UserDTO convertToUserDTO(User user) {
        AddressDTO addressDTO = user.getAddress() != null ? addressService.convertToDTO(user.getAddress()) : null;
        Set<RoleDTO> roleDTOs = user.getRoles().stream()
                .map(role -> new RoleDTO(role.getId(), role.getRoleType().name()))
                .collect(Collectors.toSet());
        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), null, addressDTO, roleDTOs);
    }

    public User convertToUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.id());
        user.setEmail(userDTO.email());
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());

        Address address = Optional.ofNullable(userDTO.address())
                .map(addressDTO -> addressService.convertToEntity(addressDTO))
                .orElse(null);
        user.setAddress(address);

        Set<Role> roles = userDTO.roles().stream()
                .map(roleDTO -> roleRepository.findByRoleType(AdminOrUser.valueOf(roleDTO.roleType()))
                        .orElseThrow(() -> new RuntimeException("Role not found with type: " + roleDTO.roleType())))
                .collect(Collectors.toSet());
        user.setRoles(roles);

        return user;
    }

    public UserDTO createEmptyUserDTO() {
        return new UserDTO(
                null, // ID użytkownika jest null, ponieważ jest to nowy użytkownik
                "",   // Puste imię
                "",   // Puste nazwisko
                "",   // Pusty email
                "",   // Puste hasło
                new AddressDTO(null, "", "", "", ""), // Pusty obiekt AddressDTO
                new HashSet<>() // Pusty zestaw ról
        );
    }

    @Transactional
    public UserDTO registerNewUser(UserDTO userDTO, AdminOrUser roleType) {
        // Konwersja DTO do encji
        User user = convertToUser(userDTO);

        // Obsługa roli
        Role role = roleRepository.findByRoleType(roleType)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono roli: " + roleType));
        user.setRoles(Set.of(role)); // Zakładając, że użytkownik ma tylko jedną rolę przy rejestracji

        // Zapis użytkownika
        User savedUser = userRepository.save(user);

        // Konwersja zapisanego użytkownika do DTO i zwrot
        return convertToUserDTO(savedUser);
    }
    private void updateUserFields(User user, UserDTO userDTO, boolean isAdmin, String password) {
        if (isAdmin && password != null && !password.isBlank()) {
            // Assuming password is hashed before saving
            user.setPasswordHash(password);
        }
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        // Email is not updated here based on the requirements
    }

    private void updateUserDataAndAddress(User user, UserDTO userDTO) {
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        // Email is not updated here based on the requirements

        Address address = Optional.ofNullable(userDTO.address())
                .map(dto -> {
                    Address existingAddress = user.getAddress() != null ? user.getAddress() : new Address();
                    existingAddress.setStreet(dto.street());
                    existingAddress.setCity(dto.city());
                    existingAddress.setPostalCode(dto.postalCode());
                    existingAddress.setCountry(dto.country());
                    return existingAddress;
                }).orElse(null);

        if (address != null && address.getId() == null) {
            address = addressRepository.save(address);
        }
        user.setAddress(address);
    }
}