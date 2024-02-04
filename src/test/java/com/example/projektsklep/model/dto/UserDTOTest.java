//package com.example.projektsklep.model.dto;
//
//import org.junit.Before;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class UserDTOTest {
//
//    private UserDTO userDTO;
//
//    @Before
//    public void setUp() {
//        // Przygotowanie testowego obiektu UserDTO
//        AddressDTO mockAddress = Mockito.mock(AddressDTO.class);
//
//        UserDTO userDTO = new UserDTO()
//                .email("jan.kowalski@example.com")
//                .firstName("Jan")
//                .lastName("Kowalski")
//                .phoneNumber(123456789) // Dodaj numer telefonu
//                .password("tajneHaslo") // Dodaj hasło
//                .address(mockAddress)
//                .build();
//    }
//}