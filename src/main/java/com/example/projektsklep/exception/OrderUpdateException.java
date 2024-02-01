package com.example.projektsklep.exception;

public class OrderUpdateException extends RuntimeException {

    public OrderUpdateException(Long orderId, String message) {
        super("Błąd podczas aktualizacji zamówienia o ID " + orderId + ": " + message);
    }
}
