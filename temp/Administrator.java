package model;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;

public class Administrator extends User {
    private List<User> staff; // List to manage staff members
    private List<RefillRequest> refillRequests; // List to store refill requests for review
    private List<Request> userRequests; // List to store general user requests (password reset, unlock account)

    private final Scanner scanner = new Scanner(System.in);

    public Administrator(String id, String name) {
        super(id, name, "Administrator");
        this.staff = new ArrayList<>();
        this.refillRequests = new ArrayList<>();
        this.userRequests = new ArrayList<>();
    }
    
    public Administrator(String id, String password, String name) {
    	super(id,password,name,"Administrator");
        this.staff = new ArrayList<>();
        this.refillRequests = new ArrayList<>();
        this.userRequests = new ArrayList<>();
    }

 
    public List<User> getStaff(){ 
    	return this.staff;
    }
    
	public String getId() {
		return this.id;
	}

    public void addStaff() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Role (Doctor, Pharmacist, Administrator): ");
        String role = scanner.nextLine().trim().toLowerCase();

        System.out.print("Enter Staff ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter Staff Name: ");
        String name = scanner.nextLine();

        // Check for duplicates based on role and ID
        boolean exists = false;

        switch (role) {
            case "doctor":
                exists = Database.doctors.stream().anyMatch(doctor -> doctor.getId().equals(id));
                if (!exists) {
                    Doctor newDoctor = new Doctor(id, name);
                    Database.doctors.add(newDoctor);
                    System.out.println("Doctor " + name + " added to the hospital staff.");
                }
                break;

            case "pharmacist":
                exists = Database.pharmacists.stream().anyMatch(pharmacist -> pharmacist.getId().equals(id));
                if (!exists) {
                    Pharmacist newPharmacist = new Pharmacist(id, name);
                    Database.pharmacists.add(newPharmacist);
                    System.out.println("Pharmacist " + name + " added to the hospital staff.");
                }
                break;

            case "administrator":
                exists = Database.administrators.stream().anyMatch(admin -> admin.getId().equals(id));
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
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Role (Doctor, Pharmacist, Administrator): ");
        String role = scanner.nextLine().trim().toLowerCase();

        System.out.print("Enter Staff ID: ");
        String id = scanner.nextLine();

        boolean removed = false;

        switch (role) {
            case "doctor":
                removed = Database.doctors.removeIf(doctor -> doctor.getId().equals(id));
                break;

            case "pharmacist":
                removed = Database.pharmacists.removeIf(pharmacist -> pharmacist.getId().equals(id));
                break;

            case "administrator":
                removed = Database.administrators.removeIf(admin -> admin.getId().equals(id));
                break;

            default:
                System.out.println("Invalid role entered.");
                return;
        }

        if (removed) {
            System.out.println("Staff member with ID " + id + " has been removed.");
        } else {
            System.out.println("Staff member with ID " + id + " not found.");
        }
    }
    
    public void manageStaff() {
    	int c;
    	do {
    	System.out.println("1. Add Staff Member");
    	System.out.println("2. Remove Staff Member");
    	System.out.println("3. Return");
    	c = scanner.nextInt();
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
    	}while (c!=3);
    }

    // Display all hospital staff
    public void displayStaff() {
        if (Database.getTotalStaffCount() == 0) {
            System.out.println("No staff members available.");
        } else {
            System.out.println("Hospital Staff:");

            for (Doctor doctor : Database.doctors) {
                System.out.println("ID: " + doctor.getId() + " Name: " + doctor.getName() + " Role: Doctor");
            }

            for (Pharmacist pharmacist : Database.pharmacists) {
                System.out.println("ID: " + pharmacist.getId() + " Name: " + pharmacist.getName() + " Role: Pharmacist");
            }

            for (Administrator administrator : Database.administrators) {
                System.out.println("ID: " + administrator.getId() + " Name: " + administrator.getName() + " Role: Administrator");
            }
        }
    }

