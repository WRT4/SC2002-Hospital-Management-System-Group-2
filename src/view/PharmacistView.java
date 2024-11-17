package view;

import java.util.Scanner;
import application.Database;

public class PharmacistView extends UserView{
	public PharmacistView(Scanner scanner){
		super(scanner);
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
