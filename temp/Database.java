package model;

import java.util.ArrayList;

public class Database {

    public static final ArrayList<Doctor> doctors = new ArrayList<>();
    public static final ArrayList<Patient> patients = new ArrayList<>();
    public static final ArrayList<Pharmacist> pharmacists = new ArrayList<>();
    public static final ArrayList<Administrator> administrators = new ArrayList<>();
    public static final MedicationBank medicationBank = new MedicationBank();
    
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
        doctors.add(new Doctor("D001", "John Smith", "password"));
        doctors.add(new Doctor("D002", "Emily Clarke", "password"));

        // Adding predefined pharmacists
        pharmacists.add(new Pharmacist("P001","Mark Lee", "password"));

        // Adding predefined administrators
        administrators.add(new Administrator("A001", "Sarah Lee", "password"));

        // Adding predefined patients
        patients.add(new Patient("P1001", "Alice Brown", "password", new MedicalRecord()));
        patients.add(new Patient("P1002", "Bob Stone", "password", new MedicalRecord()));
        patients.add(new Patient("P1003", "Charlie White", "password", new MedicalRecord()));

        System.out.println("Predefined users have been initialized in the database.");
    }

    // Method to find a user by ID
    public static User findUserById(String userId) {
        // Check in doctors list
        for (Doctor doctor : doctors) {
            if (doctor.getId().equals(userId)) {
                return doctor;
            }
        }
        // Check in pharmacists list
        for (Pharmacist pharmacist : pharmacists) {
            if (pharmacist.getId().equals(userId)) {
                return pharmacist;
            }
        }
        // Check in administrators list
        for (Administrator administrator : administrators) {
            if (administrator.getId().equals(userId)) {
                return administrator;
            }
        }
        // Check in patients list
        for (Patient patient : patients) {
            if (patient.getId().equals(userId)) {
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
