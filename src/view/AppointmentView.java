package view;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import controller.AppointmentController;
import model.Appointment;

/**
 * Provides methods for displaying appointment-related information.
 * Includes methods to view scheduled appointments, appointment outcomes, and handle user input for appointments.
 * @author Hoo Jing Huan, Lim Wee Keat, Tan Wen Rong
 * @version 1.0
 * @since 2024-11-18
 */
public class AppointmentView {

    /**
     * Prints the details of a scheduled appointment.
     *
     * @param apt The appointment to be printed
     */
    public static void printScheduledAppointment(Appointment apt) {
        System.out.println("Appointment " + apt.getAppointmentID() + ": " + apt.getDate() + " at " + apt.getTimeSlot().getStartTime() + " with Doctor " + apt.getDoctor().getName());
        System.out.println("Status: " + apt.getStatus());
        System.out.println();
    }

    /**
     * Prints the details and outcome of a completed appointment.
     *
     * @param apt The appointment to be printed
     */
    public static void printAppointmentOutcome(Appointment apt) {
        System.out.println(apt.getPatient());
        System.out.println("Appointment " + apt.getAppointmentID() + ": " + apt.getDate() + " at " + apt.getTimeSlot().getStartTime() + " with Doctor " + apt.getDoctor().getName());
        System.out.println("Service Type: " + apt.getServiceType());
        System.out.println("Prescription: " + apt.getPrescriptions() + " Status: " + apt.getPrescriptionStatus());
        System.out.println("Consultation notes: " + apt.getNotes());
        System.out.println();
    }

    /**
     * Prints the details of a cancelled appointment.
     *
     * @param apt The appointment to be printed
     */
    public static void printCancelledAppointments(Appointment apt) {
        System.out.println("Appointment " + apt.getAppointmentID() + ": " + apt.getDate() + " at " + apt.getTimeSlot().getStartTime() + " with Doctor " + apt.getDoctor().getName() + " CANCELLED.");
    }

    /**
     * Prompts the user to select an appointment by entering an appointment ID.
     * Handles input and validation of the appointment ID.
     *
     * @param appointments The list of appointments to choose from
     * @param noConfirmed  Whether to include confirmed appointments in the selection
     * @param scanner      The Scanner instance for user input
     * @return The selected appointment, or null if the user exits
     */
    public static Appointment promptForAppointment(ArrayList<Appointment> appointments, boolean noConfirmed, Scanner scanner) {
        Appointment apt = null;
        while (true) {
            try {
                System.out.println("Enter the appointment ID or -1 to exit:");
                int id = getChoice(scanner);

                if (id == -1) return null;

                apt = AppointmentController.findAppointment(appointments, noConfirmed, id);

                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input type! Please enter a number.");
                scanner.nextLine();
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred. Please try again.");
                scanner.nextLine();
            }
        }
        return apt;
    }

    /**
     * Prompts the user to enter a choice.
     * Handles validation of the input to ensure it is a number.
     *
     * @param scanner The Scanner instance for user input
     * @return The user's choice as an integer
     */
    public static int getChoice(Scanner scanner) {
        System.out.print("Enter choice: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Enter a number.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    /**
     * Prompts the user to enter a prescription.
     *
     * @param scanner The Scanner instance for user input
     * @return The name of the prescribed medicine
     */
    public static String inputPrescription(Scanner scanner) {
        System.out.println("What medicine do you want to prescribe?");
        String name = scanner.next();
        return name;
    }

    /**
     * Prompts the user to enter the dosage of the prescription.
     *
     * @param scanner The Scanner instance for user input
     * @return The dosage of the prescribed medicine
     */
    public static int inputDosage(Scanner scanner) {
        System.out.println("What is the dosage?");
        int dosage = scanner.nextInt();
        return dosage;
    }

    /**
     * Prompts the user to enter the service type for an appointment.
     *
     * @param scanner The Scanner instance for user input
     * @return The service type entered by the user
     */
    public static String inputServiceType(Scanner scanner) {
        System.out.println("Setting Service Type...");
        System.out.println("Enter Service Type: ");
        String ser = scanner.next();
        return ser;
    }

    /**
     * Prompts the user to enter consultation notes for an appointment.
     *
     * @param scanner The Scanner instance for user input
     * @return The consultation notes entered by the user
     */
    public static String inputNotes(Scanner scanner) {
        System.out.println("Setting Consultation notes...");
        System.out.println("Enter Consultation notes: ");
        String note = scanner.nextLine();
        return note;
    }
}
