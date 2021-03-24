package org.example.api.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import static org.example.core.Order.OrderStatus;

import javax.validation.constraints.NotNull;

public class OrderStatusChangeRequest {

    @JsonProperty
    @NotNull
    public String id;

    @JsonProperty
    public OrderStatus status;
}
