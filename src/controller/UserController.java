package controller;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

import application.Database;
import model.User;
import view.UserView;

public class UserController extends SessionController{
	
	private UserView userView;
	private User user;
	private Scanner scanner;
	
	public UserController(User user, Scanner scanner) {
		this.user = user;
		this.userView = new UserView (scanner);
		this.scanner = scanner;
		startTime = LocalTime.now();
		startDate = LocalDate.now();
		String log = "User " + user.getID() + " logged in at " + startTime.format(formatter) + " on " + startDate; 
        Database.SYSTEM_LOGS.add(log);
	}
	
	 // Method to simulate user login
    public boolean login(String inputID, String inputPassword) {
        int attempts = 0;
        final int maxAttempts = 3;

        while (attempts < maxAttempts) {
            // Validate user ID and password
            if (user.getID().equals(inputID) && user.getPassword().equals(inputPassword)) {
            	if (user.isLocked()) return false;
                System.out.println("Login successful!");
                if (user.isFirstLogin()) {
                    System.out.println("You are logging in for the first time or your password has been reset. Please change your password.");
                    changePassword();
                }
                return true;
            } else {
                attempts++;
                System.out.println("Invalid credentials. Please try again.");
                System.out.println("Attempt " + attempts + " of " + maxAttempts);

                // If the maximum attempts are reached, lock the account
                if (attempts == maxAttempts) {
                    System.out.println("Maximum login attempts reached. Account is locked.");
                    lockAccount();
                    return false;
                }

                // Prompt user to re-enter credentials
                inputPassword = userView.enterPassword();
            }
        }
        return false;
    }
    
    // Method to change the user's password
    public void changePassword() {
        System.out.print("Enter your new password: ");
        String newPassword = scanner.nextLine();
        System.out.print("Confirm your new password: ");
        String confirmPassword = scanner.nextLine();

        if (newPassword.equals(confirmPassword)) {
        	if (newPassword.equals("password")) {
        		System.out.println("Please set a different password!");
        		changePassword();
        	}
            user.setPassword(newPassword);
            
            user.setFirstLogin(false);
            System.out.println("Password changed successfully!");

            // Prompt user to log in again with the updated password
            System.out.print("Please log in again with your new password!\n");
            String userID = userView.enterID();
            String newPassword1 = userView.enterPassword();

            // Validate login with the updated password
            if (user.getID().equals(userID) && user.getPassword().equals(newPassword1)) {
                System.out.println("Login successful with updated password!");
                // accessSystem(); // Directly redirect to the role-specific dashboard
            } else {
                System.out.println("Login failed with the updated password. Exiting.");
            }
    
        } else {
            System.out.println("Passwords do not match. Please try again.");
            changePassword(); // Retry if passwords do not match
        }
    }
    
 // Method to handle role-specific actions (to be overridden by subclasses if needed)
    public void accessSystem() {
        String role = user.getRole();
        if (role == null) {
            System.out.println("User role is not set. Access denied.");
            return;
        }

//        if (role.equalsIgnoreCase("patient") && user instanceof Patient) {
//            System.out.println("Accessing Patient Dashboard...");
//            PatientController pC = new PatientController(((Patient) user), scanner);
//            pC.showMenu();
//        } else if (role.equalsIgnoreCase("doctor") && user instanceof Doctor) {
//            System.out.println("Accessing Doctor Dashboard...");
//            DoctorController dC = new DoctorController(((Doctor) user), scanner);
//            dC.showMenu();
//        } else if (role.equalsIgnoreCase("pharmacist") && user instanceof Pharmacist) {
//            System.out.println("Accessing Pharmacist Dashboard...");
//            PharmacistController pC = new PharmacistController(((Pharmacist) user), scanner);
//            pC.showMenu();
//        } else if (role.equalsIgnoreCase("administrator") && user instanceof Administrator) {
//            System.out.println("Accessing Administrator Dashboard...");
//            AdministratorController aC = new AdministratorController(((Administrator) user), scanner);
//            aC.showMenu();
//        } else {
//            System.out.println("Unknown role. Access denied.");
//        }
        
        SessionController sessionController = user.createController(scanner);
        sessionController.showMenu();
    }
    
    
    /**
     * Locks the user account.
     */
    public void lockAccount() {
        user.setLocked(true);
        System.out.println("Account has been locked.");
        String log = "Account " + user.getID() + " has been locked.";
        Database.SYSTEM_LOGS.add(log);
    }

    /**
     * Unlocks the user account.
     */
    public void unlockAccount() {
        user.setLocked(false);
        System.out.println("Account has been unlocked.");
        String log = "Account " + user.getID() + " has been unlocked.";
        Database.SYSTEM_LOGS.add(log);
    }
    
    /**
     * Changes the user's password.
     * @param newPassword The new password to set.
     */
    public void changePassword(String newPassword) {
        user.setPassword(newPassword);
        System.out.println("Password has been changed successfully.");
    }

    
    /**
     * Displays the menu and handles user input for various actions.
     */
    public void showMenu() {
        int choice;

        do {
            userView.showMenu();
            // Input validation
            choice = userView.getChoice();
            scanner.nextLine();
            switch (choice) {
            	case 1:
	                accessSystem();
	                break;
                case 2:
                    System.out.println("Changing password...");
                    changePassword();
                    break;
                case 3:
                    System.out.println("Displaying activity log...");
                    userView.displayActivityLog(user);
                    break;
                case 4:
                    System.out.println("Logging out...");
                    LocalDate endDate = LocalDate.now();
                    LocalTime endTime = LocalTime.now();
                    LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
                    LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);
                    // Calculate the duration
                    Duration duration = Duration.between(startDateTime, endDateTime);
                    // Extract hours, minutes, and seconds
                    long hours = duration.toHours();
                    long minutes = duration.toMinutes() % 60; // Remaining minutes
                    long seconds = duration.getSeconds() % 60; // Remaining seconds
                    String log = "User " + user.getID() + " logged out at " + endTime.format(formatter) + " on " + endDate + ". Session lasted for " + hours + " hours, " + minutes + " minutes, " + seconds + " seconds." ;
                    Database.SYSTEM_LOGS.add(log);
                    break;
                default:
                	System.out.println("Invalid option! Please try again.");
            }
        } while (choice != 4);
    }
	
}
