package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;

/**
 * Represents a medication inventory system.
 * Provides functionalities to manage medications, including adding, updating stock levels,
 * viewing inventory, and loading data from a CSV file. Handles low-stock alerts and
 * allows for copying inventory from another {@code MedicationBank}
 * @author Lee Kuan Rong, Lim Wee Keat, Tan Wen Rong, Yeoh Kai Wen
 * @version 1.0
 * @since 2024-11-18
 */
public class MedicationBank implements Serializable {

    private static final long serialVersionUID = 4996340102333033032L;
    public HashMap<String, Medication> inventory;

    /**
     * Constructs an empty {@code MedicationBank}.
     * Initializes an empty medication inventory.
     */
    public MedicationBank() {
        this.inventory = new HashMap<>();
    }

    /**
     * Initializes the medication bank by loading medications from a CSV file.
     */
    public void initialiseMedicineBank() {
        try {
            loadMedicationsFromCSV("src/application/Medicine_List.csv");
            System.out.println("Medications successfully loaded from CSV.");
        } catch (IOException e) {
            System.err.println("Error loading medications from CSV: " + e.getMessage());
        }
    }

    /**
     * Copies the inventory from another {@code MedicationBank}.
     * Clears the current inventory before copying.
     * Used for loading from Serialized file
     *
     * @param otherBank The {@code MedicationBank} to copy inventory from.
     */
    public void copyFrom(MedicationBank otherBank) {
        if (otherBank == null) {
            System.out.println("The provided MedicationBank is null. Copy operation aborted.");
            return;
        }

        this.inventory.clear();

        for (String medicationName : otherBank.inventory.keySet()) {
            Medication medication = otherBank.inventory.get(medicationName);
            this.inventory.put(medicationName, new Medication(
                medication.getName(),
                medication.getStockLevel(),
                medication.getExpirationDate(),
                medication.getLowStockThreshold()
            ));
        }

        System.out.println("Medication inventory loaded successfully from saved MedicationBank.");
    }

    /**
     * Loads medications from a CSV file into the inventory.
     *
     * @param filePath The path to the CSV file.
     * @throws IOException If an error occurs while reading the file.
     */
    private void loadMedicationsFromCSV(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
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

                Medication medication = new Medication(name, stockLevel, expirationDate, lowStockThreshold);
                addMedication(medication);
            }
        }
    }

    /**
     * Adds a medication to the inventory.
     *
     * @param medication The {@code Medication} object to add.
     */
    public void addMedication(Medication medication) {
        if (medication == null || medication.getName() == null || medication.getName().isEmpty()) {
            System.out.println("Invalid medication information.");
            return;
        }
        inventory.put(medication.getName(), medication);
        System.out.println(medication.getName() + " added with stock: " + medication.getStockLevel());
    }

    /**
     * Increases the stock of a specific medication.
     *
     * @param medicationName The name of the medication.
     * @param amount         The amount to increase.
     */
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

    /**
     * Decreases the stock of a specific medication.
     *
     * @param medicationName The name of the medication.
     * @param amount         The amount to decrease.
     */
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

    /**
     * Checks the stock level of a specific medication.
     *
     * @param medicationName The name of the medication.
     * @return The stock level of the medication, or 0 if the medication is not found.
     */
    public int checkStock(String medicationName) {
        Medication medication = inventory.get(medicationName);
        if (medication == null) {
            System.out.println("Medication not found.");
            return 0;
        }
        return medication.getStockLevel();
    }

    /**
     * Displays the entire medication inventory.
     */
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

    /**
     * Checks if the stock level of a medication is below its threshold and triggers an alert if true.
     *
     * @param medication The {@code Medication} object to check.
     */
    private void checkAndAlertForLowStock(Medication medication) {
        if (medication.isStockLow()) {
            System.out.println("ALERT: Low stock for " + medication.getName() + ". Current stock: "
                    + medication.getStockLevel() + ", Threshold: " + medication.getLowStockThreshold());
        }
    }
}
