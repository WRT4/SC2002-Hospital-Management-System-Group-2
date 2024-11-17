package controller;

import java.util.Scanner;
import model.Appointment;
import model.Medication;
import view.AppointmentView;


public class AppointmentController {
	
	public static void setPrescription(Appointment apt, Scanner scanner) {
		String name = AppointmentView.inputPrescription(scanner);
		int dosage = AppointmentView.inputDosage(scanner);
        Medication prescribedMed = new Medication(name,dosage);
        apt.getPrescriptions().add(prescribedMed);
	}
	
	public static void setServiceType(Appointment apt, Scanner scanner) {
		String ser = AppointmentView.inputServiceType(scanner);
		apt.setServiceType(ser);
		System.out.println("Service Type added!");
	}
	
	public static void setNotes(Appointment apt, Scanner scanner) {
		//clear buffer
		scanner.nextLine();
		String note = AppointmentView.inputNotes(scanner);
		apt.setNotes(note);
		System.out.println("Consultation notes added!");
	}
	
}
