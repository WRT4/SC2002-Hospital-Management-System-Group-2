package view;

import model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import enums.Status;
import application.Database;

/**
 * Represents the user interface for patients in the medical system.
 * Handles input and output operations specific to patient functions.
 * Provides methods to view medical records, requests, appointments, and available slots.
 * @author Hoo Jing Huan, Tan Wen Rong, Yeoh Kai Wen
 * @version 1.0
 * @since 2024-11-18
 */
public class PatientView extends UserView {

    /**
     * Constructs a PatientView object with a given scanner for input.
     *
     * @param scanner The Scanner instance for user input
     */
    public PatientView(Scanner scanner) {
        super(scanner);
    }

    /**
     * Displays the medical record of the patient.
     *
     * @param record The medical record to view
     */
    public void viewRecord(MedicalRecord record) {
        System.out.println("\nViewing Record...\n");
        record.printMedicalRecord();
    }

    /**
     * Displays the list of appointment requests made by the patient.
     *
     * @param requests The list of appointment requests
     */
    public void viewRequests(ArrayList<AppointmentRequest> requests) {
        System.out.println("\nViewing Requests...\n");
        if (requests.size() == 0) {
            System.out.println("No requests made!");
            return;
        }
        for (AppointmentRequest request : requests) {
            System.out.println(request);
        }
    }

    /**
     * Displays the list of scheduled appointments for the patient.
     *
     * @param appointments The list of scheduled appointments
     */
    public void viewScheduledAppointments(ArrayList<Appointment> appointments) {
        System.out.println("\nViewing scheduled Appointment...\n");
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
        System.out.println("Note: Appointment request that hasn't been accepted by doctor can be found in Menu Option 8");
    }

    /**
     * Displays the list of doctors.
     */
    public void viewDoctors() {
        for (Doctor doctor : Database.DOCTORS) {
            System.out.println(doctor);
        }
        System.out.println("Enter Doctor ID or -1 to go back: ");
    }

    /**
     * Displays the outcomes of past appointments.
     *
     * @param appointments The list of past appointments
     */
    public static void viewAppointmentOutcomes(ArrayList<Appointment> appointments) {
        System.out.println("\nViewing Appointment Outcomes...\n");
        int num = 0;
        for (Appointment apt : appointments) {
            if (apt.getStatus() == Status.COMPLETED) {
                AppointmentView.printAppointmentOutcome(apt);
                num++;
            } else if (apt.getStatus() == Status.CANCELLED) {
                AppointmentView.printCancelledAppointments(apt);
                num++;
            }
        }
        if (num == 0) {
            System.out.println("No completed or cancelled appointments!");
        }
    }

    /**
     * Displays the available appointment slots for a given date.
     *
     * @param date The date to check for available slots
     */
    public void viewAvailableSlots(LocalDate date) {
        if (date == null) return;

        String docInputID;
        System.out.println("List of doctors: ");
        for (Doctor doctor : Database.DOCTORS) {
            System.out.println(doctor.getID() + "- " + doctor.getName());
        }
        System.out.println("Enter the ID of doctor to check available slots:");
        docInputID = scanner.next();
        for (Doctor doctor : Database.DOCTORS) {
            if (docInputID.compareTo(doctor.getID()) == 0) {
                ScheduleView.viewAvailableSlots(date, doctor.getSchedule());
                break;
            }
        }
    }

    /**
     * Displays the patient menu options.
     */
    public void showMenu() {
        System.out.println("\nPatient Menu: ");
        System.out.println("1. View Inbox");
        System.out.println("2. View Medical Record");
        System.out.println("3. Enter and Update Personal Information");
        System.out.println("4. View Available Appointment Slots");
        System.out.println("5. Schedule an Appointment");
        System.out.println("6. Reschedule an Appointment");
        System.out.println("7. Cancel a Scheduled Appointment");
        System.out.println("8. View/Cancel Appointment Requests");
        System.out.println("9. View Scheduled Appointments");
        System.out.println("10. View Past Appointment Outcome Records");
        System.out.println("11. Exit to Main Menu");
        System.out.println("Choose an action:");
    }
}
