package controller;
 

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

import application.Database;
import enums.Status;
import model.Administrator;
import model.Appointment;
import model.Medication;
import model.Patient;
import model.Pharmacist;
import model.RefillRequest;
import view.PatientView;
import view.PharmacistView;
import view.AppointmentView;

public class PharmacistController extends SessionController {
	
	private PharmacistView pharmacistView;
	private Pharmacist pharmacist;
	
	public PharmacistController(Pharmacist p, Scanner s) {
		pharmacist = p;
		this.scanner = s;
		pharmacistView = new PharmacistView(scanner);
		unreadIndex = p.getUnreadIndex();
		startTime = LocalTime.now();
		startDate = LocalDate.now();
	}
	
	public void showMenu() {
    	int choice;
    	do {
    		pharmacistView.showMessageBox(pharmacist.getMessages(), unreadIndex);
    		pharmacistView.showMenu();
    		choice = pharmacistView.getChoice();
    		scanner.nextLine();
    		switch (choice) {
    		case 1:
    			unreadIndex = pharmacistView.viewInbox(pharmacist.getMessages(), unreadIndex);
				pharmacist.setUnreadIndex(unreadIndex);
        		break;
    		case 2:
    			Patient patient = getPatient();
    			if (patient == null) {
    	            System.out.println("No valid patient selected. Returning to menu.");
    	            break;
    	        }
    			PatientView.viewAppointmentOutcomes(patient.getAppointments());
    			break;
    		case 3:
    			updatePrescriptionStatus();
    			break;
    		case 4: 			
    			pharmacistView.viewMedicationInventory();
    			break;
    		case 5: 
    		    submitReplenishmentRequest(); 
    		    break;
    		case 6:
    			System.out.println("Exiting Pharmacist menu...");
    			String log = "Pharmacist " + pharmacist.getID() + " accessed system from " + startTime.format(FORMATTER) + " on " + startDate + " to " + LocalTime.now().format(FORMATTER) + " on " + LocalDate.now(); 
                Database.SYSTEM_LOGS.add(log);
    			break;
    		default:
    			System.out.println("Invalid choice. Please try again.");
    		}
    	} while (choice !=6);
    }	
	
	public Patient getPatient() {
        // Display available administrators
        for (Patient pat : Database.PATIENTS) {
            System.out.println(pat);
        }

        // Prompt user for Admin ID
        System.out.println("Enter Patient ID or -1 to go back: ");
        String id = pharmacistView.enterID().trim(); // Read trimmed input
        if (id.equals("-1")) return null; // Allow user to go back

        // Find the matching administrator
        for (Patient pat : Database.PATIENTS) {
            if (pat.getID().equals(id)) {
                return pat; // Return the found admin
            }
        }

        // If no match is found, notify the user
        System.out.println("Patient not found!");
        return null; // Explicitly return null if admin is not found
    }
	
	public Administrator getAdmin() {
        // Display available administrators
        for (Administrator admin : Database.ADMINISTRATORS) {
            System.out.println(admin);
        }

        // Prompt user for Admin ID
        System.out.println("Enter Admin ID or -1 to go back: ");
        String id = pharmacistView.enterID().trim(); // Read trimmed input
        if (id.equals("-1")) return null; // Allow user to go back

        // Find the matching administrator
        for (Administrator admin : Database.ADMINISTRATORS) {
            if (admin.getID().equals(id)) {
                return admin; // Return the found admin
            }
        }

        // If no match is found, notify the user
        System.out.println("Administrator not found!");
        return null; // Explicitly return null if admin is not found
    }
	
	public void submitReplenishmentRequest() {
    	System.out.print("Enter Medication Name for replenishment request: ");
	    String medicationName = scanner.next();
	     if (!Database.MEDICATION_BANK.inventory.containsKey(medicationName)) {
	            System.out.println("Error: Medication not found in inventory.");
	            return;
	        }
	    scanner.nextLine();
	    System.out.print("Enter quantity to request: ");
	    int requestedAmount = scanner.nextInt();
	    
	    Administrator admin = getAdmin();
	    if (admin == null) {
            System.out.println("Replenishment request canceled.");
            return; // Stop if no valid admin is selected
        }

        Medication medication = Database.MEDICATION_BANK.inventory.get(medicationName);
        if (medication.isStockLow()) {
                if (pharmacist.getRequests() == null) {
                    pharmacist.setRequests(new ArrayList<>()); // Initialize if null
                }
            RefillRequest request = new RefillRequest(medicationName, requestedAmount, this.pharmacist, admin);
            pharmacist.getRequests().add(request);
            admin.receiveRefillRequest(request); // Use the Administrator instance
            System.out.println("Replenishment request submitted for " + medicationName);
            sendMessage(request);
        } else {
            System.out.println("Stock is sufficient for " + medicationName);
        }        
    }
	
    //Function to send message to admin to inform about replenishment request 
    public void sendMessage(RefillRequest r) {
    	String message;
    	message = "Request at " + r.getRequestDate() + ": Refill Request for " + r.getMedication() + " of amount: " +
    	r.getRequestedAmount();
    	r.getAdmin().getMessages().add(message);
    }
    
    public void updatePrescriptionStatus () {        
           // Curate all appointments with prescription status: pending into list
        ArrayList<Appointment> pendingAppointments = new ArrayList<>();
        for (Patient patient : Database.PATIENTS) {
            for (Appointment apt : patient.getAppointments()) {
                if (apt.getPrescriptionStatus() == Status.PENDING) {
                    pendingAppointments.add(apt);
                }
            }
        }

        // If no pending appointments are found, exit the method
        if (pendingAppointments.isEmpty()) {
            System.out.println("No appointments with pending prescription status found.");
            return;
        }

        // Display pending appointments
        System.out.println("Appointments with Pending Prescription Status:");
        for (Appointment apt : pendingAppointments) {
            AppointmentView.printAppointmentOutcome(apt); // Use AppointmentView method
        }

        // Prompt the user to select an appointment ID
        System.out.println("Choose Appointment ID to update Prescription Status:");
        int apptChoice = pharmacistView.getChoice();

        // Find the selected appointment
        Appointment selectedAppointment = null;
        for (Appointment apt : pendingAppointments) {
            if (apt.getAppointmentID() == apptChoice) {
                selectedAppointment = apt;
                break;
            }
        }

        if (selectedAppointment == null) {
            System.out.println("Invalid appointment ID selected.");
            return;
        }

        // First, attempt to dispense medication before setting the status
        boolean dispensedSuccessfully = dispenseMedication(selectedAppointment);
        
        if (dispensedSuccessfully) {
            selectedAppointment.setPrescriptionStatus(Status.DISPENSED);
            System.out.println("Prescription status updated to DISPENSED for the selected appointment.");
        } else {
            System.out.println("Unable to update prescription status to DISPENSED due to dispensing issues.");
        }
    }

    // Function to attempt dispensing medication and return success status
    private boolean dispenseMedication(Appointment appointment) {
        boolean allMedicationsDispensed = true;  // Flag to track if all medications were dispensed successfully

        for (Medication prescribedMed : appointment.getPrescriptions()) {
            String medName = prescribedMed.getName();
            int requiredQuantity = prescribedMed.getDosage();
            
            if (!Database.MEDICATION_BANK.inventory.containsKey(medName)) {
                System.out.println("Medication " + medName + " is not available in the inventory.");
                allMedicationsDispensed = false;
                continue;
            }
            
            Medication inventoryMed = Database.MEDICATION_BANK.inventory.get(medName);
            
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

}
