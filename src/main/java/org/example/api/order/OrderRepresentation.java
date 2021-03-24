package org.example.api.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.api.account.AccountRelationRepresentation;
import org.example.api.orderItem.OrderItemRepresentation;
import org.example.core.Order;
import org.example.core.Order.OrderStatus;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderRepresentation {

    @JsonProperty
    @NotNull
    public String id;

    @JsonProperty
    @NotNull
    @Valid
    public AccountRelationRepresentation account;

    @JsonProperty
    @NotNull
    @NotEmpty
    @Valid
    public Set<OrderItemRepresentation> orderItems;

    @JsonProperty
    @NotNull
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone="America/Phoenix")
    public Timestamp created_at;

    @JsonProperty
    @NotNull
    public OrderStatus status;

    @SuppressWarnings("unused")
    public OrderRepresentation() {
    }

    public OrderRepresentation(
            String id,
            AccountRelationRepresentation accountRepresentation,
            Set<OrderItemRepresentation> orderItems,
            Timestamp created_at,
            OrderStatus status) {
        this.id = id;
        this.account = accountRepresentation;
        this.orderItems = orderItems;
        this.status = status;
        this.created_at = created_at;
    }

    public static OrderRepresentation createFromOrder(Order order) {

        AccountRelationRepresentation account = new AccountRelationRepresentation(order.account.id);
        Set<OrderItemRepresentation> orderItems = order.orderItems.stream().map(OrderItemRepresentation::createFromOrderItem).collect(Collectors.toSet());

        return new OrderRepresentation(
                order.id,
                account,
                orderItems,
                order.date,
                order.status
        );
    }
}
