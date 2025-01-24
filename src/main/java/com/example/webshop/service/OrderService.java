package com.example.webshop.service;

import com.example.webshop.api.model.OrderDetails;
import com.example.webshop.api.model.User;
import com.example.webshop.repo.OrderRepo;
import com.example.webshop.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    public OrderDetails saveOrder(OrderDetails order) {
        return orderRepo.save(order);
    }

    public OrderDetails getOrder(Long id) {
        Optional<OrderDetails> order = orderRepo.findById(id);
        return order.orElse(null); // Return order or null if not found
    }

    public Iterable<OrderDetails> getOrders() {
        return orderRepo.findAll();
    }

    public OrderDetails updateOrder(OrderDetails order, Long id) {
        Optional<OrderDetails> updateOrder = orderRepo.findById(id);
        if (updateOrder.isEmpty())
            return null; // Order not found
        OrderDetails getUpdateOrder = updateOrder.get();
        getUpdateOrder.setOrderDate(order.getOrderDate());
        getUpdateOrder.setOrderStatus(order.getOrderStatus());
        getUpdateOrder.setCurrency(order.getCurrency());
        getUpdateOrder.setShippingAddress(order.getShippingAddress());
        getUpdateOrder.setPaymentMethod(order.getPaymentMethod());
        getUpdateOrder.setPaymentStatus(order.getPaymentStatus());
        getUpdateOrder.setTotalAmount(order.getTotalAmount());
        return orderRepo.save(getUpdateOrder);
    }

    public void deleteOrder(Long id) {
        System.out.println("Attempting to delete user with ID: " + id);
        orderRepo.deleteById(id);
    }
}
