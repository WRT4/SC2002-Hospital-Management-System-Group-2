package application;

import java.util.Scanner;
import controller.UserController;
import model.User;

/**
 * The main entry point for the Hospital Management Application.
 * This class initializes the database, handles user login, and manages
 * the main application loop for interacting with different user controllers.
 * @author Hoo Jing Huan, Lee Kuan Rong, Lim Wee Keat, Tan Wen Rong, Yeoh Kai Wen
 * @version 1.0
 * @since 2024-11-18
 */
public class HospitalManagementApp {

    /**
     * The main method for the Hospital Management Application.
     * Initializes the database and manages the application's main loop, including
     * user login and interaction with the respective controllers.
     *
     * @param args Command-line arguments
     */
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
            System.out.println("Goodbye!");
        }
    }

    /**
     * Prompts the user to enter a User ID and retrieves the corresponding user from the database.
     * If the entered User ID is "-1", the method returns null, indicating an exit request.
     *
     * @param scanner A {@link Scanner} instance for reading user input
     * @return A {@link User} object corresponding to the entered User ID, or null if the user exits
     */
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
