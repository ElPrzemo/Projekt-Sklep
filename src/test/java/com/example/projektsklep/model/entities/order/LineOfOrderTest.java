//package com.example.projektsklep.model.entities.order;
//
//import com.example.projektsklep.model.entities.adress.Address;
//import com.example.projektsklep.model.entities.product.Author;
//import com.example.projektsklep.model.entities.product.Category;
//import com.example.projektsklep.model.entities.product.Product;
//import com.example.projektsklep.model.entities.role.Role;
//import com.example.projektsklep.model.entities.user.User;
//import com.example.projektsklep.model.enums.AdminOrUser;
//import com.example.projektsklep.model.enums.OrderStatus;
//import com.example.projektsklep.model.enums.ProductType;
//import com.example.projektsklep.model.notification.Email;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.Month;
//import java.util.Calendar;
//import java.util.GregorianCalendar;
//import java.util.List;
//import java.util.Set;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.mockito.Mockito.*;
//
//class LineOfOrderTest {
////    @Test
////    public void shouldReturnCorrectTotalPriceWhenQuantityIsOne() {
////        // Given
////        Product product = new Product();
////        LineOfOrder lineOfOrder = new LineOfOrder(product, 1);
////
////        // When
////        BigDecimal totalPrice = lineOfOrder.getTotalPrice();
////
////        // Then
////        assertThat(totalPrice).isEqualTo(BigDecimal.TEN);
////    }
//
//    @Test
//    public void shouldReturnCorrectTotalPriceWhenQuantityIsMoreThanOne() {
//        // Given
//        Product product = new Product();
//        LineOfOrder lineOfOrder = new LineOfOrder(product, 5);
//
//        // When
//        BigDecimal totalPrice = lineOfOrder.getTotalPrice();
//
//        // Then
//        assertThat(totalPrice).isEqualTo(BigDecimal.valueOf(50));
//    }
//
//    @Test
//    public void shouldHandleNullUnitPrice() {
//        // Given
//        Product product = new Product( null);
//        LineOfOrder lineOfOrder = new LineOfOrder(product, 3);
//
//        // When
//        BigDecimal totalPrice = lineOfOrder.getTotalPrice();
//
//        // Then
//        assertThat(totalPrice).isZero();
//    }
//}
//
//
//
