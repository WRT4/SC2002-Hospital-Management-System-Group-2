package model;

import model.Database;
import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * User.java
 * Represents a user in the Hospital Management System (HMS).
 * This class provides login functionality, password management, and role-based access.
 */
public class User {
    protected String id;
    protected String password;
    protected String role;
    protected boolean isFirstLogin;
    protected String name;

    public User(String id, String password, String name, String role) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.role = role;
        this.isFirstLogin = true; // Default to true for new users
    }

    // Method to simulate user login
    public boolean login(String inputPassword) {
        if (this.password.equals(inputPassword)) {
            System.out.println("Login successful!");
            if (isFirstLogin) {
                System.out.println("You are logging in for the first time. Please change your password.");
                changePassword();
            }
            return true;
        } else {
            System.out.println("Invalid credentials. Please try again.");
            return false;
        }
    }

    // Method to change the user's password
    public void changePassword() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your new password: ");
        String newPassword = scanner.nextLine();
        System.out.print("Confirm your new password: ");
        String confirmPassword = scanner.nextLine();

        if (newPassword.equals(confirmPassword)) {
            this.password = newPassword;
            
            this.isFirstLogin = false;
            System.out.println("Password changed successfully!");

            // Prompt user to log in again with the updated password
            Scanner scanner1 = new Scanner(System.in);
            System.out.print("Enter your User ID: ");
            String userID = scanner1.nextLine();
            System.out.print("Enter your new password: ");
            String newPassword1 = scanner1.nextLine();

            // Validate login with the updated password
            if (this.id.equals(userID) && this.password.equals(newPassword1)) {
                System.out.println("Login successful with updated password!");
                accessSystem(); // Directly redirect to the role-specific dashboard
            } else {
                System.out.println("Login failed with the updated password. Exiting.");
            }
    
        } else {
            System.out.println("Passwords do not match. Please try again.");
            changePassword(); // Retry if passwords do not match
        }
    }

    // Method to display user information
    public void displayUserInfo() {
        System.out.println("User ID: " + id);
        System.out.println("Role: " + role);
    }

    // Method to handle role-specific actions (to be overridden by subclasses if needed)
    public void accessSystem() {
        String role = this.getRole();
        if (role == null) {
            System.out.println("User role is not set. Access denied.");
            return;
        }

        if (role.equalsIgnoreCase("patient") && this instanceof Patient) {
            System.out.println("Accessing Patient Dashboard...");
            ((Patient) this).showMenu();
        } else if (role.equalsIgnoreCase("doctor") && this instanceof Doctor) {
            System.out.println("Accessing Doctor Dashboard...");
            ((Doctor) this).showMenu();
        } else if (role.equalsIgnoreCase("pharmacist") && this instanceof Pharmacist) {
            System.out.println("Accessing Pharmacist Dashboard...");
            ((Pharmacist) this).showMenu();
        } else if (role.equalsIgnoreCase("administrator") && this instanceof Administrator) {
            System.out.println("Accessing Administrator Dashboard...");
            ((Administrator) this).showMenu();
        } else {
            System.out.println("Unknown role. Access denied.");
        }
    }


    /**
     * Getter method for the user's name.
     * @return The name of the user.
     */
    public String getName() {
        return this.getName();
    }
    
    private boolean isLocked = false;

    /**
     * Locks the user account.
     */
    public void lockAccount() {
        this.isLocked = true;
        System.out.println("Account has been locked.");
    }

    /**
     * Unlocks the user account.
     */
    public void unlockAccount() {
        this.isLocked = false;
        System.out.println("Account has been unlocked.");
    }

    /**
     * Changes the user's password.
     * @param newPassword The new password to set.
     */
    public void changePassword(String newPassword) {
        this.password = newPassword;
        System.out.println("Password has been changed successfully.");
    }

    /**
     * Displays the activity log for the user (placeholder implementation).
     */
    public void displayActivityLog() {
        System.out.println("Displaying activity log for user ID: " + this.id);
        // Placeholder: In a real system, this would display the user's actual activity log.
    }
  

    /**
     * Displays the menu and handles user input for various actions.
     */
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("User Menu: ");
            System.out.println("1. Login");
            System.out.println("2. Change Password");
            System.out.println("3. Lock Account");
            System.out.println("4. Unlock Account");
            System.out.println("5. Display Activity Log");
            System.out.println("6. Access Role-Specific Dashboard");
            System.out.println("7. Logout");
            System.out.println("Choose an action:");

            // Input validation
            choice = getChoice();
            while (choice < 1 || choice > 7) {
                System.out.println("Invalid option! Please try again.");
                choice = getChoice();
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter password to login: ");
                    String inputPassword = scanner.nextLine();
                    this.login(inputPassword);
                    break;

                case 2:
                    System.out.println("Changing password...");
                    changePassword();
                    break;

                case 3:
                    System.out.println("Locking account...");
                    lockAccount();
                    break;

                case 4:
                    System.out.println("Unlocking account...");
                    unlockAccount();
                    break;

                case 5:
                    System.out.println("Displaying activity log...");
                    displayActivityLog();
                    break;

                case 6:
                    System.out.println("Accessing role-specific dashboard...");
                    accessSystem();
                    break;

                case 7:
                    System.out.println("Logging out...");
                    break;

                default:
                    System.out.println("Unexpected error occurred.");
            }
        } while (choice != 7);
    }

    /**
     * Helper method to get a valid choice from the user.
     */
    private int getChoice() {
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        try {
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine(); // Clear invalid input
        }
        return choice;
    }

    public String getRole() {
        return this.role;
    }
    
}
