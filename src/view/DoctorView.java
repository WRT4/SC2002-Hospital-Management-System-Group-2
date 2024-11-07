package view;
import model.*;
import java.util.Scanner;
import controller.*;

public class DoctorView {
	// @Override
	static Scanner scanner = new Scanner(System.in);
	
    public static void showMenu(Doctor doctor) {
		DoctorController.remindPendingRequests(doctor);
		System.out.println("Message Box: ");
		int counter = 0;
		for (String message: doctor.getMessages()){
			System.out.println((counter+1) + " - " + message);
			System.out.println();
			counter++;
		}
		if (counter ==0)
			System.out.println("No messages yet!");

		int choice;
        do {
        	System.out.println("Doctor Menu: ");
            System.out.println("1. View Patient Medical Records");
            System.out.println("2. Update Patient Medical Records");
            System.out.println("3. View Personal Schedule");
            System.out.println("4. Set Availability for Appointments / Update Personal Schedule");
            System.out.println("5. Accept or Decline Appointment Requests");
            System.out.println("6. View Upcoming Appointments / Cancel Appointment");
            System.out.println("7. Record Appointment Outcome");
            System.out.println("8. Logout");
            System.out.println("Choose an action:");
            choice = DoctorController.getChoice();
            while (choice < 1 || choice > 8) {
            	System.out.println("Invalid option! Try again!");
				choice = DoctorController.getChoice();
            }

            switch (choice) {
                case 1:
                    // Assuming you have a method to get a patient object
                	DoctorController.viewMedicalRecords(doctor);
                    break;
                case 2:
                    // Assuming you have methods to get diagnosis and prescription
                	DoctorController.updateMedicalRecord(doctor);
                    break;
                case 3:
                	DoctorController.viewSchedule(doctor);
                    break;
                case 4:
                	DoctorController.setAvailability(doctor);
                    break;
                case 5:
                    // Implement accept or decline appointment requests
                	DoctorController.viewRequests(doctor);
                    break;
                case 6:
                	DoctorController.viewAppointments(doctor);
                    break;
                case 7:
                    // Implement record appointment outcome
                	DoctorController.recordAppointmentOutcomes(doctor);
                    break;
                case 8:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 8);
    }
}
