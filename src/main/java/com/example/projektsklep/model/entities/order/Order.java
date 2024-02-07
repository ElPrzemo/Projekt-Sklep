package com.example.projektsklep.model.entities.order;


import com.example.projektsklep.model.entities.adress.Address;
import com.example.projektsklep.model.entities.product.Product;
import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.enums.OrderStatus;
import com.example.projektsklep.model.notification.Observer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;

import java.awt.print.Pageable;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order implements Observable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // poniżej były problemy ze słowem zastrzeżonym user i musiałem zamienić accountHolder
    @ManyToOne
    @JoinColumn(name = "account_holder_id")
    private User accountHolder;


    @ManyToOne
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;
    @Enumerated(EnumType.ORDINAL)
    private OrderStatus orderStatus;

    @Column(name = "date_created")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateCreated;

    @Column(name = "sent_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate sentAt;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<LineOfOrder> lineOfOrders;

    private BigDecimal totalPrice;

    public void calculateTotalPrice() {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (LineOfOrder lineOfOrder : lineOfOrders) {
            totalPrice = totalPrice.add(lineOfOrder.getUnitPrice().multiply(new BigDecimal(lineOfOrder.getQuantity())));
        }
        this.totalPrice = totalPrice;
    }

    @Transient // Nie przechowujemy listy obserwatorów w bazie danych
    private List<Observer> registeredObservers = new ArrayList<>();

    @Override
    public void registerObserver(Observer observer) {
        registeredObservers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer observer) {
        registeredObservers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : registeredObservers) {
            observer.update(this);
        }
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        setOrderStatus(orderStatus);
        notifyObservers();
    }


    public void setUser(User user) {
        if (user != null) {
            this.accountHolder = user;
            // Opcjonalnie: pobierz i ustaw dodatkowe dane użytkownika
        }
    }

    public User getUser() {
        // Sprawdź, czy w obiekcie Order jest ustawiony User
        if (this.accountHolder != null) {
            return this.accountHolder;
        } else {
            // Pobierz użytkownika z innego źródła na podstawie identyfikatora lub podobnie
            // (wymaga implementacji dostępu do danych)
            return null;
        }
    }

    public void setListOfOrders(List<LineOfOrder> lineOfOrders) {
        // Przypisz otrzymaną listę do odpowiedniej zmiennej w Order
        this.lineOfOrders = lineOfOrders;
        // Opcjonalnie: aktualizuj inne pola związane z zamówieniem i produktami
    }

    public Address getAddress() {
        if (this.shippingAddress != null) {
            return this.shippingAddress;
        } else {
            // Opcjonalnie: pobierz adres z innego źródła i ustaw go dla Order
            return null;
        }
    }

    public List<Product> getProducts() {
        if (this.lineOfOrders != null) {
            List<Product> products = new ArrayList<>();
            for (LineOfOrder lineOfOrder : this.lineOfOrders) {
                products.add(lineOfOrder.getProduct());
            }
            return products;
        } else {
            // Opcjonalnie: pobierz listę produktów z innego źródła
            return null;
        }
    }

}