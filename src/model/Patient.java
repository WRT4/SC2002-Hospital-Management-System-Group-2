package model;

import java.util.ArrayList;

public class Patient extends User {
	
	private MedicalRecord record;
	private ArrayList<Appointment> appointments;
	private ArrayList<AppointmentRequest> requests;
	private ArrayList<String> messages;
	private int unreadIndex;

	
	public void showMenu() {}
	
	public Patient(String id, String password, String name) {
		super(id, password, name, "Patient");
		this.record = new MedicalRecord(id, name);
		this.appointments = new ArrayList<Appointment>();
		this.requests = new ArrayList<AppointmentRequest>();
		this.messages = new ArrayList<String>();
		this.unreadIndex = 0;
	}
	
	public String getPatientId() {
		return id;
	}
	
	public String getName() {
		return name;
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

	public ArrayList<String> getMessages(){
		return messages;
	}
	
	public String toString() {
		return "Patient ID: " + id + ", Patient Name: " + name;
	}

	public ArrayList<AppointmentRequest> getRequests() {
		return requests;
	}

	public int getUnreadIndex() {
		return unreadIndex;
	}
	
	public void setUnreadIndex(int i) {
		this.unreadIndex = i;
	}

}

