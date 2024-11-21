package view;

import java.util.Scanner;
import application.Database;

/**
 * Represents the user interface for pharmacists in the medical system.
 * Handles input and output operations specific to pharmacist functions.
 * Provides methods to view medication inventory and display pharmacist menu options.
 * @author Lee Kuan Rong Shane, Tan Wen Rong
 * @version 1.0
 * @since 2024-11-18
 */
public class PharmacistView extends UserView {

    /**
     * Constructs a PharmacistView object with a given scanner for input.
     *
     * @param scanner The Scanner instance for user input
     */
    public PharmacistView(Scanner scanner) {
        super(scanner);
    }

    /**
     * Returns the value of ID input by user
     *
     */
    public String enterID(){
        String id = scanner.next();
        return id;
    }

    /**
     * Displays the medication inventory.
     */
    public void viewMedicationInventory() {
        Database.MEDICATION_BANK.viewInventory();
    }

    /**
     * Displays the pharmacist menu options.
     */
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
