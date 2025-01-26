package com.example.webshop.repo;

import com.example.webshop.api.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM OrderItem oi WHERE oi.product.id = :productId")
    void deleteAllByProductId(@Param("productId") Long productId);
}
