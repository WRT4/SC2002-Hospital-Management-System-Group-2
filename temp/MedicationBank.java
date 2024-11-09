
package model;

import java.time.LocalDate;
import java.util.HashMap;

public class MedicationBank {
    public HashMap<String, Medication> inventory;

    public MedicationBank() {
        this.inventory = new HashMap<>();
        initializeMedications();  // Initialize with predefined medications
    }

    // Initialize predefined medications
    private void initializeMedications() {
        Medication paracetamol = new Medication("Paracetamol", 0, LocalDate.of(2025, 12, 31), 20);
        Medication ibuprofen = new Medication("Ibuprofen",  200, LocalDate.of(2024, 6, 30), 10);
        Medication amoxicillin = new Medication("Amoxicillin",  250, LocalDate.of(2026, 5, 15), 15);

        addMedication(paracetamol);
        addMedication(ibuprofen);
        addMedication(amoxicillin);
    }

    // Add a medication to the inventory
    public void addMedication(Medication medication) {
        if (medication == null || medication.getName() == null || medication.getName().isEmpty()) {
            System.out.println("Invalid medication information.");
            return;
        }
        inventory.put(medication.getName(), medication);
        System.out.println(medication.getName() + " added with stock: " + medication.getStockLevel());
    }

    // Update stock for a medication
    public void increaseStock(String medicationName, int amount) {
        Medication medication = inventory.get(medicationName);
        if (medication == null) {
            System.out.println("Medication not found in the inventory.");
            return;
        }
        medication.addStock(amount);
        System.out.println(medicationName + " stock updated. New stock level: " + medication.getStockLevel());
        checkAndAlertForLowStock(medication);  // Check for low stock after update
    }
    
    public void decreaseStock(String medicationName, int amount) {
        Medication medication = inventory.get(medicationName);
        if (medication == null) {
            System.out.println("Medication not found in the inventory.");
            return;
        }
        	medication.minusStock(amount);
        System.out.println(medicationName + " stock updated. New stock level: " + medication.getStockLevel());
        checkAndAlertForLowStock(medication);  // Check for low stock after update
    }

    // Check stock level for a specific medication
    public int checkStock(String medicationName) {
        Medication medication = inventory.get(medicationName);
        if (medication == null) {
            System.out.println("Medication not found.");
            return 0;
        }
        return medication.getStockLevel();
    }

    // View the entire medication inventory
    public void viewInventory() {
        if (inventory.isEmpty()) {
            System.out.println("The inventory is currently empty.");
            return;
        }
        System.out.println("Current Medication Inventory:");
        for (Medication medication : inventory.values()) {
            System.out.println(medication);  // Calls toString() method of Medication
        }
    }

    // Check if any medication stock is below the threshold and send an alert
    private void checkAndAlertForLowStock(Medication medication) {
        if (medication.isStockLow()) {
            System.out.println("ALERT: Low stock for " + medication.getName() + ". Current stock: " 
                                + medication.getStockLevel() + ", Threshold: " + medication.getLowStockThreshold());
            // Here you can notify the administrator or send an alert.
        }
    }
}

