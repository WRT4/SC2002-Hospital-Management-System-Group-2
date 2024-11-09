package model;

import java.util.Scanner;

public class TestHospitalManagement {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Database db = new Database();

        // Test Case 25: First-Time Login and Password Change
        System.out.print("Enter User ID for first-time login: ");
        String userID = scanner.nextLine();
        User user = Database.getUser(userID);

        if (user != null) {
            System.out.print("Enter default password: ");
            String inputPassword = scanner.nextLine();
            
            // Attempt login with userID and inputPassword
            if (user.login(userID, inputPassword)) {
                System.out.println("First-time login successful. You may proceed.");
            } else {
                System.out.println("First-time login failed. Exiting.");
                return;
            }
        } else {
            System.out.println("User not found. Exiting.");
            return;
        }

        // Test Case 26: Login with Incorrect Credentials
        System.out.println("\nTest Case 26: Login with Incorrect Credentials");
        System.out.print("Enter User ID: ");
        userID = scanner.nextLine();
        user = Database.getUser(userID);

        if (user != null) {
            System.out.print("Enter password: ");
            String inputPassword = scanner.nextLine();

            // Validate incorrect login attempt
            if (!user.login(userID, inputPassword)) {
                System.out.println("Login attempt failed. Please check your credentials.");
            }
        } else {
            System.out.println("User not found. Exiting.");
            return;
        }

        // Test Case 27: Login with Correct Credentials and Navigate to Show Menu
        System.out.println("\nTest Case 27: Login with Correct Credentials");
        System.out.print("Enter User ID: ");
        userID = scanner.nextLine();
        user = Database.getUser(userID);

        if (user != null) {
            System.out.print("Enter password: ");
            String inputPassword = scanner.nextLine();

            // Attempt login with correct credentials
            if (user.login(userID, inputPassword)) {
                System.out.println("Login successful. Accessing the role-specific menu...");
                user.showMenu(); // Navigate to role-specific menu based on user role
            } else {
                System.out.println("Login attempt failed. Please check your credentials.");
            }
        } else {
            System.out.println("User not found. Exiting.");
        }

        // Close the scanner
        scanner.close();
    }


