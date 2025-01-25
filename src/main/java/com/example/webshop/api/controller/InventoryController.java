package com.example.webshop.api.controller;

import com.example.webshop.api.model.Inventory;
import com.example.webshop.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/webshop/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    // Save an inventory
    @PostMapping(value = "/save")
    public Inventory saveInventory(@RequestBody Inventory inventory) {
        return inventoryService.saveInventory(inventory);
    }

    // Get an inventory by ID
    @GetMapping("/{id}")
    public Inventory getInventory(@PathVariable("id") Long id) {
        return inventoryService.getInventory(id);
    }

    // Get all inventories
    @GetMapping("/getAll")
    public Iterable<Inventory> getInventories() {
        return inventoryService.getInventories();
    }

    // Update an inventory
    @PostMapping(value = "/update/{id}")
    public Inventory updateInventory(@RequestBody Inventory inventory, @PathVariable(name = "id") Long id) {
        return inventoryService.updateInventory(inventory, id);
    }

    // Delete an inventory by ID
    @DeleteMapping(value = "/delete/{id}")
    public void deleteInventory(@PathVariable("id") Long id) {
        inventoryService.deleteInventory(id);
    }

    // Health check endpoint
    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }
}