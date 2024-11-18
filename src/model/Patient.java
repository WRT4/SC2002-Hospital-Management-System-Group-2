package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

import controller.PatientController;
import controller.SessionController;
import enums.Status;

/**
 * Represents a patient in the medical system.
 * Each patient has a medical record, a list of appointments, and appointment requests.
 * Provides methods to update personal information and manage appointments and requests.
 * @author Hoo Jing Huan, Lee Kuan Rong, Lim Wee Keat, Tan Wen Rong, Yeoh Kai Wen
 * @version 1.0
 * @since 2024-11-18
 */
public class Patient extends User {

	private static final long serialVersionUID = 3070349195380175059L;
	private MedicalRecord record;
	private ArrayList<Appointment> appointments;
	private ArrayList<AppointmentRequest> requests;

	/**
	 * Constructs a Patient object with detailed information.
	 * Initializes a medical record and sets up lists for appointments and requests.
	 *
	 * @param id          The unique ID of the patient
	 * @param name        The name of the patient
	 * @param dob         The date of birth of the patient
	 * @param gender      The gender of the patient
	 * @param bloodType   The blood type of the patient
	 * @param contactInfo The contact information (email) of the patient
	 * @param age         The age of the patient
	 */
	public Patient(String id, String name, String dob, String gender, String bloodType, String contactInfo, int age) {
		super(id, name, "password", "Patient");
		this.record = new MedicalRecord(id, name);
		this.appointments = new ArrayList<>();
		this.requests = new ArrayList<>();

		// Initialize medical record details
		record.setDateOfBirth(dob);
		record.setGender(gender);
		record.setBloodType(bloodType);
		record.setEmail(contactInfo);
		record.setAge(age);
	}

	/**
	 * Constructs a Patient object with a given ID, password, and name.
	 * Initializes the medical record with basic details.
	 *
	 * @param id       The unique ID of the patient
	 * @param password The password for the patient
	 * @param name     The name of the patient
	 */
	public Patient(String id, String password, String name) {
		this(id, name, password, new MedicalRecord(id, name));
	}

	/**
	 * Constructs a Patient object with a given ID and name.
	 * Password defaults to "password".
	 *
	 * @param id   The unique ID of the patient
	 * @param name The name of the patient
	 */
	public Patient(String id, String name) {
		this(id, name, "password", new MedicalRecord(id, name));
	}

	/**
	 * Constructs a Patient object with a given ID, name, password, and medical record.
	 * Initializes lists for appointments and requests.
	 *
	 * @param id       The unique ID of the patient
	 * @param name     The name of the patient
	 * @param password The password for the patient
	 * @param record   The medical record associated with the patient
	 */
	public Patient(String id, String name, String password, MedicalRecord record) {
		super(id, name, password, "Patient");
		this.record = record;
		this.appointments = new ArrayList<>();
		this.requests = new ArrayList<>();
	}

	/**
	 * Sets the date of birth for the patient in their medical record.
	 *
	 * @param dob The new date of birth
	 */
	public void setDateOfBirth(String dob) {
		record.setDateOfBirth(dob);
	}

	/**
	 * Sets the gender for the patient in their medical record.
	 *
	 * @param gender The new gender
	 */
	public void setGender(String gender) {
		record.setGender(gender);
	}

	/**
	 * Updates the name of the patient in their medical record.
	 *
	 * @param name The new name
	 */
	public void setName(String name) {
		record.setName(name);
	}

	/**
	 * Updates the email address for the patient in their medical record.
	 *
	 * @param email The new email address
	 */
	public void updateEmail(String email) {
		record.setEmail(email);
	}

	/**
	 * Updates the phone number for the patient in their medical record.
	 *
	 * @param number The new phone number
	 */
	public void updatePhoneNumber(String number) {
		record.setPhoneNumber(number);
	}

	/**
	 * Retrieves the medical record associated with the patient.
	 *
	 * @return The medical record
	 */
	public MedicalRecord getRecord() {
		return record;
	}

	/**
	 * Retrieves the list of appointments associated with the patient.
	 *
	 * @return An ArrayList of appointments
	 */
	public ArrayList<Appointment> getAppointments() {
		return appointments;
	}

	/**
	 * Returns a string representation of the patient, including their ID and name.
	 *
	 * @return A string with patient details
	 */
	public String toString() {
		return "Patient ID: " + id + ", Patient Name: " + name;
	}

	/**
	 * Retrieves the list of appointment requests made by the patient.
	 *
	 * @return An ArrayList of appointment requests
	 */
	public ArrayList<AppointmentRequest> getRequests() {
		return requests;
	}

	/**
	 * Creates a session controller for the patient, used for accessing the patient dashboard.
	 *
	 * @param scanner A Scanner instance for user input
	 * @return A new PatientController instance
	 */
	@Override
	public SessionController createController(Scanner scanner) {
		System.out.println("Accessing Patient Dashboard...");
		return new PatientController(this, scanner);
	}
	
	/**
	 * Checks if there is an overlapping appointment for the given date and time.
	 * Iterates through the list of appointments and compares the date and start time of each appointment.
	 *
	 * @param date The date to check for overlapping appointments
	 * @param time The time to check for overlapping appointments
	 * @return true if there is an overlapping appointment, false otherwise
	 */
	public boolean checkOverlapping(LocalDate date, LocalTime time) {
	    for (Appointment appointment : appointments) {
	        if (appointment.getDate().equals(date) && appointment.getTimeSlot().getStartTime().equals(time) && appointment.getStatus() == Status.CONFIRMED) {
	            return true;
	        }
	    }
	    return false;
	}
	
}
