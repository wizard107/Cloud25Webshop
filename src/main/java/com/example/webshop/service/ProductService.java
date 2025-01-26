package com.example.webshop.service;

import com.example.webshop.api.model.Inventory;
import com.example.webshop.api.model.Product;
import com.example.webshop.repo.InventoryRepo;
import com.example.webshop.repo.OrderItemRepo;
import com.example.webshop.repo.ProductRepo;
import com.example.webshop.utility.ObjectUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private InventoryRepo inventoryRepo;

    // Save a product
    @Transactional
    public Product saveProduct(Product product) {
        if(product.getInventory() == null || product.getInventory().getId() == null) {
            Inventory inventory = new Inventory();
            ObjectUpdater.updateNonNullFields(product.getInventory(), inventory);
            inventory = inventoryRepo.save(inventory);
            product.setInventory(inventory);
        }
        return productRepo.save(product);
    }

    // Get a product by ID
    public Product getProduct(Long id) {
        Optional<Product> product = productRepo.findById(id);
        return product.orElse(null); // Return product or null if not found
    }

    // Get all products
    public Iterable<Product> getProducts() {
        return productRepo.findAll();
    }

    // Update a product
    public Product updateProduct(Product product, Long id) {
        Optional<Product> updateProduct = productRepo.findById(id);
        if (updateProduct.isEmpty())
            return null; // Product not found
        Product existingProduct = updateProduct.get();
        ObjectUpdater.updateNonNullFields(product, existingProduct);
        return productRepo.save(existingProduct);
    }

    // Delete a product by ID
    @Transactional
    public void deleteProduct(Long id) {
        System.out.println("Attempting to delete product with ID: " + id);
        orderItemRepo.deleteAllByProductId(id);
        inventoryRepo.deleteAllByProductId(id);
        productRepo.deleteById(id);
    }
}