package com.example.webshop.api.model;

import com.example.webshop.api.model.enums.OrderStatus;
import com.example.webshop.api.model.enums.PaymentMethod;
import com.example.webshop.api.model.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_details_id", nullable = false)
    private OrderDetails order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // Many-to-One relationship

    public OrderItem() {
    }

    public OrderItem(int quantity, OrderDetails order, Product product) {
        this.quantity = quantity;
        this.order = order;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public OrderDetails getOrder() {
        return order;
    }

    public void setOrder(OrderDetails order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
