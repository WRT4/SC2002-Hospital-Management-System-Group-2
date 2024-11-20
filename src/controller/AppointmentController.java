package controller;

import java.util.ArrayList;
import java.util.Scanner;
import enums.Status;
import model.Appointment;
import model.Medication;
import view.AppointmentView;

/**
 * Controls the operations related to appointments in the medical system.
 * Provides methods to set prescriptions, service types, and consultation notes.
 * Includes functionality to find appointments by ID and handle prescription statuses.
 * @author Hoo Jing Huan, Lim Wee Keat, Tan Wen Rong
 * @version 1.0
 * @since 2024-11-18
 */
public class AppointmentController {

    /**
     * Sets the prescription for an appointment.
     * Prompts the user to enter prescription details and updates the appointment with the prescribed medication.
     *
     * @param apt The appointment to set the prescription for
     * @param scanner The Scanner instance for user input
     */
    public static void setPrescription(Appointment apt, Scanner scanner) {
        apt.setPrescriptionStatus(Status.PENDING);
        String name = AppointmentView.inputPrescription(scanner);
        int dosage = AppointmentView.inputDosage(scanner);
        Medication prescribedMed = new Medication(name, dosage);
        apt.getPrescriptions().add(prescribedMed);
    }

    /**
     * Sets the service type for an appointment.
     * Prompts the user to enter the service type and updates the appointment with the entered service type.
     *
     * @param apt The appointment to set the service type for
     * @param scanner The Scanner instance for user input
     */
    public static void setServiceType(Appointment apt, Scanner scanner) {
        String ser = AppointmentView.inputServiceType(scanner);
        apt.setServiceType(ser);
        System.out.println("Service Type added!");
    }

    /**
     * Sets the consultation notes for an appointment.
     * Prompts the user to enter consultation notes and updates the appointment with the entered notes.
     *
     * @param apt The appointment to set the consultation notes for
     * @param scanner The Scanner instance for user input
     */
    public static void setNotes(Appointment apt, Scanner scanner) {
        // Clear buffer
        scanner.nextLine();
        String note = AppointmentView.inputNotes(scanner);
        apt.setNotes(note);
        System.out.println("Consultation notes added!");
    }

    /**
     * Finds an appointment by ID from a list of appointments, and optionally filters out completed appointments.
     * Throws an exception if the appointment is cancelled or completed based on conditions.
     *
     * @param appointments A list of appointments to search
     * @param noConfirmed  If true, completed appointments are not returned
     * @param id           The ID of the appointment to find
     * @return The appointment found or throws a RuntimeException if not found or invalid
     * @throws RuntimeException If the appointment is not found, cancelled, or completed based on conditions
     */
    public static Appointment findAppointment(ArrayList<Appointment> appointments, boolean noConfirmed, int id) throws RuntimeException {
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
    
}
