package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a medical record for a patient.
 * It contains details such as the patient's personal information, medical diagnoses,
 * prescriptions, and contact details.
 * @author Hoo Jing Huan, Lee Kuan Rong, Lim Wee Keat, Tan Wen Rong, Yeoh Kai Wen
 * @version 1.0
 * @since 2024-11-18
 */
public class MedicalRecord implements Serializable {

    private static final long serialVersionUID = 4416168631521337836L;
    private String patientID;
    private String name;
    private String dateOfBirth;
    private String gender;
    private String bloodType;
    private ArrayList<String> diagnoses;
    private ArrayList<String> prescriptions;
    private String phoneNumber;
    private String email;
    private int age;

    /**
     * Constructs a MedicalRecord object with detailed patient information.
     *
     * @param patientID   The unique ID of the patient
     * @param name        The name of the patient
     * @param dateOfBirth The date of birth of the patient
     * @param gender      The gender of the patient
     * @param phoneNumber The patient's phone number
     * @param email       The patient's email address
     * @param bloodType   The blood type of the patient
     */
    public MedicalRecord(String patientID, String name, String dateOfBirth, String gender, String phoneNumber, String email, String bloodType) {
        this.patientID = patientID;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.bloodType = bloodType;
        this.diagnoses = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
    }

    /**
     * Constructs a basic MedicalRecord with only patient ID and name.
     * Other fields are initialized as empty strings.
     *
     * @param patientID The unique ID of the patient
     * @param name      The name of the patient
     */
    public MedicalRecord(String patientID, String name) {
        this(patientID, name, "", "", "", "", "");
    }

    /**
     * Default constructor for MedicalRecord. Initializes all fields as empty strings.
     */
    public MedicalRecord() {
        this("", "", "", "", "", "", "");
    }

    /**
     * Adds a diagnosis to the medical record if added by a doctor.
     *
     * @param diagnosis The diagnosis to be added
     * @param doctor    The doctor adding the diagnosis
     */
    public void addDiagnosis(String diagnosis, Doctor doctor) {
        if (doctor != null) {
            diagnoses.add(diagnosis);
            System.out.println("Diagnosis added by Dr. " + doctor.getName());
        } else {
            System.out.println("Only a doctor can add a diagnosis.");
        }
    }

    /**
     * Adds a prescription to the medical record if added by a doctor.
     *
     * @param prescription The prescription to be added
     * @param doctor       The doctor adding the prescription
     */
    public void addPrescription(String prescription, Doctor doctor) {
        if (doctor != null) {
            prescriptions.add(prescription);
            System.out.println("Prescription added by Dr. " + doctor.getName());
        } else {
            System.out.println("Only a doctor can add a prescription.");
        }
    }

    /**
     * Adds a prescription to the medical record without any restrictions.
     *
     * @param prescription The prescription to be added
     */
    public void addPrescription(String prescription) {
        prescriptions.add(prescription);
    }

    /**
     * Gets the phone number of the patient.
     *
     * @return The phone number
     */
    public String getPhonenumber() {
        return phoneNumber;
    }

    /**
     * Sets a new phone number for the patient.
     *
     * @param phoneNumber The new phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the email address of the patient.
     *
     * @return The email address
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Sets a new email address for the patient.
     *
     * @param email The new email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Prints the complete medical record of the patient, including all personal and medical details.
     */
    public void printMedicalRecord() {
        System.out.println("Name: " + name + " | ID: " + patientID + " | Phone number: " + phoneNumber + " | Email Address: " + email +
                "\nDate Of Birth: " + dateOfBirth + " | Gender: " + gender + " | Blood Type: " + bloodType + " | Age: " + age +
                "\nDiagnoses: " + diagnoses +
                "\nPrescriptions: " + prescriptions);
    }

    /**
     * Sets the date of birth for the patient.
     *
     * @param dob The new date of birth
     */
    public void setDateOfBirth(String dob) {
        this.dateOfBirth = dob;
    }

    /**
     * Gets the date of birth of the patient.
     *
     * @return The date of birth
     */
    public String getDateOfBirth() {
        return this.dateOfBirth;
    }

    /**
     * Gets the phone number of the patient.
     *
     * @return The phone number
     */
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    /**
     * Gets the name of the patient.
     *
     * @return The name of the patient
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the gender of the patient.
     *
     * @param gender The gender to be set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Sets the name of the patient.
     *
     * @param name The new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the blood type of the patient.
     *
     * @param bloodType The new blood type
     */
    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    /**
     * Displays the patient's contact information.
     */
    public void viewContactInfo() {
        System.out.println("Current contact info: ");
        System.out.println("Name: " + getName());
        System.out.println("Email: " + getEmail());
        System.out.println("Date of Birth: " + getDateOfBirth());
        System.out.println("Phone Number: " + getPhoneNumber());
    }

    /**
     * Gets the age of the patient.
     *
     * @return The age of the patient
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the age of the patient.
     *
     * @param age The new age
     */
    public void setAge(int age) {
        this.age = age;
    }
}
