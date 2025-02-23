package com.example.webshop.service;

import com.example.webshop.api.model.Image;
import com.example.webshop.api.model.Product;
import com.example.webshop.repo.ImageRepo;
import com.example.webshop.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageService {

    @Autowired
    private GcsService gcsService;

    @Autowired
    private ImageRepo imageRepo;

    @Autowired
    private ProductRepo productRepo;

    public Image uploadImage(MultipartFile file, int position, Product product) throws IOException {
        if (position < 1 || position > 3) {
            throw new IllegalArgumentException("Position must be 1, 2, or 3");
        }

        Image existingImage = imageRepo.findByProductAndPosition(product, position);
        if (existingImage != null) {
            gcsService.deleteImage(existingImage.getGcsObjectName());
            imageRepo.delete(existingImage);
        }


        String objectName = "products/" + product.getId() + "/image" + position + ".jpg";


        gcsService.uploadImage(file, objectName);


        Image image = new Image(objectName, position, product);
        return imageRepo.save(image);
    }

    public void deleteImage(Long imageId) {
        Image image = imageRepo.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));


        gcsService.deleteImage(image.getGcsObjectName());


        imageRepo.delete(image);
    }


    public String getImageUrl(Long imageId) {
        Image image = imageRepo.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        return gcsService.getPublicUrl(image.getGcsObjectName());
    }
}