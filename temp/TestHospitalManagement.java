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
            user.login(inputPassword); // This should trigger password change if first-time login
        }

        // Test Case 26: Login with Incorrect Credentials
        System.out.print("Enter User ID: ");
        userID = scanner.nextLine();
        user = Database.getUser(userID);

        String inputPassword;
		if (user != null) {
            System.out.print("Enter password: ");
            inputPassword = scanner.nextLine();
            user.login(inputPassword); // Validate incorrect login attempt
        }

        // Test Case 27: Login with Correct Credentials and Navigate to Show Menu
        System.out.print("Enter User ID: ");
        userID = scanner.nextLine();
        user = Database.getUser(userID);

        if (user != null) {
            System.out.print("Enter password: ");
            inputPassword = scanner.nextLine();
            if (user.login(inputPassword)) {
                user.showMenu(); // Navigate to role-specific menu based on user role
            }
        }

        scanner.close();
    }
}
