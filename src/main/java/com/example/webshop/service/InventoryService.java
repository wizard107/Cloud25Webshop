package com.example.webshop.service;

import com.example.webshop.api.model.Inventory;
import com.example.webshop.repo.InventoryRepo;
import com.example.webshop.utility.ObjectUpdater;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepo inventoryRepo;

    @Autowired
    private EmailService emailService; // Inject EmailService

    private static final int LOW_STOCK_THRESHOLD = 5; // Set stock threshold


    // Save an inventory
    public Inventory saveInventory(Inventory inventory) {
        Inventory savedInventory = inventoryRepo.save(inventory);
        checkLowStock(savedInventory);
        return savedInventory;
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
        Inventory updatedInventory = inventoryRepo.save(existingInventory);

        checkLowStock(updatedInventory);
        return updatedInventory;
    }

    // Delete an inventory by ID
    public void deleteInventory(Long id) {
        System.out.println("Attempting to delete inventory with ID: " + id);
        inventoryRepo.deleteById(id);
    }

    private void checkLowStock(Inventory inventory) {
        if (inventory.getStock() < LOW_STOCK_THRESHOLD) {
            try {
                emailService.sendLowStockNotification(inventory);
            } catch (MessagingException e) {
                e.printStackTrace();
                System.err.println(" Failed to send low stock notification for: " + inventory.getId());
            }
        }
    }
}