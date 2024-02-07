package com.example.projektsklep.model.notification;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.example.projektsklep.model.entities.order.Order;
import com.example.projektsklep.model.enums.OrderStatus;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class EmailTest {

    @Test
    public void testUpdate_OrderStatusChanged() {
        // Given
        Order mockOrder = mock(Order.class);
        when(mockOrder.getId()).thenReturn(123L);
        when(mockOrder.getOrderStatus()).thenReturn(OrderStatus.PENDING);

        // When
        verify(mockOrder, times(1)).changeOrderStatus(OrderStatus.SHIPPED);

        // Then
        verify(mockOrder, times(1)).changeOrderStatus(OrderStatus.SHIPPED);

        String expectedOutput = "E-mail - zamówienie numer: 123 zmieniło status na: WYSŁANE";
        String actualOutput = getCapturedOutput(mockOrder, expectedOutput); // Przekaż oczekiwany tekst
        assertEquals(expectedOutput, actualOutput);
    }

    // Metoda przechwytuje wyjście z Email
    private String getCapturedOutput(Order mockOrder, String expectedOutput) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PrintStream oldStream = System.out;
        System.setOut(new PrintStream(stream));

        Email email = new Email(mockOrder); // Użyj prawdziwego obiektu Email
        mockOrder.registerObserver(email);
        mockOrder.changeOrderStatus(OrderStatus.SHIPPED);

        System.setOut(oldStream);
        return stream.toString();
    }
//    @Test
//    public void testUpdate_OrderStatusChanged() {
//        // Given
//        Order mockOrder = Mockito.mock(Order.class);
//        Mockito.when(mockOrder.getId()).thenReturn(123L);
//        Mockito.when(mockOrder.getOrderStatus()).thenReturn(OrderStatus.PENDING);
//
//        // When
//       // Mockito.when(mockOrder.changeOrderStatus(OrderStatus.SHIPPED)).thenReturn(null); // Assuming changeOrderStatus returns void
//        mockOrder.changeOrderStatus(OrderStatus.SHIPPED);
//
//        // Then
//        Mockito.verify(mockOrder, times(1)).changeOrderStatus(OrderStatus.SHIPPED);
//
//        String expectedOutput = "E-mail - zamówienie numer: 123 zmieniło status na: WYSŁANE";
//        String actualOutput = getCapturedOutput(mockOrder, expectedOutput);
//        assertEquals(expectedOutput, actualOutput);
//    }
//
//    private String getCapturedOutput(Order mockOrder, String expectedOutput) {
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        PrintStream oldStream = System.out;
//        System.setOut(new PrintStream(stream));
//
//        Email email = new Email(mockOrder); // Use a real Email object
//        mockOrder.registerObserver(email);
//        mockOrder.changeOrderStatus(OrderStatus.SHIPPED); // Call the real method again
//
//        System.setOut(oldStream);
//        return stream.toString();
//    }
}
