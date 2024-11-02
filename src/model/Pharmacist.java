package model;

public class Pharmacist extends User {
    private MedicationBank medicationBank;

    public Pharmacist(String id, String name, MedicationBank medicationBank) {
        super(id, name, "Pharmacist");
        this.medicationBank = medicationBank;
    }

    public void submitReplenishmentRequest(String medicationName, int requestedAmount, Administrator administrator) {
        if (!medicationBank.inventory.containsKey(medicationName)) {
            System.out.println("Error: Medication not found in inventory.");
            return;
        }
        
        Medication medication = medicationBank.inventory.get(medicationName);
        if (medication.isStockLow()) {
            RefillRequest request = new RefillRequest(medicationName, requestedAmount);  // Create refill request
            administrator.receiveRefillRequest(request);  // Updated to work with RefillRequest
            System.out.println("Replenishment request submitted for " + medicationName + " (current stock: " 
                               + medication.getStockLevel() + ")");
        } else {
            System.out.println("Stock is sufficient for " + medicationName);
        }
    }

    // Other methods unchanged
}



