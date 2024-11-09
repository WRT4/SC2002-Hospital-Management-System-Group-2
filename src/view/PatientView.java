package view;

import model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class PatientView {
	private Scanner scanner;
	
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
	
	public void showMenu() {
		System.out.println("Patient Menu: ");
		System.out.println("1. View medical record");
		System.out.println("2. Enter and update personal information");
		System.out.println("3. View available appointment slots");
		System.out.println("4. Schedule an appointment");
		System.out.println("5. Reschedule an appointment");
		System.out.println("6. Cancel a scheduled appointment");
		System.out.println("7. View/Cancel appointment requests");
		System.out.println("8. View scheduled appointments");
		System.out.println("9. View past appointment outcome records");
		System.out.println("10. Logout");
		System.out.println("Choose an action:");
	}
}
