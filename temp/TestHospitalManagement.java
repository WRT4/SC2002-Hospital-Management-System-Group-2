package model;

import java.util.Scanner;

public class TestHospitalManagement {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Database db = new Database();
        User user;

        // Main loop for the application
        while (true) {
            // Prompt the user for their User ID
            System.out.print("Enter User ID (-1 to exit): ");
            user = null;
            while (user == null) {
                String userID = scanner.nextLine();
                
                if (userID.equals("-1")) {
                    System.out.println("Exiting the application!");
                    scanner.close();
                    return; // Exit the program
                }

                user = Database.getUser(userID);
                if (user == null) {
                    System.out.print("Invalid User ID. Please try again: ");
                }
            }

            // Prompt the user for their password
            System.out.print("Enter Password: ");
            String inputPassword = scanner.nextLine();

            // Delegate login handling to the User class
            boolean loggedIn = user.login(user.getId(), inputPassword);

            // If login is successful, access the user's menu
            if (loggedIn) {
                user.showMenu();
            }

            // After the user logs out, return to the login prompt
            System.out.println("You have logged out. Returning to the login page...");
        }
    }
}
