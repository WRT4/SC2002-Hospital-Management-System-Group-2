package view;

import model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class PatientView {
	private Scanner scanner;
	private final String RED = "\u001B[31m";
    private final String RESET = "\u001B[0m";
	
	public PatientView(Scanner scanner){
		this.scanner = scanner;
	}
	
	public int getChoice() {
        System.out.print("Enter choice: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Enter a number.");
            scanner.next();
        }
        return scanner.nextInt();
    }
	
	public void viewRecord(Patient patient) {
		patient.getRecord().printMedicalRecord();
	}
	
	public void viewRequests(Patient patient) {
		// view status
		ArrayList<AppointmentRequest> requests = patient.getRequests();
		if (requests.size() == 0) {
			System.out.println("No requests made!");
			return;
		}
		for (AppointmentRequest request : requests) {
			System.out.println(request);
		}
	}
	
	public void viewScheduledAppointments(Patient patient) {
		// view status
		ArrayList<Appointment> appointments = patient.getAppointments();
		if (appointments.size() == 0) {
			System.out.println("No scheduled appointments!");
			return;
		}
		int num = 0;
		for (Appointment apt : appointments) {
			if (apt.getStatus() == Status.CONFIRMED) {
				AppointmentView.printScheduledAppointment(apt);
				num++;
			}
		}
		if (num == 0) {
			System.out.println("No scheduled appointments! ");
			return;
		}
		System.out.println("Note: Appointment request that hasn't been accepted by doctor could be found in Menu Option 7");
	}
	
	public void viewAppointmentOutcomes(Patient patient) {
		int num = 0;
		ArrayList<Appointment> appointments = patient.getAppointments();
		for (Appointment apt : appointments) {
			if (apt.getStatus() == Status.COMPLETED) {
				AppointmentView.printAppointmentOutcome(apt);
				num++;
			}
			else if (apt.getStatus() == Status.CANCELLED) {
				AppointmentView.printCancelledAppointments(apt);
				num++;
			}
		}
		if (num == 0) {
			System.out.println("No completed or cancelled appointments!");
		}
	}
	
	public void viewAvailableSlots() {
		System.out.println("Viewing available slots: ");
		System.out.println("Enter date: ");
		LocalDate date = ScheduleView.inputDate(true);
		if(date == null) return;

		String docInputID;
		System.out.println("List of doctors: ");
		for (Doctor doctor : Database.doctors) {
			System.out.println(doctor.getId() + "- " + doctor.getName());
		}
		System.out.println("Enter the ID of doctor to check available slots:");
		docInputID = scanner.next();
		for (Doctor doctor : Database.doctors) {
			if (docInputID.compareTo(doctor.getId())==0){
				ScheduleView.viewAvailableSlots(date, doctor.getSchedule());
				break;
			}
		}
	}
	
	public int viewInbox(Patient patient, int unreadIndex) {
		ArrayList<String> messages = patient.getMessages();
		System.out.println("Inbox:\n");
		int counter = 0;
		System.out.println("Unread Messages:");
		for (int i = messages.size()-1; i >= unreadIndex; i--) {
			System.out.println(RED + (i+1) + ". " +  "UNREAD - " + messages.get(i) + RESET);
			System.out.println();
			counter++;
		}
		if (counter == 0) System.out.println("No unread messages!\n");
		int counter2 = 0;
		System.out.println("Message history:");
		for (int i = unreadIndex - 1; i >= 0; i--) {
			System.out.println((i+1) + ". " + messages.get(i));
			System.out.println();
			counter++;
		}
		if (counter2 == 0) System.out.println("No message history!\n");
		return messages.size();
	}
	
	public void showMessageBox(Patient patient, int index) {
		ArrayList<String> messages = patient.getMessages();
		System.out.println("Message Box: ");
		int counter = 0;;
		for (int i = messages.size() - 1; i >= index; i--){
			System.out.println(RED + "UNREAD - " + messages.get(i) + RESET);
			System.out.println();
			counter++;
		}
		if (counter ==0)
			System.out.println("No unread messages!");
	}
	
	public void showMenu() {
		System.out.println("Patient Menu: ");
		System.out.println("1. View inbox");
		System.out.println("2. View medical record");
		System.out.println("3. Enter and update personal information");
		System.out.println("4. View available appointment slots");
		System.out.println("5. Schedule an appointment");
		System.out.println("6. Reschedule an appointment");
		System.out.println("7. Cancel a scheduled appointment");
		System.out.println("8. View/Cancel appointment requests");
		System.out.println("9. View scheduled appointments");
		System.out.println("10. View past appointment outcome records");
		System.out.println("11. Logout");
		System.out.println("Choose an action:");
	}
}
