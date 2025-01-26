package com.example.webshop.repo;

import com.example.webshop.api.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {

    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId")
    List<OrderItem> findByOrderId(@Param("orderId") Long orderId);

    @Modifying
    @Transactional
    @Query("DELETE FROM OrderItem oi WHERE oi.order.id = :orderId")
    void deleteAllByOrderId(@Param("orderId") Long orderId);

    @Modifying
    @Transactional
    @Query("DELETE FROM OrderItem oi WHERE oi.product.id = :productId")
    void deleteAllByProductId(@Param("productId") Long productId);
}
