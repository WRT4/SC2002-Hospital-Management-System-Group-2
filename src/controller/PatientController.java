package controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import model.*;

public class PatientController {
	
	static Scanner scanner = new Scanner(System.in);
	public static void viewRecord(Patient patient) {
		patient.getRecord().printMedicalRecord();
	}
	
	public static int getChoice() {
        System.out.print("Enter choice: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Enter a number.");
            scanner.next();
        }
        return scanner.nextInt();
    }
	
	public static void sendCancellationMessage(Appointment tempApp){
		String messageToDoctor;
		messageToDoctor = "Message at " + LocalDate.now() + ": Appointment cancelled by " + tempApp.getPatient().getName() + "\n" +
				"Appointment Details - " + tempApp.getTimeSlot().toString();
		tempApp.getDoctor().getMessages().add(0,messageToDoctor);
	}
	
	public static void sendRequestMessage(AppointmentRequest request, boolean rescheduled) {
		String messageToDoctor;
		if (rescheduled == false) {
			messageToDoctor = "Message at " + LocalDate.now() + ": Appointment Request sent by " + request.getPatient().getName() + "\n" +
					"Appointment Details - " + request.getTimeSlot().toString();
		}
		else {
			messageToDoctor = "Message at " + LocalDate.now() + ": Rescheduled appointment Request sent by " + request.getPatient().getName() + "\n" +
					"Appointment Details - " + request.getTimeSlot().toString();
		}
		request.getDoctor().getMessages().add(0,messageToDoctor);
	}
	
	public static void updatePersonalInfo(Patient patient) {
		System.out.println("1. Enter basic info");
		System.out.println("2. Update contact info");
		System.out.println("-1. Go back");
		int choice3;
		choice3 = getChoice();
		if (choice3 == 1) {
			enterBasicInfo(patient);
		}
		else if (choice3 == 2) {
			updateContactInfo(patient);
		}
	}
	
	public static void enterBasicInfo(Patient patient){
		System.out.println("Enter your full name: ");
		String name = scanner.nextLine();
		patient.setName(name);

		System.out.println("Enter your gender (Female / Male): ");
		String gender = scanner.next();
		patient.setGender(gender);

		System.out.println("Enter your date of birth (in DD/MM/YYYY): ");
		String dob = scanner.next();
		patient.setDateOfBirth(dob);

	}
	
	public static void updateContactInfo(Patient patient) {
		// move to app class?
		MedicalRecord record = patient.getRecord();
		System.out.println("Current contact info: ");
		System.out.println("Name: " + record.getName());
		System.out.println("Email: " + record.getEmail());
		System.out.println("Date of Birth: " + record.getDateOfBirth());
		System.out.println("Phone Number: " + record.getPhoneNumber());
		System.out.println("1. Update Contact info");
		System.out.println("-1. Exit");
		int choice = 0, choice2 = 0;
		choice = getChoice();
		while(choice != 1 && choice != -1) {
			System.out.println("Option doesn't exist! ");
			choice = getChoice();
		}
		while (choice == 1) {
			System.out.println("What would you like to update?");
			System.out.println("1. Email");
			System.out.println("2. Phone Number");
			System.out.println("-1. Go back");
			choice2 = getChoice();
			if (choice2 == 1) {
				System.out.println("Enter new email: ");
				String email = scanner.next();
				patient.updateEmail(email);
			}
			else if (choice2 == 2) {
				System.out.println("Enter new Phone Number: ");
				String phoneNumber = scanner.next();
				patient.updatePhoneNumber(phoneNumber);
			}
			else break;
		}
		if (choice == -1) {
			return;
		}
	}
	
	public static void viewAvailableSlots() {
		System.out.println("Viewing available slots: ");
		System.out.println("Enter date: ");
		LocalDate date = ScheduleController.inputDate(true);
		if(date == null) return;

		else if (date.isBefore(LocalDate.now())) {
			while (date.isBefore(LocalDate.now())) {
				System.out.println("Date has lapsed! Unable to schedule!");
				date = ScheduleController.inputDate(true);
				if (date == null) return;
			}
		}

		String docInputID;
		System.out.println("List of doctors: ");
		for (Doctor doctor : Database.doctors) {
			System.out.println(doctor.getId() + "- " + doctor.getName());
		}
		System.out.println("Enter the ID of doctor to check available slots:");
		docInputID = scanner.next();
		for (Doctor doctor : Database.doctors) {
			if (docInputID.compareTo(doctor.getId())==0){
				ScheduleController.viewAvailableSlots(date, doctor.getSchedule());
				break;
			}
		}
	}
	
	public static void scheduleAppointment(Patient patient) {
		for (Doctor doctor : Database.doctors) {
			System.out.println(doctor);
		}
		System.out.println("Enter Doctor id or -1 to go back: ");
		String id = scanner.next();
		if (id.equals("-1")) return;
		Doctor doctor = null;
		for (Doctor doctor1 : Database.doctors) {
			if (doctor1.getId().equals(id)) doctor = doctor1;
		}
		if (doctor == null) {
			System.out.println("Doctor not found!");
			return;
		}

		//input date time--start
		LocalDate date = ScheduleController.inputDate(true);
		if (date == null) return;

		LocalTime time = ScheduleController.inputTime();
		if (time == null) return;

		while (date.equals(LocalDate.now()) && time.isBefore(LocalTime.now())) {
			System.out.println("Time has lapsed! Please re-enter!");
			time = ScheduleController.inputTime();
			if (time == null) return;
		}
		//input date time--end
		TimeSlot requestTime = doctor.findTimeSlot(date, time);
		if (requestTime == null) return;

		if (doctor.getSchedule().checkOverlapping(requestTime)){
			System.out.println("Doctor " + doctor.getName() + " is not available at " + requestTime.toString());
			System.out.println("Please try again.");
		}
		else{
			AppointmentRequest temp = new AppointmentRequest(date, requestTime, doctor, patient);
			patient.getRequests().add(temp);
			doctor.addRequest(temp);
			sendRequestMessage(temp, false);
			System.out.println("Appointment Request Sent!");
		}
	}
	
	
	public static void rescheduleAppointments(Patient patient) {
		int num = 0;
		ArrayList<Appointment> appointments = patient.getAppointments();
		for (Appointment appointment : appointments) {
			if (appointment.getStatus() == Status.CONFIRMED) {
				System.out.println(appointment);
				num++;
			}
		}
		if (num == 0) {
			System.out.println("No scheduled appointments!");
			return;
		}
		System.out.println("Which appointment ID would you like to cancel? Enter Appointment ID or -1 to exit. ");
		Appointment temp = DoctorController.findAppointment(appointments, true);
		if (temp == null) return;
		
		// reschedule and free slot
		System.out.println("Changing appointment " + temp.getAppointmentID() + " ...");
		System.out.println("Enter new date: ");

		LocalDate date = ScheduleController.inputDate(true);
		if (date == null) return;

		LocalTime time = ScheduleController.inputTime();
		if (time == null) return;

		while (date.equals(LocalDate.now()) && time.isBefore(LocalTime.now())) {
			System.out.println("Time has lapsed! Please re-enter!");
			time = ScheduleController.inputTime();
			if (time == null) return;
		}

		TimeSlot requestTime = temp.getDoctor().findTimeSlot(date, time);
		if (requestTime == null) return;

		if (temp.getDoctor().getSchedule().checkOverlapping(requestTime)){
			System.out.println("Doctor " + temp.getDoctor().getName() + " is not available at " + requestTime.toString());
			System.out.println("Please try again.");
			return;
		}
		temp.setStatus(Status.CANCELLED);
		AppointmentRequest request = new AppointmentRequest(date, requestTime ,temp.getDoctor(), temp.getPatient());
		temp.getDoctor().addRequest(request);
		patient.getRequests().add(request);
		temp.getDoctor().getSchedule().setAvailability(temp.getDate(), temp.getTimeSlot());
		sendCancellationMessage(temp);
		sendRequestMessage(request,true);
		System.out.println("Successfully Rescheduled! New request sent!");
	}
	
	public static void cancelAppointment (Patient patient) {
		int num = 0;
		ArrayList<Appointment> appointments = patient.getAppointments();
		for (Appointment appointment : appointments) {
			if (appointment.getStatus() == Status.CONFIRMED) {
				System.out.println(appointment);
				num++;
			}
		}
		if (num == 0) {
			System.out.println("No scheduled appointments!");
			return;
		}
		System.out.println("Which appointment would you like to cancel? Enter Appointment ID or -1 to exit.");
		Appointment temp = DoctorController.findAppointment(appointments, true);
		if (temp == null) return;
		temp.getDoctor().getSchedule().setAvailability(temp.getDate(), temp.getTimeSlot());
		temp.setStatus(Status.CANCELLED);

		sendCancellationMessage(temp);
		System.out.println("Successfully cancelled!");
	}
	
	public static void viewRequests(Patient patient) {
		// view status
		ArrayList<AppointmentRequest> requests = patient.getRequests();
		if (requests.size() == 0) {
			System.out.println("No requests made!");
			return;
		}
		for (AppointmentRequest request : requests) {
			System.out.println(request);
		}
		System.out.println("Would you like to cancel a request? 1. Yes -1. Exit ");
		int choice = getChoice();
		while(choice != 1 && choice != -1) {
			System.out.println("Option doesn't exist! ");
			choice = getChoice();
		}
		if (choice == -1) return;
		if (choice == 1) {
			System.out.println("Which requestID would would like to cancel? Enter ID or -1 to exit.");
			AppointmentRequest req = DoctorController.findRequest(requests, true);
			if (req == null) return;
			req.setStatus(Status.CANCELLED);
			System.out.println("Request Cancelled!");
		}
	}
	
	public static void viewScheduledAppointments(Patient patient) {
		// view status
		int i = 0;
		ArrayList<Appointment> appointments = patient.getAppointments();
		if (appointments.size() == 0) {
			System.out.println("No scheduled appointments!");
			return;
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
			return;
		}
	}
	
	public static void viewAppointmentOutcomes(Patient patient) {
		int num = 0;
		ArrayList<Appointment> appointments = patient.getAppointments();
		for (int i = 0; i < appointments.size(); i++) {
			if (appointments.get(i).getStatus() == Status.COMPLETED) {
				appointments.get(i).printAppointmentOutcome();
				num++;
			}
			else if (appointments.get(i).getStatus() == Status.CANCELLED) {
				appointments.get(i).printCancelledAppointments();
				num++;
			}
		}
		if (num == 0) {
			System.out.println("No completed or cancelled appointments!");
		}
	}
	
	
}
