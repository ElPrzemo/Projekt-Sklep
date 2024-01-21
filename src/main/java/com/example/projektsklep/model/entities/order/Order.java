package com.example.projektsklep.model.entities.order;


import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.enums.OrderStatus;
import com.example.projektsklep.model.notification.Observer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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
}