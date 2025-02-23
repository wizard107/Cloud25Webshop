package com.example.webshop.repo;

import com.example.webshop.api.model.Image;
import com.example.webshop.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepo extends JpaRepository<Image, Long> {
    Image findByProductAndPosition(Product product, int position);
}