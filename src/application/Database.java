package application;

import model.*;
import java.util.ArrayList;

public class Database {

    public static final ArrayList<Doctor> doctors = new ArrayList<Doctor>();
    public static final ArrayList<Patient> patients = new ArrayList<Patient>();
    public static final ArrayList<Pharmacist> pharmacists = new ArrayList<Pharmacist>();
    public static final ArrayList<Administrator> administrators = new ArrayList<Administrator>();
    public static final MedicationBank medicationBank = new MedicationBank();
    public static final ArrayList<String> systemLogs = new ArrayList<String>();
    
    public static int getTotalStaffCount() {
        return doctors.size() + pharmacists.size() + administrators.size();
    }

    // Constructor to initialize the database with predefined users
    public Database() {
        initializeUsers();
    }

    // Method to initialize predefined users
    private static void initializeUsers() {
        // Adding predefined doctors
        doctors.add(new Doctor("D001", "John Smith"));
        doctors.add(new Doctor("D002", "Emily Clarke"));

        // Adding predefined pharmacists
        pharmacists.add(new Pharmacist("P001","Mark Lee"));

        // Adding predefined administrators
        administrators.add(new Administrator("A001", "Sarah Lee"));

        // Adding predefined patients
        patients.add(new Patient("P1001", "Alice Brown"));
        patients.add(new Patient("P1002", "Bob Stone"));
        patients.add(new Patient("P1003", "Charlie White"));

        System.out.println("Predefined users have been initialized in the database.");
    }

    // Method to find a user by ID
    public static User findUserById(String userId) {
        // Check in doctors list
        for (Doctor doctor : doctors) {
            if (doctor.getID().equals(userId)) {
                return doctor;
            }
        }
        // Check in pharmacists list
        for (Pharmacist pharmacist : pharmacists) {
            if (pharmacist.getID().equals(userId)) {
                return pharmacist;
            }
        }
        // Check in administrators list
        for (Administrator administrator : administrators) {
            if (administrator.getID().equals(userId)) {
                return administrator;
            }
        }
        // Check in patients list
        for (Patient patient : patients) {
            if (patient.getID().equals(userId)) {
                return patient;
            }
        }
        // Return null if user is not found
        return null;
    }


    // Method to retrieve a user based on user ID (fixed version)
    public static User getUser(String id) {
        return findUserById(id);
    }

    // Method to display all users
    public static void displayAllUsers() {
        System.out.println("Doctors:");
        for (Doctor doctor : doctors) {
            System.out.println(doctor);
        }

        System.out.println("Pharmacists:");
        for (Pharmacist pharmacist : pharmacists) {
            System.out.println(pharmacist);
        }

        System.out.println("Administrators:");
        for (Administrator administrator : administrators) {
            System.out.println(administrator);
        }

        System.out.println("Patients:");
        for (Patient patient : patients) {
            System.out.println(patient);
        }
    }
}
