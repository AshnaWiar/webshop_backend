package org.example.api.orderItem;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.api.product.ProductRelationRegistration;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class OrderItemRegistration {

    @JsonProperty
    @NotNull
    @Valid
    public ProductRelationRegistration product;

    @JsonProperty
    @NotNull
    @DecimalMin("0.1")
    public double price;

    @JsonProperty
    @Min(1)
    public int amount;

}
