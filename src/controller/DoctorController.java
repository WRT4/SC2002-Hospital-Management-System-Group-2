package controller;

import model.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class DoctorController {
    private Doctor doctor;
    private Scanner scanner;

    public DoctorController(Doctor doctor) {
        this.doctor = doctor;
        this.scanner = new Scanner(System.in);
    }

    public void setAvailability() {
        System.out.println("Would you like to \n1. Set unavailable timeslot \n2. Free unavailable timeslot \n-1. Exit");
        int choice = getChoice();
        if (choice == 1) {
            LocalDate date = Schedule.inputDate();
            LocalTime time = Schedule.inputTime();
            TimeSlot timeslot = doctor.findTimeSlot(date, time);
            if (timeslot != null && !timeslot.getOccupied()) {
                timeslot.setOccupied();
                System.out.println("Date: " + date + " Time: " + time + " set as unavailable.");
            } else {
                System.out.println("Timeslot not found or already busy.");
            }
        } else if (choice == 2) {
            LocalDate date = Schedule.inputDate();
            LocalTime time = Schedule.inputTime();
            TimeSlot timeslot = doctor.findTimeSlot(date, time);
            if (timeslot != null) {
                timeslot.free();
                System.out.println("Date: " + date + " Time: " + time + " freed.");
            } else {
                System.out.println("No such timeslot.");
            }
        }
    }

    public void viewSchedule() {
        System.out.println("Enter date to view schedule (YYYY-MM-DD): ");
        LocalDate date = Schedule.inputDate();
        doctor.getSchedule().viewAllSlots(date);
    }

    public void viewRequests() {
        ArrayList<AppointmentRequest> pendingRequests = new ArrayList<>();
        for (AppointmentRequest request : doctor.getRequests()) {
            if (request.getStatus() == Status.PENDING) {
                pendingRequests.add(request);
            }
        }
        System.out.println("Pending Appointment Requests: " + pendingRequests);
    }

    public void recordAppointmentOutcome(Appointment appointment) {
        System.out.println("Recording outcome for appointment ID: " + appointment.getAppointmentID());
        System.out.print("Enter Service Type: ");
        String serviceType = scanner.nextLine();
        appointment.setServiceType(serviceType);

        System.out.print("Enter Prescription: ");
        String prescription = scanner.nextLine();
        appointment.setPrescription(prescription);
        appointment.setPrescriptionStatus(Status.PENDING);

        System.out.print("Enter Consultation Notes: ");
        String notes = scanner.nextLine();
        appointment.setNotes(notes);

        appointment.setStatus(Status.COMPLETED);
        System.out.println("Appointment outcome recorded.");
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
