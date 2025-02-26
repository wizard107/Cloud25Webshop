package com.example.webshop.api.controller;

import com.example.webshop.api.model.OrderDetails;
import com.example.webshop.api.model.dto.OrderDetailsDTO;
import com.example.webshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/webshop/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/save")
    public OrderDetails saveOrder(@RequestBody OrderDetailsDTO order) throws Exception {
        return orderService.saveOrder(order);
    }

    @GetMapping("/{id}")
    public OrderDetails getOrder(@PathVariable("id") Long id) {
        return orderService.getOrder(id);
    }

    @GetMapping("/getAll")
    public Iterable<OrderDetails> getOrders() {
        return orderService.getOrders();
    }

    @PostMapping(value = "/update/{id}")
    public OrderDetails updateOrder(@RequestBody OrderDetails order, @PathVariable(name = "id") Long id) {
        return orderService.updateOrder(order, id);
    }

    @DeleteMapping(value = "/delete/{id}")
    public void deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteOrder(id);
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }

}
