package com.example.webshop.api.controller;

import com.example.webshop.api.model.Product;
import com.example.webshop.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/webshop/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(value = "/save", consumes = "multipart/form-data")
    public Product saveProduct(
            @RequestParam("product") String productJson,
            @RequestPart("images") List<MultipartFile> images
    ) throws IOException {
        logger.info("Received request to save product: {}", productJson);
        logger.info("Number of images received: {}", images.size());


        Product product = objectMapper.readValue(productJson, Product.class);
        logger.info("Deserialized product: {}", product);

        return productService.saveProduct(product, images);
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable("id") Long id) {
        return productService.getProduct(id);
    }

    @GetMapping("/getAll")
    public Iterable<Product> getProducts() {
        return productService.getProducts();
    }

    @PostMapping(value = "/update/{id}", consumes = "multipart/form-data")
    public Product updateProduct(
            @RequestParam("product") String productJson, // Accept the product as a JSON string
            @PathVariable(name = "id") Long id,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) throws IOException {
        // Deserialize the JSON string into a Product object
        Product product = objectMapper.readValue(productJson, Product.class);

        return productService.updateProduct(product, id, images);
    }

    // Delete a product by ID
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Successfully deleted product with ID: " + id);
    }
    // Health check endpoint
    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }
}