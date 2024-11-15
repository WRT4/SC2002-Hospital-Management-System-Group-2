package controller;
 

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

import application.Database;
import enums.Status;
import model.Administrator;
import model.Appointment;
import model.Medication;
import model.Patient;
import model.Pharmacist;
import model.RefillRequest;
import view.PharmacistView;

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
    			Patient patient = pharmacistView.getPatient();
    			pharmacistView.viewAppointmentOutcomeRecords(patient);
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
    			String log = "Pharmacist " + pharmacist.getID() + " accessed system from " + startTime + " on " + startDate + " to " + LocalTime.now() + " on " + LocalDate.now(); 
                Database.systemLogs.add(log);
    			break;
    		default:
    			System.out.println("Invalid choice. Please try again.");
    		}
    	} while (choice !=6);
    }	
	
	public void submitReplenishmentRequest() {
    	System.out.print("Enter Medication Name for replenishment request: ");
	    String medicationName = scanner.next();
	     if (!Database.medicationBank.inventory.containsKey(medicationName)) {
	            System.out.println("Error: Medication not found in inventory.");
	            return;
	        }
	    scanner.nextLine();
	    System.out.print("Enter quantity to request: ");
	    int requestedAmount = scanner.nextInt();
	    
	    Administrator admin = pharmacistView.getAdmin();
	    if (admin == null) {
            System.out.println("Replenishment request canceled.");
            return; // Stop if no valid admin is selected
        }

        Medication medication = Database.medicationBank.inventory.get(medicationName);
        if (medication.isStockLow()) {
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
        Patient patient1 = pharmacistView.getPatient();
        pharmacistView.viewAppointmentOutcomeRecords(patient1);
        int apptChoice = 0;
        if (patient1.getAppointments().isEmpty())
        	return;
        System.out.println("Choose Appointment ID to update Prescription Status");
        apptChoice = pharmacistView.getChoice();
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

        for (Medication prescribedMed : appointment.getPrescriptions()) {
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

}
