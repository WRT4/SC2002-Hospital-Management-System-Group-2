package view;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import controller.AppointmentController;
import model.Appointment;

public class AppointmentView {
	//to confirm with "Schedule" entity
    public static void printScheduledAppointment(Appointment apt){
    	System.out.println("Appointment " + apt.getAppointmentID() + ": " + apt.getDate() + " at " + apt.getTimeSlot().getStartTime() + " with Doctor " + apt.getDoctor().getName());//getName for doctor
        System.out.println("Status: " + apt.getStatus());
        System.out.println();
    }

    //print Appointment Outcome Record (for COMPLETED appointments)
    public static void printAppointmentOutcome(Appointment apt){
        System.out.println("Appointment " + apt.getAppointmentID() + ": " + apt.getDate() + " at " + apt.getTimeSlot().getStartTime() + " with Doctor " + apt.getDoctor().getName());//add outcomes
        System.out.println("Service Type: " + apt.getServiceType());
        System.out.println("Prescription: " + apt.getPrescription() + " Status :" + apt.getPrescriptionStatus());
        System.out.println("Consultation notes: " + apt.getNotes());
        System.out.println();
    }
    
    public static void printCancelledAppointments(Appointment apt) {
    	System.out.println("Appointment " + apt.getAppointmentID() + ": " + apt.getDate() + " at " + apt.getTimeSlot().getStartTime() + " with Doctor " + apt.getDoctor().getName() + " CANCELLED.");
    }
    
    public static Appointment promptForAppointment(ArrayList<Appointment> appointments, boolean noConfirmed, Scanner scanner) {
        Appointment apt = null;
        while (true) {
            try {
                System.out.println("Enter the appointment ID or -1 to exit:");
                int id = getChoice(scanner);  // getChoice can handle Scanner input
                
                if (id == -1) return null;
                
                apt = AppointmentController.findAppointment(appointments, noConfirmed, id);  // Call to the controller’s logic, findAppointment throws RuntimeException
                
                break;  // If no exceptions are thrown, we can exit the loop
            } 
            catch (InputMismatchException e) {
                System.out.println("Invalid input type! Please enter a number.");
                scanner.nextLine();  // Clear invalid input
            } 
            catch (RuntimeException e) {
                System.out.println(e.getMessage());
            } 
            catch (Exception e) {
                System.out.println("An unexpected error occurred. Please try again.");
                scanner.nextLine();  // Clear invalid input
            }
        }
        return apt;
    }

    public static int getChoice(Scanner scanner) {
        System.out.print("Enter choice: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Enter a number.");
            scanner.next();
        }
        return scanner.nextInt();
    }
    
    public static String inputPrescription(Scanner scanner) {
    	//change this
    	System.out.println("Setting prescription...");
		System.out.println("Enter prescription: ");
		String pres = scanner.next();
		return pres;
    }
    
    public static String inputServiceType(Scanner scanner) {
    	System.out.println("Setting Service Type...");
		System.out.println("Enter Service Type: ");
		String ser = scanner.next();
		return ser;
    }
    
    public static String inputNotes(Scanner scanner) {
    	System.out.println("Setting Consultation notes...");
		System.out.println("Enter Consultation notes: ");
		scanner.nextLine();
		String note = scanner.nextLine();
		return note;
    }
}
