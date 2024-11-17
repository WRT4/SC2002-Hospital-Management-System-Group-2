package view;
import model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import enums.Status;

public class DoctorView extends UserView{
	
	public DoctorView(Scanner scanner) {
		super(scanner);
	}
	
	
	public void viewMedicalRecords(Patient patient) {
		if (patient != null){
			patient.getRecord().printMedicalRecord();
		}
    }

	public void viewSchedule(Doctor doctor) {
    	System.out.println("Viewing Personal Schedule for Doctor " + doctor.getID() + ", Name: " + doctor.getName() + ": ");
		System.out.println("Enter date: ");
		LocalDate date = ScheduleView.inputDate(false, scanner);
		if (date == null) return;
		while (date.isBefore(doctor.getSchedule().getWorkingSlots().get(0).getDate())) {
			System.out.println("No record found! ");
			date = ScheduleView.inputDate(false, scanner);
			if (date == null) return;
		}
		ScheduleView.viewAllSlots(date, doctor.getSchedule());
    }
	
	public void viewRequests(ArrayList<AppointmentRequest> requests) {
		int num = 0;
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
	
	public int viewAppointments(ArrayList<Appointment> appointments) {
    	int num = 0;
        for (Appointment appointment : appointments) {
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
	
	public void viewAppointmentOutcomes(ArrayList<Appointment> appointments) {
		int num1 = 0,num2 = 0;
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
	
	
	// @Override
    public void showMenu() {
    	System.out.println("\nDoctor Menu: ");
    	System.out.println("1. View Inbox");
        System.out.println("2. View Patient Medical Records");
        System.out.println("3. Update Patient Medical Records");
        System.out.println("4. View Personal Schedule");
        System.out.println("5. Set Availability for Appointments / Update Personal Schedule");
        System.out.println("6. Accept or Decline Appointment Requests");
        System.out.println("7. View Upcoming Appointments / Cancel Appointment");
        System.out.println("8. Record Appointment Outcome");
        System.out.println("9. Exit to Main Menu");
        System.out.println("Choose an action:");
    }
}
