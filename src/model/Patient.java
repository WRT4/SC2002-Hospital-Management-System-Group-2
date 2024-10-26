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
	
	public Patient(String id, String name, MedicalRecord record) {
		super(id,name,"Patient");
		this.record = record;
		this.appointments = new ArrayList<Appointment>();
		this.requests = new ArrayList<AppointmentRequest>();
	}
	
	public void showMenu() {
		int choice = -1;
		do {
			System.out.println("Patient Menu: ");
			System.out.println("1. View medical record");
			System.out.println("2. Update personal information");
			System.out.println("3. View available appointment slots");
			System.out.println("4. Schedule an appointment");
			System.out.println("5. Reschedule an appointment");
			System.out.println("6. Cancel an appointment");
			System.out.println("7. View appointment requests");
			System.out.println("8. View scheduled appointments");
			System.out.println("9. View past appointment outcome recrods");
			System.out.println("10. Logout");
			choice = getChoice();
			while (choice < 1 || choice > 10) {
				System.out.println("No such option! ");
				choice = getChoice();
			}
			switch (choice) {
				case 1:
					viewRecord();
					break;
				case 2:
					updateContactInfo();
					break;
				case 3:
					viewAvailableSlots();
					break;
				case 4:
					scheduleAppointment();
					break;
				case 5:
					rescheduleAppointments();
					break;
				case 6:
					cancelAppointment();
					break;
				case 7:
					viewRequests();
					break;
				case 8:
					viewScheduledAppointments();
					break;
				case 9:
					viewAppointmentOutcomes();
					break;
				case 10:
					System.out.println("Logging out...");
			}
		}
		while (choice != 10);
	}
	
	public void viewAvailableSlots() {
		System.out.println("Viewing available slots: ");
		System.out.println("Enter date: ");
		LocalDate date = Schedule.inputDate();
		if(date == null) return;
		for (Doctor doctor : Database.doctors) {
			System.out.println("Schedule for Doctor " + doctor.getName() + ":");
			doctor.getSchedule().viewAvailableSlots(date);
		}
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
	
	private int getChoice() {
		int choice = -1;
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("Enter choice: ");
			try {
				choice = sc.nextInt();
				break;
			}
			catch (Exception e) {
				System.out.println("Error input! Try again!");
				sc.nextLine();
			}
		}
		return choice;
	}
	
	public void viewRecord() {
		record.printMedicalRecord();
	}
	
	public void updateContactInfo() {
		// move to app class?
		System.out.println("Current contact info: ");
		System.out.println("Name: " + name);
		System.out.println("Email: " + record.getEmail());
		System.out.println("Date of Birth: " + record.getDateOfBirth());
		System.out.println("Phone Number: " + record.getPhoneNumber());
		System.out.println("1. Update Contact info");
		System.out.println("-1. Exit");
		int choice = 0, choice2 = 0;
		Scanner sc = new Scanner(System.in);
		choice = getChoice();
		while(choice != 1 && choice != -1) {
			System.out.println("Option doesn't exist! ");
			choice = getChoice();
		}
		if (choice == 1) {
			System.out.println("What would you like to change?");
			System.out.println("1. Email");
			System.out.println("2. Phone Number");
			System.out.println("-1. Go back");
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
			else return;
		}
		else if (choice == -1) {
			return;
		}
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
	
	public void scheduleAppointment() {
		Scanner sc = new Scanner(System.in);
		for (Doctor doctor : Database.doctors) {
			System.out.println(doctor.toString());
		}
		System.out.println("Enter Doctor id or -1 to go back: ");
		String id = sc.next();
		if (id.equals("-1")) return;
		Doctor doctor = null;
		for (Doctor doctor1 : Database.doctors) {
			if (doctor1.getId().equals(id)) doctor = doctor1;
		}
		if (doctor == null) {
			System.out.println("Doctor not found!");
			return;
		}
		LocalDate date = Schedule.inputDate();
		if (date == null) return;
		LocalTime time = Schedule.inputTime();
		if (time == null) return;
		TimeSlot requestTime = doctor.findTimeSlot(date, time);
		if (requestTime == null) return;
		//2. ask for duration -- 30 minutes is default ---------------- later

		if (doctor.getSchedule().checkOverlapping(requestTime)){
			System.out.println("Doctor " + doctor.getName() + " is not available at " + requestTime.toString());
			System.out.println("Please try again.");
		}
		else{
			AppointmentRequest temp = new AppointmentRequest(date, requestTime, doctor, this);
			requests.add(temp);
			doctor.addRequest(temp);
		}
		System.out.println("Appointment Request Sent!");
	}
	
	public void rescheduleAppointments() {
		// move to app class?
		for (Appointment appointment : appointments) {
			if (appointment.getStatus() == Status.CONFIRMED) {
				System.out.println(appointment);
			}
		}
		System.out.println("Which appointment ID would you like to reschedule? Enter -1 to go back ");
		int choice = getChoice();
		while (choice > appointments.size() || (choice < 1 && choice != -1)) {
			System.out.println("Error input! Try again! ");
			choice = getChoice();
		}
		if (choice == -1) return;
		System.out.println("Changing appointment " + choice + " ...");
		Appointment temp = null;
		for (Appointment appointment : appointments) {
			if (appointment.getAppointmentID() == choice) {
				temp = appointment;
				break;
			}
		}
		// display available timeslots
//		for (Doctor doctor : Database.doctors) {
//			System.out.println("Schedule for Doctor " + doctor.getName());
//			doctor.viewSchedule();
//		}
		if (temp == null) {
			System.out.println("Error! AppointmentID does not exist!");
			return;
		}
		// reschedule and free slot
		System.out.println("Enter new date: ");
		LocalDate date = Schedule.inputDate();
		if (date == null) return;
		System.out.println("Enter new time: ");
		LocalTime time = Schedule.inputTime();
		if (time == null) return;
		TimeSlot requestTime = temp.getDoctor().findTimeSlot(date, time);
		if (requestTime == null) return;
		//2. ask for duration -- 30 minutes is default ---------------- later

		if (temp.getDoctor().getSchedule().checkOverlapping(requestTime)){
			System.out.println("Doctor " + temp.getDoctor().getName() + " is not available at " + requestTime.toString());
			System.out.println("Please try again.");
			return;
		}
		temp.setStatus(Status.CANCELLED);
		AppointmentRequest request = new AppointmentRequest(date, requestTime ,temp.getDoctor(), temp.getPatient());
		temp.getDoctor().addRequest(request);
		requests.add(request);
		temp.getDoctor().getSchedule().setAvailability(temp.getDate(), temp.getTimeSlot());
		System.out.println("Successfully Rescheduled! New request sent!");
	}
	
	public void cancelAppointment () {
		Scanner sc = new Scanner(System.in);
		for (Appointment apt : appointments) {
			if (apt.getStatus() == Status.CONFIRMED) {
				System.out.println(apt);
			}
		}
		System.out.println("Which appointment would you like to cancel? Enter Appointment ID");
		int apptID = sc.nextInt();
		Appointment temp = null;
		for (Appointment appointment : appointments) {
			if (appointment.getAppointmentID() == apptID) {
				temp = appointment;
			}
				
		}
		if (temp == null) {
			System.out.println("Error! Appointment does not exist!");
			return;
		}
		if (temp.getStatus() != Status.CONFIRMED) {
			System.out.println("Error! Appointment is COMPLETED or CANCELLED!");
			return;
		}
		temp.getDoctor().getSchedule().setAvailability(temp.getDate(), temp.getTimeSlot());
		temp.setStatus(Status.CANCELLED);
		System.out.println("Successfully cancelled!");
	}
	
	public void viewScheduledAppointments() {
		// view status
		int i = 0;
		if (appointments.size() == 0) {
			System.out.println("No scheduled appointments");
		}
		int num = 0;
		for (i = 0; i < appointments.size(); i++) {
			if (appointments.get(i).getStatus() == Status.CONFIRMED) {
				appointments.get(i).printScheduledAppointment();
				num++;
			}
		}
		if (num == 0) {
			System.out.println("No scheduled appointments! ");
		}
	}
	
	public void viewRequests() {
		// view status
		if (requests.size() == 0) {
			System.out.println("No requests made!");
			return;
		}
		for (AppointmentRequest request : requests) {
			System.out.println(request);
		}
		System.out.println("Would you like to cancel? 1. Yes -1. Go back ");
		int choice = getChoice();
		while(choice != 1 && choice != -1) {
			System.out.println("Option doesn't exist! ");
			choice = getChoice();
		}
		if (choice == -1) return;
		if (choice == 1) {
			System.out.println("Which requestID would would like to cancel? Enter ID or -1 to go back ");
			int id = getChoice();
			if (id == -1) return;
			AppointmentRequest req = null;
			for (AppointmentRequest request : requests) {
				if (request.getRequestID() == id) {
					req = request;
				}
			}
			if (req == null) {
				System.out.println("Request does not exist!");
				return;
			}
			if (req.getStatus() != Status.PENDING) {
				if (req.getStatus() == Status.CANCELLED) {
					System.out.println("Request already cancelled!");
					return;
				}
				else if (req.getStatus() == Status.ACCEPTED) {
					System.out.println("Request already accepted! Cancel appointment instead!");
					return;
				}
				else if (req.getStatus() == Status.DECLINED) {
					System.out.println("Request already declined!");
					return;
				}
			}
			else {
				req.setStaus(Status.CANCELLED);
				System.out.println("Request Cancelled!");
			}
		}
	}
	
	public void viewAppointmentOutcomes() {
		for (int i = 0; i < appointments.size(); i++) {
			if (appointments.get(i).getStatus() == Status.CONFIRMED) {
				appointments.get(i).printAppointmentOutcome();
			}
			else if (appointments.get(i).getStatus() == Status.CANCELLED) {
				appointments.get(i).printCancelledAppointments();;
			}
		}
	}
	
	public MedicalRecord getRecord() {
		return record;
	}

	public ArrayList<Appointment> getAppointments() {
		return appointments;
	}
	
	public String toString() {
		return "Patient ID: " + id + " Patient Name: " + name; 
	}

}

