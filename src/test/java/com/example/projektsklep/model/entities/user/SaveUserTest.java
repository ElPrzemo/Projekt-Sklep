//package com.example.projektsklep.model.entities.user;
//
//import com.example.projektsklep.model.dto.AddressDTO;
//import com.example.projektsklep.model.dto.UserDTO;
//import com.example.projektsklep.model.entity.User;
//import com.example.projektsklep.model.repository.UserRepository;
//import com.example.projektsklep.service.UserService;
//import com.example.projektsklep.util.UserDTOFactory;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.util.Optional;
//
//@SpringBootTest
//public class SaveUserTest {
//
//    @Autowired
//    private UserService userService;
//
//    @MockBean
//    private UserRepository userRepository;
//
//    @Test
//    public void testSaveUser_ZPoprawnymiDanymi_PowinienZapisaćUżytkownika() throws Exception {
//        // Dane testowe
//        UserDTO userDTO = UserDTOFactory.create("jan.kowalski@example.com", "Jan", "Kowalski", "tajneHaslo");
//        AddressDTO addressDTO = new AddressDTO.Builder()
//                .ulica("Aleje Ujazdowskie 13")
//                .miasto("Warszawa")
//                .kodPocztowy("00-587")
//                .kraj("Polska")
//                .build();
//
//        // Mocowanie zależności
//        User mockUser = Mockito.mock(User.class);
//        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(mockUser);
//
//        // Wywołanie testowanej metody
//        Long userId = userService.saveUser(userDTO, addressDTO);
//
//        // Weryfikacja wyników
//        Assertions.assertNotNull(userId, "Zwrócony identyfikator użytkownika nie powinien być null");
//
//        // Dodatkowe asercje w zależności od specyfiki logiki i zakresu testu:
//        // - Zweryfikuj, czy dane użytkownika są poprawnie zapisane w bazie danych
//        // - Sprawdź, czy adres jest powiązany z zapisanym użytkownikiem
//        // - Obsłużuj oczekiwane wyjątki lub błędy
//    }
//
//    @Test
//    public void testSaveUser_ZNiepoprawnymiDanymi_PowinienZgłosićWyjątek() {
//        // Dane testowe
//        UserDTO userDTO = UserDTOFactory.create("", "", "", ""); // Użyj pustych wartości dla nieprawidłowych danych
//        AddressDTO addressDTO = null;
//
//        // Wywołanie testowanej metody i weryfikacja
//        Assertions.assertThrows(IllegalArgumentException.class, () -> {
//            userService.saveUser(userDTO, addressDTO);
//        });
//
//        // Dodatkowe asercje w zależności od specyficznego oczekiwanego wyjątku
//        // - Zweryfikuj, czy wiadomość wyjątku odpowiada oczekiwanej treści
//    }
//}