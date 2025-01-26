package com.example.webshop.api.model;

import com.example.webshop.api.model.enums.ProductCategories;
import jakarta.persistence.*;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private ProductCategories category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @Column(nullable = true)
    private Double price;


    private String imageId;

    private String color;
    private String material;
    @Column(nullable = true)
    private int height;
    @Column(nullable = true)
    private int width;
    @Column(nullable = true)
    private int depth;
    @Column(nullable = true)
    private int weight;


    public Product() {
    }


    public Product(Long id, String name, String description, ProductCategories category, Inventory inventory, Double price, String imageId, String color, String material, int height, int width, int depth, int weight) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.inventory = inventory;
        this.price = price;
        this.imageId = imageId;
        this.color = color;
        this.material = material;
        this.height = height;
        this.width = width;
        this.depth = depth;
        this.weight = weight;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductCategories getCategory() {
        return category;
    }

    public void setCategory(ProductCategories category) {
        this.category = category;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}