package com.example.webshop.service;

import com.example.webshop.api.model.Inventory;
import com.example.webshop.repo.InventoryRepo;
import com.example.webshop.utility.ObjectUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepo inventoryRepo;

    // Save an inventory
    public Inventory saveInventory(Inventory inventory) {
        return inventoryRepo.save(inventory);
    }

    // Get an inventory by ID
    public Inventory getInventory(Long id) {
        Optional<Inventory> inventory = inventoryRepo.findById(id);
        return inventory.orElse(null); // Return inventory or null if not found
    }

    // Get all inventories
    public Iterable<Inventory> getInventories() {
        return inventoryRepo.findAll();
    }

    // Update an inventory
    public Inventory updateInventory(Inventory inventory, Long id) {
        Optional<Inventory> updateInventory = inventoryRepo.findById(id);
        if (updateInventory.isEmpty())
            return null; // Inventory not found
        Inventory existingInventory = updateInventory.get();
        ObjectUpdater.updateNonNullFields(inventory, existingInventory);
        return inventoryRepo.save(existingInventory);
    }

    // Delete an inventory by ID
    public void deleteInventory(Long id) {
        System.out.println("Attempting to delete inventory with ID: " + id);
        inventoryRepo.deleteById(id);
    }
}