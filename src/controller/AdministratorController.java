package controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

import application.Database;
import enums.Status;
import model.Administrator;
import model.Doctor;
import model.Pharmacist;
import model.RefillRequest;
import view.AdministratorView;

public class AdministratorController extends SessionController{
	private Administrator admin;
	private AdministratorView administratorView;
	
	public void showMenu() {
        int adminChoice;
        do {
        	administratorView.showMessageBox(admin.getMessages(), unreadIndex);
        	administratorView.showMenu();
            adminChoice = administratorView.getChoice();
            scanner.nextLine(); 
            switch (adminChoice) {
	            case 1:
	        		unreadIndex = administratorView.viewInbox(admin.getMessages(), unreadIndex);
	        		admin.setUnreadIndex(unreadIndex);
	        		break;
                case 2: 
                	viewAndManageStaff();
                	break;
                case 3: 
                	administratorView.displayAppointments();
                    break;
                case 4: 
                	viewAndManageMedInventory();
                    break;
                case 5: 
                	viewAndManageRefillRequests();
                    break;
                case 6:
                	administratorView.viewSystemLogs();
                case 7:
                    System.out.println("Logging out of Administrator menu.");
                    String log = "Admin " + admin.getID() + " accessed system from " + startTime + " on " + startDate + " to " + LocalTime.now() + " on " + LocalDate.now(); 
                    Database.systemLogs.add(log);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (adminChoice != 7);
    }
	
	public AdministratorController(Administrator admin, Scanner scanner) {
		this.admin = admin;
		this.scanner = scanner;
		administratorView = new AdministratorView(scanner);
		unreadIndex = admin.getUnreadIndex();
		startTime = LocalTime.now();
		startDate = LocalDate.now();
	}
	
	public void viewAndManageRefillRequests() {
    	int c;
    	do {
    		administratorView.viewAndManageRefillRequestsMenu();
        	c = administratorView.getChoice();
        	scanner.nextLine();
        	switch(c) {
        	case 1:
        		administratorView.viewPendingRefillRequests(admin);
        		break;
        	case 2:
        		administratorView.viewCompletedRefillRequests(admin);
        		break;
        	case 3:
                approveRefillRequest();
        		break;
        	case 4:
        		break;
        	default:
        		System.out.println("Error input. Please choose from 1-4.");
        		break;
        	}
        } while (c != 4);
    }
	
	// Approve a specific refill request
    public void approveRefillRequest() {
        System.out.print("Enter request number to approve:     (Enter -1 to exit)");
        int num = administratorView.getChoice();
        if (num == -1)
        return;
        int requestNum = num -1;
        if (requestNum >= 0 && requestNum < admin.getRefillRequests().size()) {
            RefillRequest request = admin.getRefillRequests().get(requestNum);
            if (request.getStatus() == Status.COMPLETED) {
            	System.out.println("Replenishment request already completed.");
            	return;
            }
            request.acceptRequest();
            Database.medicationBank.increaseStock(request.getMedication(), request.getRequestedAmount()); 
            sendMessage(request);
        } else {
            System.out.println("Invalid request index.");
        }
    }
    
    //Function to update pharmacist to inform about success of replenishment request 
    public void sendMessage(RefillRequest r) {
    	String message;
    	message = "Request at " + r.getRequestDate() + ": Refill Request for " + r.getMedication() + " of amount: " +
    	r.getRequestedAmount() + " successfully fulfillled!";
    	r.getPharmacist().getMessages().add(message);
    }
    
    
    public void viewAndManageMedInventory() {
    	int c; 
    	do {
	    	System.out.println("1. View Medication Inventory");
	    	System.out.println("2. Manage Medication Inventory");
	    	System.out.println("3. Exit");
	    	System.out.println("Enter your choice: ");
	    	c = scanner.nextInt();
	    	scanner.nextLine();
	    	
	    	switch(c) {
		    	case 1:
		    		administratorView.viewMedBankInventory();
		    		break;
		    	case 2:
		    		manageMedBankInventory();
		    		break;
		    	case 3:
		    		break;
		    	default:
		    		System.out.println("Error input. Please choose 1,2 or 3.");
		    		break;
		    }
    	} while (c != 3);
    }
	
    private void manageMedBankInventory() {
    	int c;
    	do {
        	System.out.println("1. Add medication stock");
        	System.out.println("2. Remove medication stock");
        	System.out.println("3. Add new medication to inventory");
        	System.out.println("4. Exit");
        	System.out.println("Enter your choice: ");
        	c = scanner.nextInt();
        	scanner.nextLine();
        	
        	switch(c) {
	        	case 1:
	        	  	System.out.println("");
	                System.out.print("Enter name of medication to be updated");
	                String medicationName = scanner.nextLine();
	                System.out.print("Enter amount to be added: ");
	                int stock1 = scanner.nextInt();
	                Database.medicationBank.increaseStock(medicationName, stock1); 
	        		break;
	        	case 2:
	        	  	System.out.println("");
	                System.out.print("Enter name of medication to be updated");
	                String medName = scanner.nextLine();
	                System.out.print("Enter amount to be removed: ");
	                int stock2 = scanner.nextInt();
	                Database.medicationBank.decreaseStock(medName, stock2); 
	        		break;
	        	case 3:
	        		administratorView.addNewMed();
	        		break;
	        	case 4:
	        		break;
	        	default:
	        		System.out.println("Error input. Please choose from 1-4.");
	        		break;
        	}
        } while (c != 4);
    }
    
    public void viewAndManageStaff() {
    	int c; 
    	do {
	    	administratorView.viewAndManageStaffMenu();
	    	c = administratorView.getChoice();
	    	scanner.nextLine();	
	    	switch(c) {
		    	case 1:
		    		administratorView.displayStaff();
		    		break;
		    	case 2:
		    		manageStaff();
		    		break;
		    	case 3:
		    		break;
		    	default:
		    		System.out.println("Error input. Please choose 1,2 or 3.");
		    		break;
	    	}
    	} while (c != 3);
    }
    
    public void manageStaff() {
    	int c;
    	do {
    		administratorView.manageStaffMenu();
	    	c = administratorView.getChoice();
	    	scanner.nextLine();
	    	
	    	switch (c) {
		    	case 1:
		    		addStaff();
		    		break;
		    	case 2:
		    		removeStaff();
		    		break;
		    	case 3:
		    		break;
		    	default:
		    		System.out.println("Invalid input. Please choose 1,2 or 3.");
		    		break;
	    	}
    	} while (c!=3);
    }
    
    public void addStaff() {
        String role = administratorView.enterRole();
        String id = administratorView.enterID();
        String name = administratorView.enterName();

        // Check for duplicates based on role and ID
        boolean exists = false;

        switch (role) {
            case "doctor":
                exists = Database.doctors.stream().anyMatch(doctor -> doctor.getID().equals(id));
                if (!exists) {
                    Doctor newDoctor = new Doctor(id, name);
                    Database.doctors.add(newDoctor);
                    System.out.println("Doctor " + name + " added to the hospital staff.");
                }
                break;

            case "pharmacist":
                exists = Database.pharmacists.stream().anyMatch(pharmacist -> pharmacist.getID().equals(id));
                if (!exists) {
                    Pharmacist newPharmacist = new Pharmacist(id, name);
                    Database.pharmacists.add(newPharmacist);
                    System.out.println("Pharmacist " + name + " added to the hospital staff.");
                }
                break;

            case "administrator":
                exists = Database.administrators.stream().anyMatch(admin -> admin.getID().equals(id));
                if (!exists) {
                    Administrator newAdmin = new Administrator(id, name);
                    Database.administrators.add(newAdmin);
                    System.out.println("Administrator " + name + " added to the hospital staff.");
                }
                break;

            default:
                System.out.println("Invalid role entered.");
                return;
        }

        if (exists) {
            System.out.println("Error: A staff member with ID " + id + " already exists in the " + role + " list.");
        }
    }

    
    public void removeStaff() {
        String role = administratorView.enterRole();
        String id = administratorView.enterID();

        boolean removed = false;

        switch (role) {
            case "doctor":
                removed = Database.doctors.removeIf(doctor -> doctor.getID().equals(id));
                break;

            case "pharmacist":
                removed = Database.pharmacists.removeIf(pharmacist -> pharmacist.getID().equals(id));
                break;

            case "administrator":
                removed = Database.administrators.removeIf(admin -> admin.getID().equals(id));
                break;

            default:
                System.out.println("Invalid role entered.");
                return;
        }

        if (removed) {
            System.out.println("Staff member with ID " + id + " has been removed.");
        } 
        else {
            System.out.println("Staff member with ID " + id + " not found.");
        }
    }
}