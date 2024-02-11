package com.example.projektsklep.model.entities.adress;

import com.example.projektsklep.model.entities.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {
    @Test
    public void testAddressConstructor_GivenValidData_ShouldCreateAddress() {
        // Given
        String street = "Aleje Ujazdowskie 13";
        String city = "Warszawa";
        String postalCode = "00-587";
        String country = "Polska";

        // When
        Address address = new Address(); // Use the no-argument constructor
        address.setStreet(street);
        address.setCity(city);
        address.setPostalCode(postalCode);
        address.setCountry(country);

        // Then
        Assertions.assertEquals(street, address.getStreet());
        Assertions.assertEquals(city, address.getCity());
        Assertions.assertEquals(postalCode, address.getPostalCode());
        Assertions.assertEquals(country, address.getCountry());
    }



}