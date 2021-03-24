package org.example.core;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.example.api.orderItem.OrderItemRegistration;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "order_item")
public class OrderItem {

    private Order order;
    private Product product;
    private String productTitle;
    private double price;
    private int amount;

    public OrderItem() {}

    public OrderItem(Product product, String productTitle, double price, int amount) {
        this.product = product;
        this.productTitle = productTitle;
        this.price = price;
        this.amount = amount;
    }

    public static OrderItem createFromRegistration(OrderItemRegistration registration) {
        return new OrderItem(
                new Product(registration.product.id),
                registration.product.title,
                registration.price,
                registration.amount
        );
    }

    @EmbeddedId
    public PrimaryKey getPrimaryKey() {
        return new PrimaryKey(this.product.id, this.order.id);
    }

    @SuppressWarnings("unused")
    public void setPrimaryKey(PrimaryKey ignored) {}

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", updatable = false, insertable = false)
    @JsonBackReference
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @OneToOne
    @JoinColumn(name = "product_id", updatable = false, insertable = false)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProduct_title() {
        return productTitle;
    }

    public void setProduct_title(String productTitle) {
        this.productTitle = productTitle;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Embeddable
    public static class PrimaryKey implements Serializable {

        private String productId;
        private String orderId;

        public PrimaryKey() {
        }

        public PrimaryKey(String productId, String orderId) {
            this.productId = productId;
            this.orderId = orderId;
        }

        public String getProduct_id() {
            return productId;
        }

        public void setProduct_id(String productId) {
            this.productId = productId;
        }

        public String getOrder_id() {
            return orderId;
        }

        public void setOrder_id(String orderId) {
            this.orderId = orderId;
        }

        @Override
        public int hashCode() {
            return this.productId.hashCode() + this.orderId.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof PrimaryKey) {
                return ((PrimaryKey) obj).productId.equalsIgnoreCase(this.productId)
                        && ((PrimaryKey) obj).orderId.equalsIgnoreCase(this.orderId);
            }
            return false;
        }
    }
}
