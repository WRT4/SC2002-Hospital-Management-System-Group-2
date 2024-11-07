package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.temporal.ChronoUnit;

public class Patient extends User {
	
	private MedicalRecord record;
	private ArrayList<Appointment> appointments;
	private ArrayList<AppointmentRequest> requests;
	private ArrayList<String> messages;
	
	Scanner scanner = new Scanner(System.in);
	
	public void showMenu() {}
	
	public Patient(String id, String name, MedicalRecord record) {
		super(id,name,"Patient");
		this.record = record;
		this.appointments = new ArrayList<Appointment>();
		this.requests = new ArrayList<AppointmentRequest>();
		this.messages = new ArrayList<String>();
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
	
	private int getChoice() {
        System.out.print("Enter choice: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Enter a number.");
            scanner.next();
        }
        return scanner.nextInt();
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

}

