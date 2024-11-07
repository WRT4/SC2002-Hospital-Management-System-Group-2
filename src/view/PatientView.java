package view;

import model.*;
import controller.*;

public class PatientView {
	public static void showMenu(Patient patient) {
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
			choice = PatientController.getChoice();
			while (choice < 1 || choice > 10) {
				System.out.println("No such option! ");
				choice = PatientController.getChoice();
			}
			switch (choice) {
				case 1:
					PatientController.viewRecord(patient);
					break;
				case 2:
					PatientController.updatePersonalInfo(patient);
					break;
				case 3:
					PatientController.viewAvailableSlots();
					break;
				case 4:
					PatientController.scheduleAppointment(patient);
					break;
				case 5:
					PatientController.rescheduleAppointments(patient);
					break;
				case 6:
					PatientController.cancelAppointment(patient);
					break;
				case 7:
					PatientController.viewRequests(patient);
					break;
				case 8:
					PatientController.viewScheduledAppointments(patient);
					break;
				case 9:
					PatientController.viewAppointmentOutcomes(patient);
					break;
				case 10:
					System.out.println("Logging out...");
			}
		}
		while (choice != 10);
	}
}
