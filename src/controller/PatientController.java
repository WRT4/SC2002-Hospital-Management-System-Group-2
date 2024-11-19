package controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import model.Appointment;
import model.AppointmentRequest;
import application.Database;
import model.Doctor;
import model.Patient;
import model.TimeSlot;
import enums.Status;
import view.AppointmentView;
import view.PatientView;
import view.ScheduleView;

public class PatientController extends SessionController{
	
	private Patient patient;
	private PatientView patientView;
	
	public PatientController(Patient patient, Scanner scanner) {
		this.patient = patient;
		this.scanner = scanner;
		patientView = new PatientView(scanner);
		unreadIndex = patient.getUnreadIndex();
		startTime = LocalTime.now();
		startDate = LocalDate.now();
	}
	
	public void showMenu() {
		int choice = -1;
		do {
			patientView.showMessageBox(patient.getMessages(), unreadIndex);
			patientView.showMenu();
			choice = patientView.getChoice();
			scanner.nextLine(); 
			switch (choice) {
				case 1:
					unreadIndex = patientView.viewInbox(patient.getMessages(), unreadIndex);
					patient.setUnreadIndex(unreadIndex);
            		break;
				case 2:
					patientView.viewRecord(patient.getRecord());
					break;
				case 3:
					updatePersonalInfo();
					break;
				case 4:
					System.out.println("\nViewing available slots: \n");
					System.out.println("Enter date: ");
					patientView.viewAvailableSlots(inputDate());
					break;
				case 5:
					scheduleAppointment();
					break;
				case 6:
					rescheduleAppointments();
					break;
				case 7:
					cancelAppointment();
					break;
				case 8:
					viewRequests();
					break;
				case 9:
					patientView.viewScheduledAppointments(patient.getAppointments());
					break;
				case 10:
					PatientView.viewAppointmentOutcomes(patient.getAppointments());
					break;
				case 11:
					System.out.println("Exiting Patient menu...");
					String log = "Patient " + patient.getID() + " accessed system from " + startTime.format(FORMATTER) + " on " + startDate + " to " + LocalTime.now().format(FORMATTER) + " on " + LocalDate.now(); 
                    Database.SYSTEM_LOGS.add(log);
			}
		}
		while (choice != 11);
	}
	