    public void ViewAndManageStaff() {
    	int c; 
    	do {
    	System.out.println("1. View Hospital Staff");
    	System.out.println("2. Manage Hospital Staff");
    	System.out.println("3. Exit");
    	System.out.println("Enter your choice: ");
    	c = scanner.nextInt();
    	scanner.nextLine();
    	
    	switch(c) {
    	case 1:
    		displayStaff();
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
    	}while (c != 3);
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

    
    public void ViewAndManageMedInventory() {
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
    		viewMedBankInventory();
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
    	}while (c != 3);
    }
    
    private void viewMedBankInventory() {
        if (Database.medicationBank != null) {
            Database.medicationBank.viewInventory();
        } else {
            System.out.println("Medication bank is not initialized.");
        }
    }
    
    private void addNewMed() {
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
        		addNewMed();
        		break;
        	case 4:
        		break;
        	default:
        		System.out.println("Error input. Please choose from 1-4.");
        		break;
        	}
        	}while (c != 4);
    }
    
    
    
    // Receive a refill request from pharmacists
    public void receiveRefillRequest(RefillRequest request) {
        refillRequests.add(request);
    }

    // Approve a specific refill request
    public void approveRefillRequest() {
        System.out.print("Enter request number to approve:     (Enter -1 to exit)");
        int num = scanner.nextInt();
        if (num == -1)
        return;
        int requestNum = num -1;
        if (requestNum >= 0 && requestNum < refillRequests.size()) {
            RefillRequest request = refillRequests.get(requestNum);
            if (request.getStatus() == Status.COMPLETED) {
            	System.out.println("Replenishment request already completed.");
            	return;
            }
            request.acceptRequest();
            Database.medicationBank.increaseStock(request.getMedication(), request.getRequestedAmount()); 
        } else {
            System.out.println("Invalid request index.");
        }
    }

    public void viewPendingRefillRequests() {
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
    public void viewCompletedRefillRequests() {
    	int num = 0;
        System.out.println("Completed Refill Requests:");
        for (RefillRequest request : refillRequests) {
            if (request.getStatus() == Status.COMPLETED) {
                System.out.println(request);
                num++;
            }
        }
        if (num == 0) {
            System.out.println("No completed refill requests.");
        }
    }
    
    public void ViewAndManageRefillRequests() {
    	int c;
    	do {
        	System.out.println("1. View Pending Refill Requests");
        	System.out.println("2. View Completed Refill Requests");
        	System.out.println("3. Approve Pending Refill Requests");
        	System.out.println("4. Exit");
        	System.out.println("Enter your choice: ");
        	c = scanner.nextInt();
        	scanner.nextLine();
        	
        	switch(c) {
        	case 1:
                viewPendingRefillRequests();
        		break;
        	case 2:
        		viewCompletedRefillRequests();
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
        	}while (c != 4);
    }

    

    @Override
    public void showMenu() {
        int adminChoice;
        do {
            System.out.println("Administrator Menu:");
            System.out.println("1. View and Manage Hospital Staff");
            System.out.println("2. View Appointments Details");
            System.out.println("3. View and Manage Medication Inventory");
            System.out.println("4. View and Approve Replenishment Requests");
            System.out.println("5. Logout");
            adminChoice = scanner.nextInt();
            scanner.nextLine(); 

            switch (adminChoice) {
                case 1: 
                	ViewAndManageStaff();
                	break;
                case 2: 
                	displayAppointments();
                    break;
                case 3: 
                	ViewAndManageMedInventory();
                    break;
                case 4: 
                	ViewAndManageRefillRequests();
                    break;
                case 5:
                    System.out.println("Logging out of Administrator menu.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (adminChoice != 5);
    }






    //********************************* This part onwards is mainly extras, not for test cases ******************************************8**

    // Keeping original methods for handling user requests, account locking, password resetting, and logging
    public void receiveUserRequest(Request request) {
        userRequests.add(request);
        System.out.println("User request received: " + request);
    }

   
    // Approve a general user request
    public void approveUserRequest(int requestIndex) {
        if (requestIndex >= 0 && requestIndex < userRequests.size()) {
            Request request = userRequests.get(requestIndex);
            request.acceptRequest();
            userRequests.remove(requestIndex); // Remove from list after approval
            System.out.println("User request approved.");
        } else {
            System.out.println("Invalid request index.");
        }
    }
    
    // Display all pending user requests
    public void viewPendingUserRequests() {
        if (userRequests.isEmpty()) {
            System.out.println("No pending user requests.");
        } else {
            System.out.println("Pending User Requests:");
            for (int i = 0; i < userRequests.size(); i++) {
                System.out.println((i + 1) + ". " + userRequests.get(i));
            }
        }
    }
    
 // Decline a general user request
    public void declineUserRequest(int requestIndex) {
        if (requestIndex >= 0 && requestIndex < userRequests.size()) {
            Request request = userRequests.get(requestIndex);
            request.declineRequest();
            userRequests.remove(requestIndex); // Remove from list after decline
            System.out.println("User request declined.");
        } else {
            System.out.println("Invalid request index.");
        }
    }
    
    // Lock or unlock a user account
    public void toggleAccountLock(User user, boolean lock) {
        if (lock) {
            user.lockAccount(); // Lock the account
            System.out.println(user.getName() + "'s account has been locked.");
        } else {
            user.unlockAccount(); // Unlock the account
            System.out.println(user.getName() + "'s account has been unlocked.");
        }
    }
    
    // Method to receive a password reset request
    public void receiveRequest(PasswordResetRequest request) {
        userRequests.add(request);
        System.out.println("Password reset request received from user: " + request.getUser().getName());
    }
    
    // Method to receive an unlock account request
    public void receiveRequest(UnlockAccountRequest request) {
        userRequests.add(request);
        System.out.println("Unlock account request received from user: " + request.getUser().getName());
    }
    

    // Reset a staff member's password to the default
    public void resetPassword(User user) {
        user.changePassword("password"); // Reset to default password
        System.out.println(user.getName() + "'s password has been reset to the default.");
    }

    // View the activity log of a specific staff member
    public void viewStaffActivityLog(User staffMember) {
        System.out.println("Activity Log for " + staffMember.getName() + ":");
        staffMember.displayActivityLog();
    }
    
    public String toString() {
    	return "Administrator ID: " + getId() + " Name: " + getName();
    }

	//No need for decline Refill request(?) Keeping it here jic.
     // Decline a specific refill request
    /*public void declineRefillRequest(int requestIndex) {
        if (requestIndex >= 0 && requestIndex < refillRequests.size()) {
            RefillRequest request = refillRequests.get(requestIndex);
            request.declineRequest();
            refillRequests.remove(requestIndex); // Remove from list after decline
            System.out.println("Refill request declined for " + request.getMedication());
        } else {
            System.out.println("Invalid request index.");
        }
    }*/

    
}
