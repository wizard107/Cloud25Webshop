package com.example.webshop.service;

import com.example.webshop.api.model.Image;
import com.example.webshop.api.model.Inventory;
import com.example.webshop.api.model.Product;
import com.example.webshop.api.model.dto.ImageDTO;
import com.example.webshop.repo.InventoryRepo;
import com.example.webshop.repo.OrderItemRepo;
import com.example.webshop.repo.ProductRepo;
import com.example.webshop.utility.ObjectUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private InventoryRepo inventoryRepo;

    @Autowired
    private GcsService gcsService; // Inject GcsService

    // Save a product with images
    @Transactional
    public Product saveProduct(Product product, List<MultipartFile> images) throws IOException {
        logger.info("Saving product: {}", product);
        logger.info("Number of images to upload: {}", images.size());

        // Save the product to the database
        Product savedProduct = productRepo.save(product);
        logger.info("Product saved with ID: {}", savedProduct.getId());

        // Upload images to GCS and save their metadata
        List<Image> imageEntities = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            String objectName = "products/" + savedProduct.getId() + "/image" + (i + 1) + ".jpg";
            logger.info("Uploading image to GCS: {}", objectName);

            gcsService.uploadImage(images.get(i), objectName);

            Image image = new Image();
            image.setGcsObjectName(objectName);
            image.setPosition(i + 1);
            image.setProduct(savedProduct);
            imageEntities.add(image);
        }

        savedProduct.setImages(imageEntities);
        logger.info("Product and images saved successfully");

        return productRepo.save(savedProduct);
    }

    public Product getProduct(Long id) {
        Optional<Product> product = productRepo.findById(id);
        return product.orElse(null); // Return product or null if not found
    }

    public Iterable<Product> getProducts() {
        return productRepo.findAll();
    }

    public List<ImageDTO> getProductImages(Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return product.getImages().stream()
                .map(image -> new ImageDTO(
                        image.getId(),
                        gcsService.getPublicUrl(image.getGcsObjectName()),
                        image.getPosition()
                ))
                .collect(Collectors.toList());
    }

    public Product updateProduct(Product product, Long id, List<MultipartFile> images) {
        Optional<Product> updateProduct = productRepo.findById(id);
        if (updateProduct.isEmpty())
            return null; // Product not found
        Product existingProduct = updateProduct.get();
        ObjectUpdater.updateNonNullFields(product, existingProduct);
        return productRepo.save(existingProduct);
    }


    @Transactional
    public void deleteProduct(Long id) {
        System.out.println("Attempting to delete product with ID: " + id);


        Optional<Product> productOptional = productRepo.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();


            if (product.getImages() != null) {
                for (Image image : product.getImages()) {
                    gcsService.deleteImage(image.getGcsObjectName());
                }
            }
        }


        orderItemRepo.deleteAllByProductId(id);
        inventoryRepo.deleteAllByProductId(id);

        productRepo.deleteById(id);
    }
}