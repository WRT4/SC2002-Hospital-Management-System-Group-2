package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Patient extends User {
	
	private MedicalRecord record;
	private ArrayList<Appointment> appointments;
	
	public Patient(String id, String name, MedicalRecord record) {
		super(id,name,"Patient");
		this.record = record;
	}
	
	public void showMenu() {
		System.out.println("1. View medical record");
		System.out.println("2. Update personal information");
		System.out.println("3. View available appointment slots");
		System.out.println("4. Schedule an appointment");
		System.out.println("5. Reschedule an appointment");
		System.out.println("6. Cancel an appointment");
		System.out.println("7. View scheduled appointments");
		System.out.println("8. View past appointment outcome recrods");
		System.out.println("9. Logout");
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
	
	public void setEmail(String email) {
		record.setEmail(email);
	}
	
	public void setPhoneNumber(String phoneNumber) {
	        record.setPhoneNumber(phoneNumber);
	}
	
	public int getChoice() {
		int choice = -1;
		System.out.println("Enter choice: ");
		Scanner sc = new Scanner(System.in);
		try {
			choice = sc.nextInt();
		}
		catch (Exception e) {
			System.out.println("Error input! Try again!");
			getChoice();
		}
		sc.close();
		return choice;
	}
	
	public void viewRecord() {
		record.toString();
	}
	
	public void updateContactInfo() {
		// move to app class?
		System.out.println("Current contact info: ");
		System.out.println("Name: " + name);
		System.out.println("Email: " + record.getEmail());
		System.out.println("Date of Birth: " + record.getDateOfBirth());
		System.out.println("Phone Number: " + record.getPhoneNumber());
		System.out.println("1. Update Contact info");
		System.out.println("2. Exit");
		int choice = -1, choice2 = -1;
		Scanner sc = new Scanner(System.in);
		do {
			choice = getChoice();
		}
		while(choice != 1 || choice != 2);
		if (choice == 1) {
			System.out.println("What would you like to change?");
			System.out.println("1. Email");
			System.out.println("2. Phone Number");
			choice2 = getChoice();
			if (choice2 == 1) {
				System.out.println("Enter new email: ");
				String email = sc.next();
				updateEmail(email);
			}
			else if (choice2 == 2) {
				System.out.println("Enter new Phone Number: ");
				String phoneNumber = sc.next();
				updatePhoneNumber(phoneNumber);
			}
		}
		sc.close();
	}
	
	public void updateContactInfo(String email, String phoneNumber) {
		if (email == null || phoneNumber != null) {
			record.setPhoneNumber(phoneNumber);
		}
		else if (email != null || phoneNumber == null) {
			record.setEmail(email);
		}
	}
	
	public void updateEmail(String email) {
		record.setEmail(email);
	}
	
	public MedicalRecord getMedicalRecord() {
		return record;
	}
	
	public void updatePhoneNumber(String number) {
		record.setPhoneNumber(number);
	}
	
	public void scheduleAppointment(Doctor doctor, LocalDate date, LocalTime time) {
		Appointment temp = new Appointment(this, doctor, date, time);
		appointments.add(temp);
		doctor.getRequest(temp.getRequest());
	}
	
	public void rescheduleAppointments(ArrayList<Doctor> doctors) {
		// move to app class?
		int j = 0;
		for (int i = 0; i < appointments.size(); i++) {
			if (appointments.get(i).getStatus() == "Confirmed") {
				j++;
				System.out.println(j + ". " + appointments.get(i).toString());
			}
		}
		System.out.println("Which appointment would you like to reschedule? ");
		int choice = getChoice();
		while (choice > appointments.size() || choice < 1) choice = getChoice();
		System.out.println("Changing appointment " + choice + " ...");
		Appointment temp = appointments.get(choice-1);
		// display available timeslots
		for (Doctor doctor : doctors) {
			System.out.println("Schedule for Doctor " + doctor.getName());
			doctor.viewSchedule();
		}
		// reschedule and free slot
		System.out.println("Enter new date: ");
		LocalDate date = Appointment.inputDate();
		System.out.println("Enter new time: ");
		LocalTime time = Appointment.inputTime();
		temp.setDate(date);
		temp.setTime(time);
		temp.getDoctor().getSchedule().setAvailability(date, time);
	}
	
	public void cancelAppoinment (LocalDate date, LocalTime time) throws RuntimeException {
		int i = 0;
		for (i = 0; i < appointments.size(); i++) {
			if (appointments.get(i).getDate() == date && appointments.get(i).getTime() == time ) {
				break;
			}
		}
		if (i == appointments.size()) {
			throw new RuntimeException("Error! Appointment does not exist!");
		}
		appointments.remove(i); 
	}
	
	public void viewScheduledAppointents() {
		// view status
		int i = 0;
		for (i = 0; i < appointments.size(); i++) {
			appointments.get(i).printScheduledAppointment();
		}
	}
	
	public void viewAppointmentOutcomes() {
		int j = 0;
		for (int i = 0; i < appointments.size(); i++) {
			if (appointments.get(i).getStatus() == "CONFIRMED") {
				j++;
				System.out.print(j+".");
				appointments.get(i).printAppointmentOutcome();
			}
		}
	}

}

