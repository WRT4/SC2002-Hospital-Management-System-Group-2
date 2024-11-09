package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Database {
	
	public static final ArrayList<Doctor> doctors = new ArrayList<Doctor>();
	public static final ArrayList<Patient> patients = new ArrayList<Patient>();
	public static final ArrayList<Pharmacist> pharmacists = new ArrayList<Pharmacist>();
	public static final ArrayList<Administrator> administrators = new ArrayList<Administrator>();
	public static final MedicationBank medicationBank = new MedicationBank();
	
    private static HashMap<String, User> userDatabase;

    // Constructor to initialize the database with predefined users
    public Database() {
    	
    	
        userDatabase = new HashMap<>();
        initializeUsers();
        
        
	    }
    

    // Method to initialize predefined users
    private static void initializeUsers() {
        // Adding predefined users with default password "Password123"
        userDatabase.put("D001", new Doctor("D001", "Password123", "John Smith", "doctor"));
        userDatabase.put("D002", new Doctor("D002", "Password123", "Emily Clarke", "doctor"));
        userDatabase.put("P001", new Pharmacist("P001", "Password123", "Mark Lee", "pharmacist"));
        userDatabase.put("A001", new Administrator("A001", "Password123", "Sarah Lee", "administrator"));
        userDatabase.put("P1001", new Patient("P1001", "Password123", "Alice Brown", "patient"));
        userDatabase.put("P1002", new Patient("P1002", "Password123", "Bob Stone", "patient"));
        userDatabase.put("P1003", new Patient("P1003", "Password123", "Charlie White", "patient"));

        System.out.println("Predefined users have been initialized in the database.");
    }


    // Method to retrieve a user based on user ID
    public static User getUser(String id) {
        return userDatabase.get(id);
    }

    // Method to validate user login credentials
    public boolean validateLogin(String userID, String password) {
        User user = getUser(userID);
        if (user != null && user.password.equals(password)) {
            return true;
        } else {
            System.out.println("Invalid credentials. Please try again.");
            return false;
        }
    }

    // Method to update a user's password
    public void updatePassword(String userID, String newPassword) {
        User user = getUser(userID);
        if (user != null) {
            user.password = newPassword;
            System.out.println("Password updated successfully for user ID: " + userID);
        } else {
            System.out.println("User not found. Unable to update password.");
        }
    }

    // Method to display all users (for testing purposes)
    public void displayAllUsers() {
        System.out.println("Displaying all users in the database:");
        for (String userID : userDatabase.keySet()) {
            User user = userDatabase.get(userID);
            user.displayUserInfo();
        }
    }

    
}
