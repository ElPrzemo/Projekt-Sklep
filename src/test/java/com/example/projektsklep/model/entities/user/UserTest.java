package com.example.projektsklep.model.entities.user;

import com.example.projektsklep.model.entities.adress.Address;
import com.example.projektsklep.model.entities.order.Order;
import com.example.projektsklep.model.entities.role.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    public void testUserConstructor_GivenValidData_ShouldCreateUser() {
        // Given
        String email = "johndoe@example.com";
        String passwordHash = "passwordHash";
        String avatarPath = "avatarPath";
        String firstName = "John";
        String lastName = "Doe";

        // When
        User user = new User(email, passwordHash, avatarPath, firstName, lastName);

        // Then
        Assertions.assertEquals(email, user.getEmail());
        Assertions.assertEquals(passwordHash, user.getPasswordHash());
        Assertions.assertEquals(avatarPath, user.getAvatarPath());
        Assertions.assertEquals(firstName, user.getFirstName());
        Assertions.assertEquals(lastName, user.getLastName());
    }

    //    @Test
//    void testUserConstructor() {
//        User user = new User("johndoe@example.com", "passwordHash", "avatarPath", "John", "Doe");
//        Assertions.assertEquals("johndoe@example.com", user.getEmail());
//        Assertions.assertEquals("passwordHash", user.getPasswordHash());
//        Assertions.assertEquals("John", user.getFirstName());
//        Assertions.assertEquals("Doe", user.getLastName());
//    }
    @Test
    public void testAddOrder_GivenUserAndOrder_ShouldAddOrderToUser() {
        // Given
        User user = new User("johndoe@example.com", "passwordHash", "avatarPath", "John", "Doe");
        Order order = new Order();

        // When
        user.addOrder(order);

        // Then
        Assertions.assertEquals(1, user.getOrders().size());
        Assertions.assertTrue(user.getOrders().contains(order));
    }

//    @Test
//    void testAddOrder() {
//        User user = new User("johndoe@example.com", "passwordHash", "avatarPath", "John", "Doe");
//        Order order = new Order();
//
//        user.addOrder(order);
//        Assertions.assertEquals(1, user.getOrders().size());
//        Assertions.assertEquals(order, user.getOrders().iterator().next());
//    }

    @Test
    public void testSetAddress_GivenUserAndAddress_ShouldSetAddress() {
        // Given
        User user = new User("johndoe@example.com", "passwordHash", "avatarPath", "John", "Doe");
        Address address = new Address();

        // When
        user.setAddress(address);

        // Then
        Assertions.assertEquals(address, user.getAddress());
    }

//    @Test
//    void testSetAddress() {
//        User user = new User("johndoe@example.com", "passwordHash", "avatarPath", "John", "Doe");
//        Address address = new Address();
//
//        user.setAddress(address);
//        Assertions.assertEquals(address, user.getAddress());
//    }

    // @Test
//    void testGetRoles() {
//        User user = new User("johndoe@example.com", "passwordHash", "avatarPath", "John", "Doe");
//        Role role1 = new Role("ADMIN"); // Create role objects with appropriate names
//        Role role2 = new Role();
//
//        user.getRoles().add(role1);
//        user.getRoles().add(role2);
//
//        Assertions.assertEquals(2, user.getRoles().size());
//        Assertions.assertTrue(user.getRoles().contains(role1));
//        Assertions.assertTrue(user.getRoles().contains(role2));
//    }
}