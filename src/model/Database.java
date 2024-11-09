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
        userDatabase.put("D001", new Doctor("D001", "Password123", "John Smith"));
        userDatabase.put("D002", new Doctor("D002", "Password123", "Emily Clarke"));
        userDatabase.put("P001", new Pharmacist("P001", "Password123", "Mark Lee"));
        userDatabase.put("A001", new Administrator("A001", "Password123", "Sarah Lee"));
        userDatabase.put("P1001", new Patient("P1001", "Password123", "Alice Brown"));
        userDatabase.put("P1002", new Patient("P1002", "Password123", "Bob Stone"));
        userDatabase.put("P1003", new Patient("P1003", "Password123", "Charlie White"));

        System.out.println("Predefined users have been initialized in the database.");
    }


    // Method to retrieve a user based on user ID
    public static User getUser(String id) {
        return userDatabase.get(id);
    }

    
}
