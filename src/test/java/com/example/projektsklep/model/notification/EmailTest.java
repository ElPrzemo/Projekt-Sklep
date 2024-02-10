//package com.example.projektsklep.model.notification;
//
//import com.example.projektsklep.model.entities.adress.Address;
//import com.example.projektsklep.model.entities.order.LineOfOrder;
//import com.example.projektsklep.model.entities.order.Order;
//import com.example.projektsklep.model.entities.product.Author;
//import com.example.projektsklep.model.entities.product.Category;
//import com.example.projektsklep.model.entities.product.Product;
//import com.example.projektsklep.model.entities.role.Role;
//import com.example.projektsklep.model.entities.user.User;
//import com.example.projektsklep.model.enums.AdminOrUser;
//import com.example.projektsklep.model.enums.OrderStatus;
//import com.example.projektsklep.model.enums.ProductType;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.io.ByteArrayOutputStream;
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.Month;
//import java.util.Calendar;
//import java.util.GregorianCalendar;
//import java.util.List;
//import java.util.Set;
//
//import static org.mockito.Mockito.*;
//
//class EmailTest {
//    @Mock
//    Order order;
//    @Mock
//    ByteArrayOutputStream capturedOutput;
//    @InjectMocks
//    Email email;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testUpdate() {
//        when(order.getId()).thenReturn(Long.valueOf(1));
//        when(order.getOrderStatus()).thenReturn(OrderStatus.NEW_ORDER);
//
//        email.update(new Order(Long.valueOf(1), new User(Long.valueOf(1), "email", "firstName", "lastName", "passwordHash", new Address(Long.valueOf(1), "street", "city", "postalCode", "country", Long.valueOf(1)), 0, "avatarPath", Set.of(new Role(Integer.valueOf(0), AdminOrUser.ADMIN, Set.of(null))), new GregorianCalendar(2024, Calendar.FEBRUARY, 10, 12, 1).getTime(), new GregorianCalendar(2024, Calendar.FEBRUARY, 10, 12, 1).getTime(), Set.of(null)), new Address(Long.valueOf(1), "street", "city", "postalCode", "country", Long.valueOf(1)), OrderStatus.NEW_ORDER, LocalDate.of(2024, Month.FEBRUARY, 10), LocalDate.of(2024, Month.FEBRUARY, 10), List.of(new LineOfOrder(Long.valueOf(1), null, new Product(Long.valueOf(1), new Author("name"), new Category("name"), "title", "description", "miniature", new BigDecimal(0), ProductType.DEFAULT_TYPE, true, LocalDate.of(2024, Month.FEBRUARY, 10)), 0, new BigDecimal(0))), new BigDecimal(0), List.of(new Email(null))));
//    }
//
//    @Test
//    void testGetCapturedOutput() {
//        String result = email.getCapturedOutput();
//        Assertions.assertEquals("replaceMeWithExpectedResult", result);
//    }
//}
//
