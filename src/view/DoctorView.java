package view;
import model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class DoctorView {
	private Scanner scanner;
	private final String RED = "\u001B[31m";
    private final String RESET = "\u001B[0m";
	
	public DoctorView(Scanner scanner) {
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
	
	public void viewMedicalRecords(Patient patient, Doctor doctor) {
		if (patient != null){
			patient.getRecord().printMedicalRecord();
		}
    }

	public void viewSchedule(Doctor doctor) {
    	System.out.println("Viewing Personal Schedule for Doctor " + doctor.getId() + ", Name: " + doctor.getName() + ": ");
		System.out.println("Enter date: ");
		LocalDate date = ScheduleView.inputDate(false);
		if (date == null) return;
		while (date.isBefore(doctor.getSchedule().getWorkingSlots().get(0).getDate())) {
			System.out.println("No record found! ");
			date = ScheduleView.inputDate(false);
			if (date == null) return;
		}
		ScheduleView.viewAllSlots(date, doctor.getSchedule());
    }
	
	public void viewRequests(Doctor doctor) {
		int num = 0;
		ArrayList<AppointmentRequest> requests = doctor.getRequests();
		for (AppointmentRequest request: requests) {
			if (request.getStatus() == Status.PENDING){
				System.out.println(request);
				num++;
			}
		}
		if (num == 0) {
			System.out.println("No appointment requests yet!");
			return;
		}
	}
	
	public int viewAppointments(Doctor doctor) {
    	int num = 0;
        for (Appointment appointment : doctor.getSchedule().getAppointments()) {
            if (appointment.getStatus() == Status.CONFIRMED) {
            	System.out.println(appointment);
            	num++;
            }
        }
        if (num == 0) {
			System.out.println("No scheduled appointments! ");
			return 0;
		}
        return 1;
    }
	
	public void viewAppointmentOutcomes(Doctor doctor) {
		int num1 = 0,num2 = 0;
		ArrayList<Appointment> appointments = doctor.getSchedule().getAppointments();
		if (appointments.size() == 0) {
			System.out.println("No scheduled appointments!");
			return;
		}
		System.out.println("Uncompleted appointments: ");
		for (Appointment appointment : appointments) {
			if (appointment.getStatus() == Status.CONFIRMED) {
				System.out.println(appointment);
				num1++;
			}
		}
		if (num1 == 0) {
			System.out.println("No Uncompleted appointments!");
		}
		System.out.println();
		System.out.println("Completed appointments: ");
		for (Appointment appointment : appointments) {
			if (appointment.getStatus() == Status.COMPLETED) {
				AppointmentView.printAppointmentOutcome(appointment);
				num2++;
			}
		}
		if (num2 == 0) {
			System.out.println("No completed appointments!");
		}
		System.out.println();
	}
	
	public Patient getPatient(Doctor doctor) {
		ArrayList<Patient> patientsUnderCare = new ArrayList<>(doctor.getAppointmentCounter().keySet());
		if (patientsUnderCare==null || patientsUnderCare.size()==0){
			System.out.println("No patients under care.");
			return null;
		}
        // Implement method to get a patient object
    	for (Patient patient : patientsUnderCare) {
    		System.out.println(patient);
    	}
        System.out.println("Enter Patient ID or -1 to exit: ");
        String choice = scanner.next();
        if (choice.equals("-1")) return null;
        for (Patient patient : patientsUnderCare) {
        	if (choice.equals(patient.getPatientId())) {
        		return patient;
        	}
        }
		System.out.println("Patient not found. ");
        return null;
    }
	
	public int viewInbox(Doctor doctor, int unreadIndex) {
		ArrayList<String> messages = doctor.getMessages();
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
	
	public void showMessageBox(Doctor doctor, int index) {
		ArrayList<String> messages = doctor.getMessages();
		System.out.println("Message Box: ");
		int counter = 0;
		for (int i = messages.size() - 1; i >= index; i--){
			System.out.println(RED + "UNREAD - " + messages.get(i) + RESET);
			System.out.println();
			counter++;
		}
		if (counter ==0)
			System.out.println("No unread messages!");
	}
	
	// @Override
    public void showMenu() {
    	System.out.println("Doctor Menu: ");
    	System.out.println("1. View inbox");
        System.out.println("2. View Patient Medical Records");
        System.out.println("3. Update Patient Medical Records");
        System.out.println("4. View Personal Schedule");
        System.out.println("5. Set Availability for Appointments / Update Personal Schedule");
        System.out.println("6. Accept or Decline Appointment Requests");
        System.out.println("7. View Upcoming Appointments / Cancel Appointment");
        System.out.println("8. Record Appointment Outcome");
        System.out.println("9. Logout");
        System.out.println("Choose an action:");
    }
}
