package controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

import model.Appointment;
import model.AppointmentRequest;
import model.Database;
import model.Doctor;
import model.Patient;
import model.Status;
import model.TimeSlot;
import view.AppointmentView;
import view.PatientView;
import view.ScheduleView;

public class PatientController {

	private Scanner scanner;
	private Patient patient;
	private PatientView patientView;
	
	public PatientController(Patient patient, Scanner scanner) {
		this.patient = patient;
		this.scanner = scanner;
		patientView = new PatientView(scanner);
	}
	
	public void showMenu() {
		System.out.println("Message Box: ");
		int counter = 0;
		for (String message: patient.getMessages()){
			System.out.println((counter+1) + " - " + message);
			System.out.println();
			counter++;
		}
		if (counter ==0)
			System.out.println("No messages yet!");

		int choice = -1;
		do {
			patientView.showMenu();
			choice = patientView.getChoice();
			while (choice < 1 || choice > 10) {
				System.out.println("No such option! ");
				choice = patientView.getChoice();
			}
			switch (choice) {
				case 1:
					patientView.viewRecord(patient);
					break;
				case 2:
					updatePersonalInfo();
					break;
				case 3:
					patientView.viewAvailableSlots();
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
					patientView.viewScheduledAppointments(patient);
					break;
				case 9:
					patientView.viewAppointmentOutcomes(patient);
					break;
				case 10:
					System.out.println("Logging out...");
			}
		}
		while (choice != 10);
	}
	
	public void updatePersonalInfo() {
		System.out.println("1. Enter basic info");
		System.out.println("2. Update contact info");
		System.out.println("-1. Go back");
		int choice3;
		choice3 = patientView.getChoice();
		scanner.nextLine();
		if (choice3 == 1) {
			enterBasicInfo();
		}
		else if (choice3 == 2) {
			updateContactInfo();
		}
	}
	
	public void updateContactInfo() {
		patient.getRecord().viewContactInfo();
		System.out.println("1. Update Contact info");
		System.out.println("-1. Exit");
		int choice = 0, choice2 = 0;
		choice = patientView.getChoice();
		while(choice != 1 && choice != -1) {
			System.out.println("Option doesn't exist! ");
			choice = patientView.getChoice();
		}
		while (choice == 1) {
			System.out.println("What would you like to update?");
			System.out.println("1. Email");
			System.out.println("2. Phone Number");
			System.out.println("-1. Go back");
			choice2 = patientView.getChoice();
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
	
	public void enterBasicInfo(){
		System.out.println("Enter your full name: ");
		String name = scanner.nextLine();
		patient.setName(name);

		System.out.println("Enter your gender (Female / Male): ");
		String gender = scanner.next();
		patient.setGender(gender);

		System.out.println("Enter your date of birth (in DD-MM-YYYY): ");
		String dob = scanner.next();
		patient.setDateOfBirth(dob);
	}
	
	public void scheduleAppointment() {
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
		LocalDate date = ScheduleView.inputDate(true);
		if (date == null) return;

		LocalTime time = ScheduleView.inputTime();
		if (time == null) return;

		while (date.equals(LocalDate.now()) && time.isBefore(LocalTime.now())) {
			System.out.println("Time has lapsed! Please re-enter!");
			time = ScheduleView.inputTime();
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
	
	public void rescheduleAppointments() {
		ArrayList<Appointment> appointments = patient.getAppointments();
		patientView.viewScheduledAppointments(patient);
		System.out.println("Which appointment ID would you like to cancel? Enter Appointment ID or -1 to exit. ");
		Appointment temp = AppointmentView.promptForAppointment(appointments, true, scanner);
		if (temp == null) return;
		
		// reschedule and free slot
		System.out.println("Changing appointment " + temp.getAppointmentID() + " ...");
		System.out.println("Enter new date: ");

		LocalDate date = ScheduleView.inputDate(true);
		if (date == null) return;

		LocalTime time = ScheduleView.inputTime();
		if (time == null) return;

		while (date.equals(LocalDate.now()) && time.isBefore(LocalTime.now())) {
			System.out.println("Time has lapsed! Please re-enter!");
			time = ScheduleView.inputTime();
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
		temp.getDoctor().subtractAppointmentCounter(patient);
		//new appointment - in the form of a request
		AppointmentRequest request = new AppointmentRequest(date, requestTime ,temp.getDoctor(), temp.getPatient());
		temp.getDoctor().addRequest(request);
		patient.getRequests().add(request);
		temp.getDoctor().getSchedule().setAvailability(temp.getDate(), temp.getTimeSlot());
		sendCancellationMessage(temp);
		sendRequestMessage(request,true);
		System.out.println("Successfully Rescheduled! New request sent!");
	}
	
	public void cancelAppointment () {
		int num = 0;
		ArrayList<Appointment> appointments = patient.getAppointments();
		for (Appointment appointment : appointments) {
			if (appointment.getStatus() == Status.CONFIRMED) {
				System.out.println(appointment);
				num++;
			}
		}
		System.out.println("Note: Appointment request that hasn't been accepted by doctor could be found in Menu Option 7");
		if (num == 0) {
			System.out.println("No scheduled appointments!");
			System.out.println("Note: Appointment request that hasn't been accepted by doctor could be found in Menu Option 7");
			return;
		}
		System.out.println("Which appointment would you like to cancel? Enter Appointment ID or -1 to exit.");
		Appointment temp = AppointmentView.promptForAppointment(appointments, true, scanner);
		if (temp == null) return;
		temp.getDoctor().getSchedule().setAvailability(temp.getDate(), temp.getTimeSlot());
		temp.setStatus(Status.CANCELLED);
		temp.getDoctor().subtractAppointmentCounter(patient);
		sendCancellationMessage(temp);
		System.out.println("Successfully cancelled!");
	}
	
	public void viewRequests() {
		// view status
		ArrayList<AppointmentRequest> requests = patient.getRequests();
		patientView.viewRequests(patient);
		System.out.println("Would you like to cancel a request? 1. Yes -1. Exit ");
		int choice = patientView.getChoice();
		while(choice != 1 && choice != -1) {
			System.out.println("Option doesn't exist! ");
			choice = patientView.getChoice();
		}
		if (choice == -1) return;
		if (choice == 1) {
			System.out.println("Which requestID would would like to cancel? Enter ID or -1 to exit.");
			AppointmentRequest req = RequestController.findRequest(requests, true, scanner);
			if (req == null) return;
			req.setStatus(Status.CANCELLED);
			System.out.println("Request Cancelled!");
		}
	}
}
