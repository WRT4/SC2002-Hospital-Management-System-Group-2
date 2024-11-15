package application;

import model.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Database {

    public static final ArrayList<Doctor> doctors = new ArrayList<Doctor>();
    public static final ArrayList<Patient> patients = new ArrayList<Patient>();
    public static final ArrayList<Pharmacist> pharmacists = new ArrayList<Pharmacist>();
    public static final ArrayList<Administrator> administrators = new ArrayList<Administrator>();
    public static final MedicationBank medicationBank = new MedicationBank();
    public static final ArrayList<String> systemLogs = new ArrayList<>();
//    public static final ArrayList<User> allUsers = new ArrayList<User>();

    // Constructor to initialize the database from CSV files
    public Database() {
        try {
            loadStaffData("src/application/Staff_List.csv");
            loadPatientData("src/application/Patient_List.csv");
            System.out.println("Data has been successfully loaded from CSV files.");
        } catch (IOException e) {
            System.err.println("Error loading data from CSV files: " + e.getMessage());
        }
//        allUsers.addAll(doctors);
//        allUsers.addAll(patients);
//        allUsers.addAll(pharmacists);
//        allUsers.addAll(administrators);
    }
    
    public static int getTotalStaffCount() {
        return doctors.size() + pharmacists.size() + administrators.size();
    }

    // Method to load staff data from CSV file
    private static void loadStaffData(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip header row
                    continue;
                }

                String[] data = line.split(",");
                if (data.length < 5) {
                    System.out.println("Invalid data format: " + line);
                    continue;
                }

                String id = data[0].trim();
                String name = data[1].trim();
                String role = data[2].trim().toLowerCase();
                String gender = data[3].trim();
                int age = Integer.parseInt(data[4].trim());

                switch (role) {
                    case "doctor":
                        doctors.add(new Doctor(id, name, gender, age));
                        break;
                    case "pharmacist":
                        pharmacists.add(new Pharmacist(id, name, gender, age));
                        break;
                    case "administrator":
                        administrators.add(new Administrator(id, name, gender, age));
                        break;
                    default:
                        System.out.println("Unknown role: " + role);
                        break;
                }
            }
        }
    }

    // Method to load patient data from CSV file
    private static void loadPatientData(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip header row
                    continue;
                }

                String[] data = line.split(",");
                if (data.length < 6) {
                    System.out.println("Invalid data format: " + line);
                    continue;
                }

                String id = data[0].trim();
                String name = data[1].trim();
                String dob = data[2].trim();
                String gender = data[3].trim();
                String bloodType = data[4].trim();
                String contactInfo = data[5].trim();

                // Calculate age based on Date of Birth
                LocalDate birthDate = LocalDate.parse(dob, formatter);
                int age = Period.between(birthDate, LocalDate.now()).getYears();

                // Create a Patient object with the calculated age
                patients.add(new Patient(id, name, dob, gender, bloodType, contactInfo, age));
            }
        }
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
