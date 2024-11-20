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

/**
 * Represents the central database for the Hospital Management System.
 * Manages data storage, retrieval, and initialization for various entities, including doctors, patients,
 * pharmacists, and administrators. Handles both serialized file storage and CSV-based initialization.
 * @author Lim Wee Keat, Tan Wen Rong, Yeoh Kai Wen
 * @version 1.0
 * @since 2024-11-18
 */
public class Database implements Serializable {

    private static final long serialVersionUID = -4569093636507412208L;
    public static final ArrayList<Doctor> DOCTORS = new ArrayList<>();
    public static final ArrayList<Patient> PATIENTS = new ArrayList<>();
    public static final ArrayList<Pharmacist> PHARMACISTS = new ArrayList<>();
    public static final ArrayList<Administrator> ADMINISTRATORS = new ArrayList<>();
    public static final MedicationBank MEDICATION_BANK = new MedicationBank();
    public static final ArrayList<String> SYSTEM_LOGS = new ArrayList<>();
    private static final String DATABASE_FILE = "database.dat";
    public static int refillRequestCount;
    public static int appointmentRequestCount;
    public static int appointmentCount;

    /**
     * Default constructor for the Database class.
     */
    public Database() {}

    /**
     * Initializes the database by loading data from a serialized file or CSV files.
     * If a serialized file exists, it loads the data; otherwise, it initializes data from CSV files.
     */
    public static void initialiseDatabase() {
        if (loadSerializedDatabase()) {
            System.out.println("Data loaded from serialized file.");
        } else {
            try {
                loadStaffData("src/application/Staff_List.csv");
                loadPatientData("src/application/Patient_List.csv");
                MEDICATION_BANK.initialiseMedicineBank();
                saveSerializedDatabase();
                System.out.println("Data has been successfully loaded from CSV files and saved for future runs.");
            } catch (IOException e) {
                System.err.println("Error loading data from CSV files: " + e.getMessage());
            }
        }
    }

    /**
     * Saves the current state of the database to a serialized file.
     */
    public static void saveSerializedDatabase() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATABASE_FILE))) {
            oos.writeObject(DOCTORS);
            oos.writeObject(PATIENTS);
            oos.writeObject(PHARMACISTS);
            oos.writeObject(ADMINISTRATORS);
            oos.writeObject(MEDICATION_BANK);
            oos.writeObject(SYSTEM_LOGS);
            oos.writeObject(refillRequestCount);
            oos.writeObject(appointmentRequestCount);
            oos.writeObject(appointmentCount);
            System.out.println("Database saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving database: " + e.getMessage());
        }
    }

    /**
     * Loads the database from a serialized file, if it exists.
     *
     * @return True if the data was successfully loaded, false otherwise.
     */
    @SuppressWarnings("unchecked")
    private static boolean loadSerializedDatabase() {
        File file = new File(DATABASE_FILE);
        if (!file.exists()) {
            return false;
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
            refillRequestCount = (int) ois.readObject();
            appointmentRequestCount = (int) ois.readObject();
            appointmentCount = (int) ois.readObject();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading serialized database: " + e.getMessage());
            return false;
        } catch (Exception e) {
        	System.err.println("Error occured: " + e.getMessage());
        	return false;
        }
    }

    /**
     * Retrieves the total count of staff members, including doctors, pharmacists, and administrators.
     *
     * @return The total number of staff members.
     */
    public static int getTotalStaffCount() {
        return DOCTORS.size() + PHARMACISTS.size() + ADMINISTRATORS.size();
    }

    /**
     * Loads staff data from a CSV file and initializes corresponding entities.
     *
     * @param filePath The path to the staff data CSV file.
     * @throws IOException If an error occurs while reading the file.
     */
    private static void loadStaffData(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
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

    /**
     * Loads patient data from a CSV file and initializes corresponding entities.
     *
     * @param filePath The path to the patient data CSV file.
     * @throws IOException If an error occurs while reading the file.
     */
    private static void loadPatientData(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
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

                LocalDate birthDate = LocalDate.parse(dob, formatter);
                int age = Period.between(birthDate, LocalDate.now()).getYears();

                PATIENTS.add(new Patient(id, name, dob, gender, bloodType, contactInfo, age));
            }
        }
    }

    /**
     * Finds and returns a user by their unique ID.
     *
     * @param userId The unique ID of the user.
     * @return The {@link User} object if found, or null otherwise.
     */
    public static User findUserById(String userId) {
        for (Doctor doctor : DOCTORS) {
            if (doctor.getID().equals(userId)) {
                return doctor;
            }
        }
        for (Pharmacist pharmacist : PHARMACISTS) {
            if (pharmacist.getID().equals(userId)) {
                return pharmacist;
            }
        }
        for (Administrator administrator : ADMINISTRATORS) {
            if (administrator.getID().equals(userId)) {
                return administrator;
            }
        }
        for (Patient patient : PATIENTS) {
            if (patient.getID().equals(userId)) {
                return patient;
            }
        }
        return null;
    }

    /**
     * Retrieves a user by their unique ID.
     * Delegates the search to {@link #findUserById(String)}.
     *
     * @param id The unique ID of the user.
     * @return The {@link User} object if found, or null otherwise.
     */
    public static User getUser(String id) {
        return findUserById(id);
    }

    /**
     * Displays all users stored in the database.
     * Includes doctors, pharmacists, administrators, and patients.
     */
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
