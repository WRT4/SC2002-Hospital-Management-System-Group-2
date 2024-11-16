package view;

import java.time.LocalDate;
import java.util.ArrayList;
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

public class AdministratorView extends UserView {
	public AdministratorView(Scanner scanner) {
		super(scanner);
	}
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
	
    public void viewPendingRefillRequests(Administrator admin) {
        int num = 0;
        System.out.println("Pending Refill Requests:");
        for (RefillRequest request : admin.getRefillRequests()) {
            if (request.getStatus() == Status.PENDING) {
                System.out.println(request);
                num++;
            }
        }
        if (num == 0) {
            System.out.println("No pending refill requests.");
        }
    }
    public void viewCompletedRefillRequests(Administrator admin) {
    	int num = 0;
        System.out.println("Completed Refill Requests:");
        for (RefillRequest request : admin.getRefillRequests()) {
            if (request.getStatus() == Status.COMPLETED) {
                System.out.println(request);
                num++;
            }
        }
        if (num == 0) {
            System.out.println("No completed refill requests.");
        }
    }
    
    public void viewAndManageRefillRequestsMenu() {
    	System.out.println("1. View Pending Refill Requests");
    	System.out.println("2. View Completed Refill Requests");
    	System.out.println("3. Approve Pending Refill Requests");
    	System.out.println("4. Exit");
    }
    
    public void viewMedBankInventory() {
        if (Database.medicationBank != null) {
            Database.medicationBank.viewInventory();
        } else {
            System.out.println("Medication bank is not initialized.");
        }
    }
    
    public void addNewMed() {
    	System.out.println("Enter name of new medicine: ");
    	String name = scanner.next();
    	if(Database.medicationBank.inventory.containsKey(name)) {
    		System.out.println("Medication already exists.");
    		return;
    	}
    	else {
    	System.out.println("Enter stock level");
    	int x = scanner.nextInt();
    	System.out.println("How long until it expires? (in years)");
    	int y = scanner.nextInt();
    	System.out.println("What is the low stock threshold level?");
    	int n = scanner.nextInt();
    	Medication newMeds = new Medication(name, x, LocalDate.now().plusYears(y), n);
    	Database.medicationBank.addMedication(newMeds);
    	}
    }
    
    public void displayAppointments() {
        boolean hasAppointments = false;        
        for (Patient patient : Database.patients) {
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
    
    public void viewAndManageStaffMenu() {
    	System.out.println("1. View Hospital Staff");
    	System.out.println("2. Manage Hospital Staff");
    	System.out.println("3. Exit");
    }
    
 // Display all hospital staff
    public void displayStaff() {
        if (Database.getTotalStaffCount() == 0) {
            System.out.println("No staff members available.");
        } else {
            System.out.println("Hospital Staff:");

            for (Doctor doctor : Database.doctors) {
                System.out.println("ID: " + doctor.getID() + " Name: " + doctor.getName() + " Role: Doctor");
            }

            for (Pharmacist pharmacist : Database.pharmacists) {
                System.out.println("ID: " + pharmacist.getID() + " Name: " + pharmacist.getName() + " Role: Pharmacist");
            }

            for (Administrator administrator : Database.administrators) {
                System.out.println("ID: " + administrator.getID() + " Name: " + administrator.getName() + " Role: Administrator");
            }
        }
    }
    
    public void manageStaffMenu() {
    	System.out.println("1. Add Staff Member");
    	System.out.println("2. Remove Staff Member");
    	System.out.println("3. Return");
    }
    
    public String enterRole() {
    	System.out.println("Enter Role (Doctor, Pharmacist, Administrator): ");
        String role = scanner.nextLine().trim().toLowerCase();
        return role;
    }
    
    public String enterStaffID() {
    	System.out.print("Enter Staff ID: ");
        String id = scanner.nextLine();
        return id;
    }
    
    public String enterName() {
    	System.out.print("Enter Staff Name: ");
        String name = scanner.nextLine();
        return name;
    }
	public void viewSystemLogs() {
		System.out.println("Viewing system logs...\n");
		for (String log : Database.systemLogs) {
			System.out.println(log);
		}
	}
	public ArrayList<User> viewLockedAccounts() {
		ArrayList<User> lockedAccounts = new ArrayList<User>();
		System.out.println("\nViewing locked accounts...");
		System.out.println("\nLocked Patient Accounts");
		for (Patient patient : Database.patients) {
			if (patient.isLocked()) {
				System.out.println(patient.getID());
				lockedAccounts.add(patient);
			}
		}
		System.out.println("\nLocked Doctor Accounts");
		for (Doctor doctor : Database.doctors) {
			if (doctor.isLocked()) {
				System.out.println(doctor.getID());
				lockedAccounts.add(doctor);
			}
		}
		System.out.println("\nLocked Pharmacist Accounts");
		for (Pharmacist pharmacist : Database.pharmacists) {
			if (pharmacist.isLocked()) {
				System.out.println(pharmacist.getID());
				lockedAccounts.add(pharmacist);
			}
		}
		System.out.println("\nLocked administrator Accounts");
		for (Administrator administrator : Database.administrators) {
			if (administrator.isLocked()) {
				System.out.println(administrator.getID());
				lockedAccounts.add(administrator);
			}
		}
		System.out.println("\nWhich account would you like to unlock? Enter ID or -1 to exit");
		return lockedAccounts;
	}
}