package application;

import java.util.Scanner;
import controller.UserController;
import model.User;
import view.UserView;

public class HospitalManagementApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Database db = new Database();
        User user = null;

        // Main loop for the application
//        while (true) {
//            // Prompt the user for their User ID
//            user = UserView.getUser(scanner);
//            if (user == null) {
//            	System.out.println("Exiting the application!");
//            	return;
//            }
//            
//            // Prompt the user for their password
//            System.out.print("Enter Password: ");
//            String inputPassword = scanner.nextLine();
//            
//            UserController userController = new UserController(user,scanner);
//            
//            // Delegate login handling to the User class
//            boolean loggedIn = userController.login(user.getID(), inputPassword);
//
//            // If login is successful, access the user's menu
//            if (loggedIn) {
//                userController.showMenu();
//            }
//
//            // After the user logs out, return to the login prompt
//            System.out.println("You have logged out. Returning to the login page...");
//        }
        while (true) {
        	UserController userController = new UserController(user,scanner);
        	userController.showMenu();
        	
        	// After the user logs out, return to the login prompt
        	System.out.println("You have logged out. Returning to the login page...");
        }
    }
}
