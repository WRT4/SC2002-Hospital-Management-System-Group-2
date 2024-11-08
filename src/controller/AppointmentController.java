package controller;

import java.util.ArrayList;
import java.util.Scanner;
import model.Appointment;
// import model.Medication;
import model.Status;


public class AppointmentController {
	public static Appointment findAppointment(ArrayList<Appointment> appointments, boolean noConfirmed, int id) {
	    Appointment apt = null;
	    for (Appointment appointment : appointments) {
	        if (appointment.getAppointmentID() == id) {
	            apt = appointment;
	            break;
	        }
	    }
	    
	    if (apt == null) {
	        throw new RuntimeException("Appointment ID does not exist!");
	    }
	    if (apt.getStatus() == Status.CANCELLED) {
	        throw new RuntimeException("Appointment has been cancelled!");
	    }
	    if (noConfirmed && apt.getStatus() == Status.COMPLETED) {
	        throw new RuntimeException("Appointment has been completed!");
	    }
	    
	    return apt;
	}

	
	public static void setPrescription(Appointment apt, Scanner scanner) {
		//change this 
		System.out.println("Setting prescription...");
		System.out.println("Enter prescription: ");
		String pres = scanner.next();
		apt.setPrescription(pres);
		apt.getPatient().getRecord().addPrescription(pres);
		apt.setPrescriptionStatus(Status.PENDING);
		// send prescription request
		System.out.println("Prescription added!");
	}
	
	public static void setServiceType(Appointment apt, Scanner scanner) {
		System.out.println("Setting Service Type...");
		System.out.println("Enter Service Type: ");
		String ser = scanner.next();
		apt.setServiceType(ser);
		System.out.println("Service Type added!");
	}
	
	public static void setNotes(Appointment apt, Scanner scanner) {
		System.out.println("Setting Consultation notes...");
		System.out.println("Enter Consultation notes: ");
		scanner.nextLine();
		String note = scanner.nextLine();
		apt.setNotes(note);
		System.out.println("Consultation notes added!");
	}
	
}
