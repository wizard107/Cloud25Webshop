package com.example.webshop.api.controller;

import com.example.webshop.api.model.Product;
import com.example.webshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/webshop/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Save a product
    @PostMapping(value = "/save")
    public Product saveProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    // Get a product by ID
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable("id") Long id) {
        return productService.getProduct(id);
    }

    // Get all products
    @GetMapping("/getAll")
    public Iterable<Product> getProducts() {
        return productService.getProducts();
    }

    // Update a product
    @PostMapping(value = "/update/{id}")
    public Product updateProduct(@RequestBody Product product, @PathVariable(name = "id") Long id) {
        return productService.updateProduct(product, id);
    }

    // Delete a product by ID
    @DeleteMapping(value = "/delete/{id}")
    public void deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
    }

    // Health check endpoint
    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }
}