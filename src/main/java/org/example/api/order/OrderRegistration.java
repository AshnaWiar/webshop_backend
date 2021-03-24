package org.example.api.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.api.orderItem.OrderItemRegistration;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class OrderRegistration {

    @JsonProperty( required = true)
    @NotNull
    @NotEmpty
    @Valid
    public Set<OrderItemRegistration> orderItems;

}
