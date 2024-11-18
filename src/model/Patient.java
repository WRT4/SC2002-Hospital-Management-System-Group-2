package model;

import java.util.ArrayList;
import java.util.Scanner;

import controller.PatientController;
import controller.SessionController;

public class Patient extends User {
	
	private static final long serialVersionUID = 3070349195380175059L;
	private MedicalRecord record;
    private ArrayList<Appointment> appointments;
    private ArrayList<AppointmentRequest> requests;

    
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
	
	public Patient(String id, String password, String name) {
		this(id, name, password, new MedicalRecord(id, name));
	}
	
	public Patient(String id, String name) {
		this(id, name,"password", new MedicalRecord(id, name));
	}
	
	public Patient(String id, String name, String password, MedicalRecord record) {
		super(id, name, password, "Patient");
		this.record = record;
		this.appointments = new ArrayList<Appointment>();
		this.requests = new ArrayList<AppointmentRequest>();
	}
	
	public void setDateOfBirth(String dob) {
		record.setDateOfBirth(dob);
	}

	public void setGender(String gender) {
		record.setGender(gender);
	}

	public void setName(String name) {
		record.setName(name);
	}
	
	public void updateEmail(String email) {
		record.setEmail(email);
	}
	
	public void updatePhoneNumber(String number) {
		record.setPhoneNumber(number);
	}
	
	public MedicalRecord getRecord() {
		return record;
	}

	public ArrayList<Appointment> getAppointments() {
		return appointments;
	}
	
	public String toString() {
		return "Patient ID: " + id + ", Patient Name: " + name;
	}

	public ArrayList<AppointmentRequest> getRequests() {
		return requests;
	}
	
	@Override
	public SessionController createController(Scanner scanner) {
		System.out.println("Accessing Patient Dashboard...");
		return new PatientController(this, scanner);
	}
}


