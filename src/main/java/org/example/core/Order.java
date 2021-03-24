package org.example.core;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.dropwizard.auth.Auth;
import org.example.api.order.OrderRegistration;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
@NamedQueries({
        @NamedQuery(
                name = "org.example.core.Order.findAll",
                query = "SELECT o FROM Order o"
        ),
        @NamedQuery(
                name = "org.example.core.Order.findByAccountId",
                query = "SELECT o FROM Order o where o.account.id = :accountId"
        ),
})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public String id;

    @OneToOne
    public Account account;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "primaryKey.order_id",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference
    public Set<OrderItem> orderItems;

    @Column(name = "created_at", insertable = false)
    public Timestamp date;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public OrderStatus status;

    public Order() {
    }

    public Order(Account account, Set<OrderItem> orderItem) {
        this.account = account;
        this.orderItems = orderItem;
        this.status = OrderStatus.CREATED;
    }

    public static Order createFromRegistration(@Auth Account account, OrderRegistration orderRegistration) {

        Set<OrderItem> orderItems = orderRegistration.orderItems
                .stream()
                .map(OrderItem::createFromRegistration)
                .collect(Collectors.toSet());

        return new Order(account, orderItems);
    }

    @SuppressWarnings("unused")
    public enum OrderStatus {
        CREATED, DELIVERING, DELIVERED
    }
}
