package model;

import java.util.Scanner;

public class Pharmacist extends User {
    private MedicationBank medicationBank;
    private Administrator administrator;
    Scanner sc = new Scanner(System.in);

    public Pharmacist(String id, String name, MedicationBank medicationBank, Administrator administrator) {
        super(id, name, "Pharmacist");
        this.medicationBank = medicationBank;
        this.administrator = administrator; // Initialize Administrator
    }

    private void viewMedicationInventory() {
        medicationBank.viewInventory();
    }
    public void submitReplenishmentRequest(String medicationName, int requestedAmount) {
        if (!medicationBank.inventory.containsKey(medicationName)) {
            System.out.println("Error: Medication not found in inventory.");
            return;
        }

        Medication medication = medicationBank.inventory.get(medicationName);
        if (medication.isStockLow()) {
            RefillRequest request = new RefillRequest(medicationName, requestedAmount);
            administrator.receiveRefillRequest(request); // Use the Administrator instance
            System.out.println("Replenishment request submitted for " + medicationName);
        } else {
            System.out.println("Stock is sufficient for " + medicationName);
        }
    }

    public void viewAppointmentOutcomeRecords() {
        for (Patient patient: Database.patients){
            System.out.println(patient);
        }
        int patientChoice = 0;
        System.out.println("Choose Patient ID to view Appointment Outcome");
        patientChoice = sc.nextInt();
        if (patientChoice !=1 && patientChoice !=2 ){
            System.out.println("Invalid Input of patient");
        }
        Database.patients.get(patientChoice-1).viewAppointmentOutcomes();
    }
    
    
    
    public void updatePrescriptionStatus () {
        for (Patient patient: Database.patients){
            System.out.println(patient);
        }
        int patientChoice = 0;
        System.out.println("Choose Patient ID to update Prescription Status");
        patientChoice = sc.nextInt();
        if (patientChoice !=1 && patientChoice !=2 ){
            System.out.println("Invalid Input of patient");
        }
        Database.patients.get(patientChoice-1).viewAppointmentOutcomes();
        int apptChoice = 0;
        if (Database.patients.get(patientChoice - 1).getAppointments().isEmpty())
        	return;
        System.out.println("Choose Appointment ID to update Prescription Status");
        apptChoice = sc.nextInt();
       /* if (apptChoice < 1 || apptChoice > Database.patients.get(patientChoice-1).getAppointments().size()) {
            System.out.println("Error occurred. Please enter a valid number. BLAH");
            return;
        }*/
        //Appointment selectedAppointment = Database.patients.get(patientChoice - 1).getAppointments().get(apptChoice - 1);
        Appointment selectedAppointment = null;
        for (Appointment appointment : Database.patients.get(patientChoice-1).getAppointments()) {
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
            
            if (!medicationBank.inventory.containsKey(medName)) {
                System.out.println("Medication " + medName + " is not available in the inventory.");
                allMedicationsDispensed = false;
                continue;
            }
            
            Medication inventoryMed = medicationBank.inventory.get(medName);
            
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
    	/*System.out.println("What is the prescription status?");
     	String stat = sc.next().trim();
    	Status status = Status.valueOf(stat.toUpperCase());
        
        selectedAppointment.setPrescriptionStatus(status);
        System.out.println("Prescription status updated successfully for the selected appointment.");*/

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
    		    System.out.print("Enter Medication Name for replenishment request: ");
    		    String medicationName = sc.nextLine();
    		    System.out.print("Enter quantity to request: ");
    		    int requestedAmount = sc.nextInt();
    		    sc.nextLine(); // consume newline
    		    // Assuming you have an Administrator object already created or passed into this method
    		    submitReplenishmentRequest(medicationName, requestedAmount); 
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
    // Other methods unchanged
}




