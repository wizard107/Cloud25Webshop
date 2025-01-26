package com.example.webshop.service;

import com.example.webshop.api.model.OrderItem;
import com.example.webshop.repo.OrderItemRepo;
import com.example.webshop.utility.ObjectUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepo orderItemRepo;

    public OrderItem saveOrderItem(OrderItem orderItem) {
        return orderItemRepo.save(orderItem);
    }

    public OrderItem getOrderItem(Long id) {
        Optional<OrderItem> orderItem = orderItemRepo.findById(id);
        return orderItem.orElse(null); // Return orderItem or null if not found
    }

    public List<OrderItem> getOrderItemsByOrderId(Long id) {
        return orderItemRepo.findByOrderId(id);
    }

    public Iterable<OrderItem> getOrderItems() {
        return orderItemRepo.findAll();
    }


    public OrderItem updateOrderItem(OrderItem orderItem, Long id) {
        Optional<OrderItem> updateOrderItem = orderItemRepo.findById(id);
        if (updateOrderItem.isEmpty())
            return null; // User not found
        OrderItem existingOrderItem = updateOrderItem.get();
        ObjectUpdater.updateNonNullFields(orderItem, existingOrderItem);
        return orderItemRepo.save(existingOrderItem);
    }

    public void deleteOrderItem(Long id) {
        System.out.println("Attempting to delete orderitem with ID: " + id);
        orderItemRepo.deleteById(id);
    }
}
