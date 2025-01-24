package com.example.webshop.repo;

import com.example.webshop.api.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<OrderDetails, Long> {
}
