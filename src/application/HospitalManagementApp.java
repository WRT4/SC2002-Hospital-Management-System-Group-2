package application;

import java.util.Scanner;
import controller.UserController;
import model.User;

public class HospitalManagementApp {

	public static void main(String[] args) {
	    Scanner scanner = new Scanner(System.in);
	    Database.initialiseDatabase();
	    User user = null;

	    // Main loop for the application
	    try {
	        while (true) {
	            System.out.println("\nLogging in...");
	            user = getUser(scanner);
	            if (user == null) {
	                System.out.println("Exiting the application!");
	                break;
	            }
	            if (user.isLocked()) {
	                System.out.println("Account is Locked! Please contact an administrator to unlock!");
	                continue;
	            }

	            System.out.print("Enter Password: ");
	            String inputPassword = scanner.nextLine();

	            UserController userController = new UserController(user, scanner);
	            boolean loggedIn = userController.login(user.getID(), inputPassword);

	            if (loggedIn) {
	                userController.showMenu();
	            }

	            System.out.println("You have logged out. Returning to the login page...");
	        }
	    } finally {
	        scanner.close();
	        // Save the database before exiting
	        System.out.println("Saving the database...");
	        Database.saveSerializedDatabase();
	        System.out.println("Database saved successfully. Goodbye!");
	    }
	}

    
    private static User getUser(Scanner scanner) {
		System.out.print("Enter User ID (-1 to exit): ");
        User user = null;
        while (user == null) {
            String userID = scanner.nextLine();
            if (userID.equals("-1")) {
                return null; 
            }
            user = Database.getUser(userID);
            if (user == null) {
                System.out.print("Invalid User ID. Please try again: ");
            }
        }
        return user;
	}
}
