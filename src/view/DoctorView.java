package view;

import controller.DoctorController;
import model.Doctor;
import model.Appointment;
import java.util.Scanner;

public class DoctorView {
    private DoctorController controller;
    private Scanner scanner;

    public DoctorView(Doctor doctor) {
        this.controller = new DoctorController(doctor);
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        int choice;
        do {
            System.out.println("\nDoctor Menu:");
            System.out.println("1. View Patient Medical Records");
            System.out.println("2. Update Patient Medical Records");
            System.out.println("3. View Schedule");
            System.out.println("4. Set Availability");
            System.out.println("5. Accept or Decline Appointment Requests");
            System.out.println("6. View Appointments");
            System.out.println("7. Record Appointment Outcome");
            System.out.println("8. Logout");

            choice = getChoice();

            switch (choice) {
                case 1:
                    // Assume getPatient() provides a patient instance
                    System.out.println("Patient Medical Records");
                    break;
                case 2:
                    // Updating patient records
                    System.out.println("Update Patient Medical Records");
                    break;
                case 3:
                    controller.viewSchedule();
                    break;
                case 4:
                    controller.setAvailability();
                    break;
                case 5:
                    controller.viewRequests();
                    break;
                case 6:
                    System.out.println("Appointments: " + controller.doctor.viewAppointments());
                    break;
                case 7:
                    // Assume getAppointment() provides an appointment instance
                    System.out.println("Record Outcome for Appointment");
                    break;
                case 8:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 8);
    }

    private int getChoice() {
        System.out.print("Enter choice: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Enter a number.");
            scanner.next();
        }
        return scanner.nextInt();
    }
}
