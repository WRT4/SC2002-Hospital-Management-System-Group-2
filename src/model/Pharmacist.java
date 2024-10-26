package model;

import java.util.ArrayList;

public class Pharmacist extends User {
    private MedicationBank medicationBank;

    public Pharmacist(String id, String name, MedicationBank medicationBank) {
        super(id, name, "Pharmacist");
        this.medicationBank = medicationBank;
    }

    // Add a medication to the inventory
    public void addMedicationToInventory(Medication medication) {
        if (medication == null || medication.getName().isEmpty()) {
            System.out.println("Error: Medication name cannot be null or empty.");
            return;
        }
        medicationBank.addMedication(medication);
    }

    // Submit a replenishment request if stock is low
    public void submitReplenishmentRequest(String medicationName, int requestedAmount, Administrator administrator) {
        if (!medicationBank.inventory.containsKey(medicationName)) {
            System.out.println("Error: Medication not found in inventory.");
            return;
        }

        Medication medication = medicationBank.inventory.get(medicationName);
        if (medication.isStockLow()) {
            System.out.println("Replenishment request submitted for " + medicationName + " (current stock: " + medication.getStockLevel() + ")");
            administrator.receiveReplenishmentRequest(medicationName, requestedAmount);
        } else {
            System.out.println("Stock is sufficient for " + medicationName);
        }
    }

    // View the medication inventory
    public void viewInventory() {
        medicationBank.viewInventory();
    }

    // Update prescription status for a given appointment
    public void updatePrescriptionStatus(Appointment appointment, String status) {
        try {
            if (appointment == null) {
                throw new NullPointerException("Appointment cannot be null.");
            }
            if (status == null || status.isEmpty()) {
                throw new IllegalArgumentException("Prescription status cannot be null or empty.");
            }

            // Assuming valid statuses are "pending" or "dispensed"
            if (!status.equalsIgnoreCase("pending") && !status.equalsIgnoreCase("dispensed")) {
                throw new IllegalArgumentException("Invalid prescription status: " + status);
            }

            appointment.setPrescriptionStatus(status); // In Appointment class
            System.out.println("Prescription status updated to: " + status);

        } catch (NullPointerException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while updating the prescription status.");
        }
    }

    // View appointment outcome records
    public void viewAppointmentOutcomeRecords(ArrayList<Appointment> appointments) {
        if (appointments == null || appointments.isEmpty()) {
            System.out.println("No appointment records available.");
            return;
        }
        System.out.println("Appointment Outcome Records:");
        for (Appointment appointment : appointments) {
            System.out.println("Appointment ID: " + appointment.getAppointmentID() +
                               ", Prescription: " + appointment.getPrescription() +
                               ", Status: " + appointment.getPrescriptionStatus());
        }
    }

    // Display menu options
    public void showMenu() {
        System.out.println("1. View Appointment Outcome Records");
        System.out.println("2. Update Prescription Status");
        System.out.println("3. View Medication Inventory");
        System.out.println("4. Add Medication to Inventory");
        System.out.println("5. Submit Replenishment Request");
        System.out.println("6. Logout");
    }
}


