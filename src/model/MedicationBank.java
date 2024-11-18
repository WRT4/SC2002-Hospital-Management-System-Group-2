package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;

public class MedicationBank implements Serializable{
	
    private static final long serialVersionUID = 4996340102333033032L;
	public HashMap<String, Medication> inventory;

    public MedicationBank() {
        this.inventory = new HashMap<>();
        try {
            loadMedicationsFromCSV("src/application/Medicine_List.csv");
            System.out.println("Medications successfully loaded from CSV.");
        } catch (IOException e) {
            System.err.println("Error loading medications from CSV: " + e.getMessage());
        }
    }
    
 // Method to copy inventory from another MedicationBank
    public void copyFrom(MedicationBank otherBank) {
        if (otherBank == null) {
            System.out.println("The provided MedicationBank is null. Copy operation aborted.");
            return;
        }

        // Clear the current inventory to avoid duplicates
        this.inventory.clear();

        // Copy the medications from the other bank's inventory
        for (String medicationName : otherBank.inventory.keySet()) {
            Medication medication = otherBank.inventory.get(medicationName);
            // Add the medication to the current inventory
            this.inventory.put(medicationName, new Medication(
                medication.getName(),
                medication.getStockLevel(),
                medication.getExpirationDate(),
                medication.getLowStockThreshold()
            ));
        }

        System.out.println("Medication inventory copied successfully from another MedicationBank.");
    }
    // Method to load medications from a CSV file
    private void loadMedicationsFromCSV(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip header row
                    continue;
                }

                String[] data = line.split(",");
                if (data.length < 3) {
                    System.out.println("Invalid data format: " + line);
                    continue;
                }

                String name = data[0].trim();
                int stockLevel = Integer.parseInt(data[1].trim());
                int lowStockThreshold = Integer.parseInt(data[2].trim());
                LocalDate expirationDate = null;

                // Set expiration dates based on predefined medications
                switch (name.toLowerCase()) {
                    case "paracetamol":
                        expirationDate = LocalDate.of(2025, 12, 31);
                        break;
                    case "ibuprofen":
                        expirationDate = LocalDate.of(2024, 6, 30);
                        break;
                    case "amoxicillin":
                        expirationDate = LocalDate.of(2026, 5, 15);
                        break;
                    default:                       
                        System.out.println("Unknown medication: " + name + ". No expiration date set.");
                        break;
                }

                // Create the Medication object
                Medication medication = new Medication(name, stockLevel, expirationDate, lowStockThreshold);
                addMedication(medication);
            }
        }
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
        checkAndAlertForLowStock(medication);
    }

    // Decrease stock for a medication
    public void decreaseStock(String medicationName, int amount) {
        Medication medication = inventory.get(medicationName);
        if (medication == null) {
            System.out.println("Medication not found in the inventory.");
            return;
        }
        medication.minusStock(amount);
        System.out.println(medicationName + " stock updated. New stock level: " + medication.getStockLevel());
        checkAndAlertForLowStock(medication);
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
            System.out.println(medication);
        }
    }

    // Check if any medication stock is below the threshold and send an alert
    private void checkAndAlertForLowStock(Medication medication) {
        if (medication.isStockLow()) {
            System.out.println("ALERT: Low stock for " + medication.getName() + ". Current stock: "
                    + medication.getStockLevel() + ", Threshold: " + medication.getLowStockThreshold());
        }
    }
}

