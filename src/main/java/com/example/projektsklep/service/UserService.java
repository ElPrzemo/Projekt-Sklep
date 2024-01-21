package com.example.projektsklep.service;

import com.example.projektsklep.model.dto.UserDTO;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> findAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    public Optional<UserDTO> findUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToUserDTO);
    }

    public Optional<UserDTO> findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToUserDTO);
    }

    public Optional<UserDTO> findUserByUsername(String username) {
        return userRepository.findByUsernameIgnoreCaseContaining(username)
                .map(this::convertToUserDTO);
    }



    public UserDTO saveUser(UserDTO userDTO) {
        User user = convertToUser(userDTO);
        return convertToUserDTO(userRepository.save(user));
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        updateUser(existingUser, userDTO);
        return convertToUserDTO(userRepository.save(existingUser));
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO convertToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    private User convertToUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.id());
        user.setEmail(userDTO.email());
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        // Ustaw inne wymagane pola
        return user;
    }

    private void updateUser(User user, UserDTO userDTO) {
        user.setEmail(userDTO.email());
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        // Aktualizuj inne pola
    }
}