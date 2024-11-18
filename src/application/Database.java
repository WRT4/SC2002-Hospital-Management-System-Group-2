package application;

import model.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Database implements Serializable{

    private static final long serialVersionUID = -4569093636507412208L;
	public static final ArrayList<Doctor> DOCTORS = new ArrayList<Doctor>();
    public static final ArrayList<Patient> PATIENTS = new ArrayList<Patient>();
    public static final ArrayList<Pharmacist> PHARMACISTS = new ArrayList<Pharmacist>();
    public static final ArrayList<Administrator> ADMINISTRATORS = new ArrayList<Administrator>();
    public static final MedicationBank MEDICATION_BANK = new MedicationBank();
    public static final ArrayList<String> SYSTEM_LOGS = new ArrayList<>();
    private static final String DATABASE_FILE = "database.ser";

    // empty constructor
    public Database() {}

    // Method to initialize the database
    public static void initialiseDatabase() {
        if (loadSerializedDatabase()) {
            System.out.println("Data loaded from serialized file.");
        } else {
            try {
                // Load data from CSV files
                loadStaffData("src/application/Staff_List.csv");
                loadPatientData("src/application/Patient_List.csv");
                saveSerializedDatabase();
                System.out.println("Data has been successfully loaded from CSV files and saved for future runs.");
            } catch (IOException e) {
                System.err.println("Error loading data from CSV files: " + e.getMessage());
            }
        }
    }

    // Save the current state of the database to a file
    public static void saveSerializedDatabase() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATABASE_FILE))) {
            oos.writeObject(DOCTORS);
            oos.writeObject(PATIENTS);
            oos.writeObject(PHARMACISTS);
            oos.writeObject(ADMINISTRATORS);
            oos.writeObject(MEDICATION_BANK);
            oos.writeObject(SYSTEM_LOGS);
            System.out.println("Database saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving database: " + e.getMessage());
        }
    }

    // Load the database from a serialized file
    @SuppressWarnings("unchecked")
    private static boolean loadSerializedDatabase() {
        File file = new File(DATABASE_FILE);
        if (!file.exists()) {
            return false; // No serialized data found
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            DOCTORS.clear();
            DOCTORS.addAll((ArrayList<Doctor>) ois.readObject());
            PATIENTS.clear();
            PATIENTS.addAll((ArrayList<Patient>) ois.readObject());
            PHARMACISTS.clear();
            PHARMACISTS.addAll((ArrayList<Pharmacist>) ois.readObject());
            ADMINISTRATORS.clear();
            ADMINISTRATORS.addAll((ArrayList<Administrator>) ois.readObject());
            MEDICATION_BANK.copyFrom((MedicationBank) ois.readObject());
            SYSTEM_LOGS.clear();
            SYSTEM_LOGS.addAll((ArrayList<String>) ois.readObject());
            return true; // Successfully loaded serialized data
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading serialized database: " + e.getMessage());
            return false; // Fall back to CSV initialization
        }
    }
    
    public static int getTotalStaffCount() {
        return DOCTORS.size() + PHARMACISTS.size() + ADMINISTRATORS.size();
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
                        DOCTORS.add(new Doctor(id, name, gender, age));
                        break;
                    case "pharmacist":
                        PHARMACISTS.add(new Pharmacist(id, name, gender, age));
                        break;
                    case "administrator":
                        ADMINISTRATORS.add(new Administrator(id, name, gender, age));
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
                PATIENTS.add(new Patient(id, name, dob, gender, bloodType, contactInfo, age));
            }
        }
    }
    
    
 // Method to find a user by ID
    public static User findUserById(String userId) {
        // Check in DOCTORS list
        for (Doctor doctor : DOCTORS) {
            if (doctor.getID().equals(userId)) {
                return doctor;
            }
        }
        // Check in PHARMACISTS list
        for (Pharmacist pharmacist : PHARMACISTS) {
            if (pharmacist.getID().equals(userId)) {
                return pharmacist;
            }
        }
        // Check in ADMINISTRATORS list
        for (Administrator administrator : ADMINISTRATORS) {
            if (administrator.getID().equals(userId)) {
                return administrator;
            }
        }
        // Check in PATIENTS list
        for (Patient patient : PATIENTS) {
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
        System.out.println("DOCTORS:");
        for (Doctor doctor : DOCTORS) {
            System.out.println(doctor);
        }

        System.out.println("PHARMACISTS:");
        for (Pharmacist pharmacist : PHARMACISTS) {
            System.out.println(pharmacist);
        }

        System.out.println("ADMINISTRATORS:");
        for (Administrator administrator : ADMINISTRATORS) {
            System.out.println(administrator);
        }

        System.out.println("PATIENTS:");
        for (Patient patient : PATIENTS) {
            System.out.println(patient);
        }
    }
}
