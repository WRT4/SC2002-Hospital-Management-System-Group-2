package view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import application.Database;
import enums.Status;
import model.Administrator;
import model.Appointment;
import model.Doctor;
import model.Medication;
import model.Patient;
import model.Pharmacist;
import model.RefillRequest;
import model.User;

/**
 * Represents the user interface for administrators in the medical system.
 * Handles input and output operations specific to administrator functions.
 * @author Tan Wen Rong, Yeoh Kai Wen
 * @version 1.0
 * @since 2024-11-18
 */
public class AdministratorView extends UserView {
    
    /**
     * Constructs an AdministratorView object with a given scanner for input.
     *
     * @param scanner The Scanner instance for user input
     */
    public AdministratorView(Scanner scanner) {
        super(scanner);
    }

    /**
     * Displays the administrator menu options.
     */
    public void showMenu() {
        System.out.println("\nAdministrator Menu:");
        System.out.println("1. View Inbox");
        System.out.println("2. View and Manage Hospital Staff");
        System.out.println("3. View Appointments Details");
        System.out.println("4. View and Manage Medication Inventory");
        System.out.println("5. View and Approve Replenishment Requests");
        System.out.println("6. View Logs");
        System.out.println("7. Unlock Accounts");
        System.out.println("8. Exit to Main Menu");
    }

    /**
     * Displays pending refill requests.
     *
     * @param refillRequests The list of refill requests
     */
    public void viewPendingRefillRequests(List<RefillRequest> refillRequests) {
        int num = 0;
        System.out.println("Pending Refill Requests:");
        for (RefillRequest request : refillRequests) {
            if (request.getStatus() == Status.PENDING) {
                System.out.println(request);
                num++;
            }
        }
        if (num == 0) {
            System.out.println("No pending refill requests.");
        }
    }

    /**
     * Displays completed refill requests.
     *
     * @param refillRequests The list of refill requests
     */
    public void viewCompletedRefillRequests(List<RefillRequest> refillRequests) {
        int num = 0;
        System.out.println("Completed Refill Requests:");
        for (RefillRequest request : refillRequests) {
            if (request.getStatus() == Status.APPROVED) {
                System.out.println(request);
                num++;
            }
        }
        if (num == 0) {
            System.out.println("No completed refill requests.");
        }
    }

    /**
     * Displays the menu for viewing and managing refill requests.
     */
    public void viewAndManageRefillRequestsMenu() {
        System.out.println("1. View Pending Refill Requests");
        System.out.println("2. View Completed Refill Requests");
        System.out.println("3. Approve Pending Refill Requests");
        System.out.println("4. Exit");
    }

    /**
     * Displays the medication bank inventory.
     */
    public void viewMedBankInventory() {
        if (Database.MEDICATION_BANK != null) {
            Database.MEDICATION_BANK.viewInventory();
        } else {
            System.out.println("Medication bank is not initialized.");
        }
    }

    /**
     * Adds a new medication to the medication bank.
     */
    public void addNewMed() {
        System.out.println("Enter name of new medicine: ");
        String name = scanner.next();
        if (Database.MEDICATION_BANK.inventory.containsKey(name)) {
            System.out.println("Medication already exists.");
            return;
        } else {
            System.out.println("Enter stock level");
            int x = scanner.nextInt();
            System.out.println("How long until it expires? (in years)");
            int y = scanner.nextInt();
            System.out.println("What is the low stock threshold level?");
            int n = scanner.nextInt();
            Medication newMeds = new Medication(name, x, LocalDate.now().plusYears(y), n);
            Database.MEDICATION_BANK.addMedication(newMeds);
        }
    }

    /**
     * Displays all appointments.
     */
    public void displayAppointments() {
        boolean hasAppointments = false;
        for (Patient patient : Database.PATIENTS) {
            if (!patient.getAppointments().isEmpty()) {
                System.out.println("Appointments Details:");
                hasAppointments = true;
                for (Appointment appointment : patient.getAppointments()) {
                    System.out.println(appointment.getDetails());
                }
            }
        }
        if (!hasAppointments) {
            System.out.println("No appointments available.");
        }
    }

    /**
     * Displays the menu for viewing and managing hospital staff.
     */
    public void viewAndManageStaffMenu() {
        System.out.println("1. View Hospital Staff");
        System.out.println("2. Manage Hospital Staff");
        System.out.println("3. Exit");
    }

    /**
     * Displays all hospital staff.
     */
    public void displayStaff() {
        if (Database.getTotalStaffCount() == 0) {
            System.out.println("No staff members available.");
        } else {
            System.out.println("Hospital Staff:");
            for (Doctor doctor : Database.DOCTORS) {
                System.out.println("ID: " + doctor.getID() + " Name: " + doctor.getName() + " Role: Doctor");
            }
            for (Pharmacist pharmacist : Database.PHARMACISTS) {
                System.out.println("ID: " + pharmacist.getID() + " Name: " + pharmacist.getName() + " Role: Pharmacist");
            }
            for (Administrator administrator : Database.ADMINISTRATORS) {
                System.out.println("ID: " + administrator.getID() + " Name: " + administrator.getName() + " Role: Administrator");
            }
        }
    }

    /**
     * Displays the menu for managing hospital staff.
     */
    public void manageStaffMenu() {
        System.out.println("1. Add Staff Member");
        System.out.println("2. Remove Staff Member");
        System.out.println("3. Return");
    }

    /**
     * Prompts the user to enter the role of a staff member.
     *
     * @return The role entered by the user
     */
    public String enterRole() {
        System.out.println("Enter Role (Doctor, Pharmacist, Administrator): ");
        String role = scanner.nextLine().trim().toLowerCase();
        return role;
    }

    /**
     * Prompts the user to enter a staff ID.
     *
     * @return The staff ID entered by the user
     */
    public String enterStaffID() {
        System.out.print("Enter Staff ID: ");
        String id = scanner.nextLine();
        return id;
    }

    /**
     * Prompts the user to enter a staff name.
     *
     * @return The staff name entered by the user
     */
    public String enterName() {
        System.out.print("Enter Staff Name: ");
        String name = scanner.nextLine();
        return name;
    }

    /**
     * Displays the system logs.
     */
    public void viewSystemLogs() {
        System.out.println("Viewing system logs...\n");
        for (String log : Database.SYSTEM_LOGS) {
            System.out.println(log);
        }
    }

    /**
     * Displays locked accounts and prompts the user to unlock an account.
     *
     * @param lockedAccounts The list of locked accounts
     */
    public void viewLockedAccounts(ArrayList<User> lockedAccounts) {
        System.out.println("\nViewing locked accounts...");
        for (User user : lockedAccounts) {
            System.out.println(user.getID());
        }
        System.out.println("\nWhich account would you like to unlock? Enter ID or -1 to exit");
    }
}
