package view;

import java.util.Scanner;

import application.Database;
import model.Administrator;
import model.Patient;

public class PharmacistView extends UserView{
	public PharmacistView(Scanner scanner){
		super(scanner);
	}
	
	public Patient getPatient() {
        // Display available administrators
        for (Patient pat : Database.PATIENTS) {
            System.out.println(pat);
        }

        // Prompt user for Admin ID
        System.out.println("Enter Patient ID or -1 to go back: ");
        String id = scanner.next().trim(); // Read trimmed input
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
	
	public void viewAppointmentOutcomeRecords(Patient patient1) {
        PatientView.viewAppointmentOutcomes(patient1);
    }
	
	public Administrator getAdmin() {
        // Display available administrators
        for (Administrator admin : Database.ADMINISTRATORS) {
            System.out.println(admin);
        }

        // Prompt user for Admin ID
        System.out.println("Enter Admin ID or -1 to go back: ");
        String id = scanner.next().trim(); // Read trimmed input
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
	
	public void viewMedicationInventory() {
        Database.MEDICATION_BANK.viewInventory();
    }
	
	public void showMenu() {
		System.out.println("\nPharmacist Menu:");
		System.out.println("1. View Inbox");
		System.out.println("2. View Appointment Outcome Record");
		System.out.println("3. Update Prescription Status");
		System.out.println("4. View Medication Inventory");
		System.out.println("5. Submit Replenishment Request");
		System.out.println("6. Exit to Main Menu");
		System.out.println("Choose an action: ");
	}
}
