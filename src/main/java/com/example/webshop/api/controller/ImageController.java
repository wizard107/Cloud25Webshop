package com.example.webshop.api.controller;

import com.example.webshop.api.model.Image;
import com.example.webshop.api.model.Product;
import com.example.webshop.repo.ProductRepo;
import com.example.webshop.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/webshop/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private ProductRepo productRepo;

    @PostMapping("/upload")
    public Image uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("position") int position,
            @RequestParam("productId") Long productId) throws IOException {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return imageService.uploadImage(file, position, product);
    }


    @DeleteMapping("/delete/{id}")
    public String deleteImage(@PathVariable Long id) {
        imageService.deleteImage(id);
        return "Image deleted successfully";
    }


    @GetMapping("/url/{id}")
    public String getImageUrl(@PathVariable Long id) {
        return imageService.getImageUrl(id);
    }
}