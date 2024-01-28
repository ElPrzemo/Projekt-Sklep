package com.example.projektsklep.model.entities.user;

import com.example.projektsklep.model.entities.adress.Address;
import com.example.projektsklep.model.entities.order.Order;
import com.example.projektsklep.model.entities.role.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserTest {
    @Test
    void testUserConstructor() {
        User user = new User("johndoe@example.com", "passwordHash", "avatarPath", "John", "Doe");
        Assertions.assertEquals("johndoe@example.com", user.getEmail());
        Assertions.assertEquals("passwordHash", user.getPasswordHash());
        Assertions.assertEquals("John", user.getFirstName());
        Assertions.assertEquals("Doe", user.getLastName());
    }

    @Test
    void testAddOrder() {
        User user = new User("johndoe@example.com", "passwordHash", "avatarPath", "John", "Doe");
        Order order = new Order();

        user.addOrder(order);
        Assertions.assertEquals(1, user.getOrders().size());
        Assertions.assertEquals(order, user.getOrders().iterator().next());
    }

    @Test
    void testSetAddress() {
        User user = new User("johndoe@example.com", "passwordHash", "avatarPath", "John", "Doe");
        Address address = new Address();

        user.setAddress(address);
        Assertions.assertEquals(address, user.getAddress());
    }

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

