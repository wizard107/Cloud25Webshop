package com.example.webshop.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String gcsObjectName;
    private int position;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;


    public Image() {}

    public Image(String gcsObjectName, int position, Product product) {
        this.gcsObjectName = gcsObjectName;
        this.position = position;
        this.product = product;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGcsObjectName() {
        return gcsObjectName;
    }

    public void setGcsObjectName(String gcsObjectName) {
        this.gcsObjectName = gcsObjectName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}