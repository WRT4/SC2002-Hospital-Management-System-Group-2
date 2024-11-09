package model;

import java.util.Scanner;
import java.util.ArrayList;

public class Pharmacist extends User {
    //private MedicationBank medicationBank;
    private ArrayList<RefillRequest> requests;
    Scanner sc = new Scanner(System.in);

    public Pharmacist(String id, String name) {
        super(id, name, "Pharmacist");
        this.requests = new ArrayList<>();
    }
    
    public Pharmacist(String id, String name, String Password, String Role) {
    	super(id,name,Password,"Pharmacist");
    	this.requests = new ArrayList<>();
    }
    
    public Administrator getAdmin() {
        // Display available administrators
        for (Administrator admin : Database.administrators) {
            System.out.println(admin);
        }

        // Prompt user for Admin ID
        System.out.println("Enter Admin ID or -1 to go back: ");
        String id = sc.next().trim(); // Read trimmed input
        if (id.equals("-1")) return null; // Allow user to go back

        // Find the matching administrator
        for (Administrator admin : Database.administrators) {
            if (admin.getId().equals(id)) {
                return admin; // Return the found admin
            }
        }

        // If no match is found, notify the user
        System.out.println("Administrator not found!");
        return null; // Explicitly return null if admin is not found
    }
    
    public Patient getPatient() {
        // Display available administrators
        for (Patient pat : Database.patients) {
            System.out.println(pat);
        }

        // Prompt user for Admin ID
        System.out.println("Enter Patient ID or -1 to go back: ");
        String id = sc.next().trim(); // Read trimmed input
        if (id.equals("-1")) return null; // Allow user to go back

        // Find the matching administrator
        for (Patient pat : Database.patients) {
            if (pat.getId().equals(id)) {
                return pat; // Return the found admin
            }
        }

        // If no match is found, notify the user
        System.out.println("Patient not found!");
        return null; // Explicitly return null if admin is not found
    }


    private void viewMedicationInventory() {
        Database.medicationBank.viewInventory();
    }
    

    public void submitReplenishmentRequest() {
    	
    	System.out.print("Enter Medication Name for replenishment request: ");
	    String medicationName = sc.next();
	     if (!Database.medicationBank.inventory.containsKey(medicationName)) {
	            System.out.println("Error: Medication not found in inventory.");
	            return;
	        }
	    sc.nextLine();
	    System.out.print("Enter quantity to request: ");
	    int requestedAmount = sc.nextInt();
	    
	    Administrator admin = getAdmin();
	    if (admin == null) {
            System.out.println("Replenishment request canceled.");
            return; // Stop if no valid admin is selected
        }

        Medication medication = Database.medicationBank.inventory.get(medicationName);
        if (medication.isStockLow()) {
            RefillRequest request = new RefillRequest(medicationName, requestedAmount);
            requests.add(request);
            admin.receiveRefillRequest(request); // Use the Administrator instance
            System.out.println("Replenishment request submitted for " + medicationName);
        } else {
            System.out.println("Stock is sufficient for " + medicationName);
        }        
    }

   
    public void viewAppointmentOutcomeRecords() {
        Patient patient1 = getPatient();
        patient1.viewAppointmentOutcomes();
    }
    
    public void updatePrescriptionStatus () {        
        Patient patient1 = getPatient();
        patient1.viewAppointmentOutcomes();
        int apptChoice = 0;
        if (patient1.getAppointments().isEmpty())
        	return;
        System.out.println("Choose Appointment ID to update Prescription Status");
        apptChoice = sc.nextInt();
        Appointment selectedAppointment = null;
        for (Appointment appointment : patient1.getAppointments()) {
            if (appointment.getAppointmentID() == apptChoice) {
                selectedAppointment = appointment;
                break;
            }
        }

        // First, attempt to dispense medication before setting the status
        boolean dispensedSuccessfully = dispenseMedication(selectedAppointment);
        
        if (dispensedSuccessfully) {
            selectedAppointment.setPrescriptionStatus(Status.COMPLETED);
            System.out.println("Prescription status updated to COMPLETED for the selected appointment.");
        } else {
            System.out.println("Unable to update prescription status to COMPLETED due to dispensing issues.");
        }
    }

    // Function to attempt dispensing medication and return success status
    private boolean dispenseMedication(Appointment appointment) {
        boolean allMedicationsDispensed = true;  // Flag to track if all medications were dispensed successfully

        for (Medication prescribedMed : appointment.getPrescription()) {
            String medName = prescribedMed.getName();
            int requiredQuantity = prescribedMed.getDosage();
            
            if (!Database.medicationBank.inventory.containsKey(medName)) {
                System.out.println("Medication " + medName + " is not available in the inventory.");
                allMedicationsDispensed = false;
                continue;
            }
            
            Medication inventoryMed = Database.medicationBank.inventory.get(medName);
            
            if (inventoryMed.getStockLevel() < requiredQuantity) {
                System.out.println("Insufficient stock for " + medName + ". Unable to dispense.");
                allMedicationsDispensed = false;
            } else {
                inventoryMed.minusStock(requiredQuantity);
                System.out.println("Dispensed " + requiredQuantity + " units of " + medName + ". Updated stock level: " + inventoryMed.getStockLevel());
            }
        }

        return allMedicationsDispensed;
    }

    public void showMenu() {
    	
    	int choice;
    	do {
    		System.out.println("Pharmacist Menu:");
    		System.out.println("1. View Appointment Outcome Record");
    		System.out.println("2. Update Prescription Status");
    		System.out.println("3. View Medication Inventory");
    		System.out.println("4. Submit Replenishment Request");
    		System.out.println("5. Logout");
    		System.out.println("Choose an action: ");
    		choice = sc.nextInt();
    		sc.nextLine();
    		
    		switch (choice) {
    		case 1:
    			viewAppointmentOutcomeRecords();
    			break;
    		case 2:
    			updatePrescriptionStatus();
    			break;
    		case 3: 			
    			viewMedicationInventory();
    			break;
    		case 4: 
    		    submitReplenishmentRequest(); 
    		    break;
    		case 5:
    			System.out.println("Logging out...");
    			break;
    		default:
    			System.out.println("Invalid choice. Please try again.");
    		}
    	}while (choice !=5);
    }	
    
    public String toString() {
    	return "Pharmacist ID: " + getId() + " Name: " + getName();
    }
    
    public String getId() {
    	return this.id;
    }
    
    public String getName() {
    	return this.name;
    }
}


