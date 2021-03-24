package org.example.api.orderItem;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.api.product.ProductRelationRegistration;
import org.example.core.OrderItem;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class OrderItemRepresentation {

    @JsonProperty
    @NotNull
    @Valid
    public ProductRelationRegistration product;

    @JsonProperty
    @NotNull
    public String productTitle;

    @JsonProperty
    @NotNull
    @DecimalMin("0.1")
    public double price;

    @JsonProperty
    @Min(1)
    public int amount;

    public OrderItemRepresentation() {}

    public OrderItemRepresentation(ProductRelationRegistration product, String productTitle, double price, int amount) {
        this.product = product;
        this.productTitle = productTitle;
        this.price = price;
        this.amount = amount;
    }

    public static OrderItemRepresentation createFromOrderItem(OrderItem orderItem) {
        return new OrderItemRepresentation(
                new ProductRelationRegistration(orderItem.getProduct().id, orderItem.getProduct_title()),
                orderItem.getProduct_title(),
                orderItem.getPrice(),
                orderItem.getAmount()
        );
    }
}