	public void updatePersonalInfo() {
		System.out.println("\nUpdating Personal Information...\n");
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
	
	private LocalDate inputDate() {
		LocalDate finalDate = ScheduleView.inputDate(scanner);
		if (finalDate == null) return null;
		
		//system check :patient cannot book backwards in time
		while (finalDate.isBefore(LocalDate.now()) ){
			System.out.println("Date has lapsed! Unable to perform operation!");
			System.out.println("Please re-enter.");
			finalDate = ScheduleView.inputDate(scanner);
			if (finalDate == null) return null;
		}

		return finalDate;
	}
	
	public void updateContactInfo() {
		System.out.println("\nUpdating Contact Information...\n");
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
		tempApp.getDoctor().getMessages().add(messageToDoctor);
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
		request.getDoctor().getMessages().add(messageToDoctor);
	}
	
	public void enterBasicInfo(){
		System.out.println("\nEntering Basic Information...\n");
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
		System.out.println("\nScheduling Appointment...\n");
		patientView.viewDoctors();
		String id = patientView.enterID();
		if (id.equals("-1")) return;
		Doctor doctor = null;
		for (Doctor doctor1 : Database.DOCTORS) {
			if (doctor1.getID().equals(id)) doctor = doctor1;
		}
		if (doctor == null) {
			System.out.println("Doctor not found!");
			return;
		}

		ArrayList<Object> tempArr = getDateAndTime();
		if (tempArr == null) return;
		LocalDate date = (LocalDate) tempArr.get(0);
		LocalTime time = (LocalTime) tempArr.get(1);
		
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
		System.out.println("\nRescheduling Appointment...\n");
		ArrayList<Appointment> appointments = patient.getAppointments();
		patientView.viewScheduledAppointments(appointments);
		System.out.println("Which appointment ID would you like to cancel? Enter Appointment ID or -1 to exit. ");
		Appointment temp = AppointmentView.promptForAppointment(appointments, true, scanner);
		if (temp == null) return;
		
		// reschedule and free slot
		System.out.println("Changing appointment " + temp.getAppointmentID() + " ...");
		System.out.println("Enter new date: ");

		ArrayList<Object> tempArr = getDateAndTime();
		if (tempArr == null) return;
		LocalDate date = (LocalDate) tempArr.get(0);
		LocalTime time = (LocalTime) tempArr.get(1);

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
		temp.getDoctor().getSchedule().setAvailability(temp.getTimeSlot());
		sendCancellationMessage(temp);
		sendRequestMessage(request,true);
		System.out.println("Successfully Rescheduled! New request sent!");
	}
	
	private ArrayList<Object> getDateAndTime(){
		ArrayList<Object> arr = new ArrayList<>();
		//input date time--start
		LocalDate date = inputDate();
		if (date == null) return null;

		LocalTime time = ScheduleView.inputTime(scanner);
		if (time == null) return null;
		
		// check if time is in the past
		while (date.equals(LocalDate.now()) && time.isBefore(LocalTime.now())) {
			System.out.println("Time has lapsed! Please re-enter!");
			time = ScheduleView.inputTime(scanner);
			if (time == null) return null;
		}
		
		// check for if there is already an appointment at requested time
		while (patient.checkOverlapping(date, time)) {
			System.out.println("You already have a scheduled appointment at this time! Please re-enter!");
			time = ScheduleView.inputTime(scanner);
			if (time == null) return null;
		}
		arr.add(date);
		arr.add(time);
		return arr;
	}
	
	public void cancelAppointment () {
		System.out.println("\nCancelling Appointment...\n");
		int num = 0;
		ArrayList<Appointment> appointments = patient.getAppointments();
		for (Appointment appointment : appointments) {
			if (appointment.getStatus() == Status.CONFIRMED) {
				System.out.println(appointment);
				num++;
			}
		}
		System.out.println("Note: Appointment request that hasn't been accepted by doctor could be found in Menu Option 8");
		if (num == 0) {
			System.out.println("No scheduled appointments!");
			System.out.println("Note: Appointment request that hasn't been accepted by doctor could be found in Menu Option 8");
			return;
		}
		System.out.println("Which appointment would you like to cancel? Enter Appointment ID or -1 to exit.");
		Appointment temp = AppointmentView.promptForAppointment(appointments, true, scanner);
		if (temp == null) return;
		temp.getDoctor().getSchedule().setAvailability(temp.getTimeSlot());
		temp.setStatus(Status.CANCELLED);
		temp.getDoctor().subtractAppointmentCounter(patient);
		sendCancellationMessage(temp);
		System.out.println("Successfully cancelled!");
	}
	
	public void viewRequests() {
		ArrayList<AppointmentRequest> requests = patient.getRequests();
		patientView.viewRequests(requests);
		System.out.println("Would you like to cancel a request? 1. Yes -1. Exit ");
		int choice = patientView.getChoice();
		while(choice != 1 && choice != -1) {
			System.out.println("Option doesn't exist! ");
			choice = patientView.getChoice();
		}
		if (choice == -1) return;
		if (choice == 1) {
			System.out.println("Which requestID would would like to cancel? Enter ID or -1 to exit.");
			AppointmentRequest req = findRequest(requests);
			if (req == null) return;
			req.setStatus(Status.CANCELLED);
			System.out.println("Request Cancelled!");
		}
	}
	
	private AppointmentRequest findRequest(ArrayList<AppointmentRequest> requests) {
		AppointmentRequest request = null;
		while (true) {
			try {
				request = null;
				int requestID;
				System.out.println("Enter requestID or -1 to exit: ");
				requestID = patientView.getChoice();
				if (requestID == -1) return null;
				int i = 0;
				for (i = 0; i < requests.size(); i++) {
					if (requests.get(i).getRequestID() == requestID) {
						request = requests.get(i);
						break;
					}
				}
				if (request == null) throw new RuntimeException("RequestID does not exist!");
				if (request.getStatus() != Status.PENDING) {
					if (request.getStatus() == Status.CANCELLED) {
						throw new RuntimeException("Request already cancelled!");
					} else if (request.getStatus() == Status.ACCEPTED) {
						throw new RuntimeException("Request already accepted! Cancel appointment instead!");
					} else if (request.getStatus() == Status.DECLINED) {
						throw new RuntimeException("Request already declined!");
					}
				}
				break;
			} catch (InputMismatchException e) {
				System.out.println("Wrong input type! Try Again!");
				scanner.nextLine();
				continue;
			} catch (RuntimeException e) {
				System.out.println(e.getMessage());
				scanner.nextLine();
				continue;
			} catch (Exception e) {
				System.out.println("Error! Try Again!");
				scanner.nextLine();
				continue;
			}
		}
		return request;
	}
	
}
