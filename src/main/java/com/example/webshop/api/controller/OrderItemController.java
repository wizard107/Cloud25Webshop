package com.example.webshop.api.controller;

import com.example.webshop.api.model.OrderDetails;
import com.example.webshop.api.model.OrderItem;
import com.example.webshop.api.model.dto.OrderDetailsDTO;
import com.example.webshop.service.OrderItemService;
import com.example.webshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/webshop/orderItem")
public class OrderItemController {
    @Autowired
    private OrderItemService orderItemService;

    @PostMapping(value = "/save")
    public OrderItem saveOrder(@RequestBody OrderItem orderItem) {
        return orderItemService.saveOrderItem(orderItem);
    }

    @GetMapping("/{id}")
    public OrderItem getOrderItem(@PathVariable("id") Long id) {
        return orderItemService.getOrderItem(id);
    }

    @GetMapping("/order/{id}")
    public List<OrderItem> getOrderItemsByOrderId(@PathVariable("id") Long id) {
        return orderItemService.getOrderItemsByOrderId(id);
    }

    @GetMapping("/getAll")
    public Iterable<OrderItem> getOrderItems() {
        return orderItemService.getOrderItems();
    }

    @PostMapping(value = "/update/{id}")
    public OrderItem updateOrder(@RequestBody OrderItem orderItem, @PathVariable(name = "id") Long id) {
        return orderItemService.updateOrderItem(orderItem, id);
    }

    @DeleteMapping(value = "/delete/{id}")
    public void deleteOrder(@PathVariable("id") Long id) {
        orderItemService.deleteOrderItem(id);
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }

}
