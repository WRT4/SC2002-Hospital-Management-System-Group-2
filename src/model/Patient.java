package model;

import java.util.ArrayList;

public class Patient extends User {
	
	private MedicalRecord record;
	private ArrayList<Appointment> appointments;
	private ArrayList<AppointmentRequest> requests;
	
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

}

