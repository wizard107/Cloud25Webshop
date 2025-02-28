package com.example.webshop.service;

import com.example.webshop.api.model.OrderDetails;
import com.example.webshop.api.model.OrderItem;
import com.example.webshop.api.model.Product;
import com.example.webshop.api.model.User;
import com.example.webshop.api.model.dto.OrderDetailsDTO;
import com.example.webshop.api.model.dto.ProductDTO;
import com.example.webshop.repo.OrderItemRepo;
import com.example.webshop.repo.OrderRepo;
import com.example.webshop.repo.ProductRepo;
import com.example.webshop.repo.UserRepo;
import com.example.webshop.utility.ObjectDTOMapper;
import com.example.webshop.utility.ObjectUpdater;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private OrderItemRepo orderItemRepo;
    @Autowired
    private EmailService emailService;


    @Transactional
    public OrderDetails saveOrder(OrderDetailsDTO order) {
        OrderDetails newOrder = new OrderDetails();
        ObjectDTOMapper.updateNonNullFields(order, newOrder);
        User user = userRepo.findById(order.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        newOrder.setUser(user);
        newOrder = orderRepo.save(newOrder);
        List<Product> products = productRepo.findAllById(order.getProducts().stream().map(ProductDTO::getId).collect(Collectors.toSet()));
        List<OrderItem> items = new ArrayList<>();
        if(!order.getProducts().isEmpty()) {
            for(ProductDTO product : order.getProducts()) {
                OrderItem item = new OrderItem(product.getQuantity(), newOrder,
                        products.stream().filter(p -> Objects.equals(p.getId(), product.getId())).findFirst().
                                orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + product.getId())));

                items.add(item);
            }
            orderItemRepo.saveAll((items));

        }
        try {
            emailService.sendInvoiceEmail(items);
        } catch (MessagingException e) {
            // Log the error but don't prevent the order from being saved
            System.err.println("Failed to send invoice email: " + e.getMessage());
        }

        return newOrder;
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
        OrderDetails existingOrder = updateOrder.get();
        ObjectUpdater.updateNonNullFields(order, existingOrder);
        return orderRepo.save(existingOrder);
    }

    @Transactional
    public void deleteOrder(Long id) {
        System.out.println("Attempting to delete order with ID: " + id);
        orderItemRepo.deleteAllByOrderId(id);
        orderRepo.deleteById(id);
    }
}
