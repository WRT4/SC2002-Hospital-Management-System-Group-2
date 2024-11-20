package view;

import model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import enums.Status;

/**
 * Represents the user interface for doctors in the medical system.
 * Handles input and output operations specific to doctor functions.
 * Provides methods to view medical records, schedules, requests, and appointments.
 * @author Hoo Jing Huan, Lim Wee Keat, Tan Wen Rong
 * @version 1.0
 * @since 2024-11-18
 */
public class DoctorView extends UserView {

    /**
     * Constructs a DoctorView object with a given scanner for input.
     *
     * @param scanner The Scanner instance for user input
     */
    public DoctorView(Scanner scanner) {
        super(scanner);
    }

    /**
     * Displays the medical records of a patient.
     *
     * @param patient The patient whose medical records are to be viewed
     */
    public void viewMedicalRecords(Patient patient) {
        if (patient != null) {
            patient.getRecord().printMedicalRecord();
        }
    }

    /**
     * Displays the personal schedule of a doctor for a given date.
     *
     * @param doctor The doctor whose schedule is to be viewed
     */
    public void viewSchedule(Doctor doctor) {
        System.out.println("Viewing Personal Schedule for Doctor " + doctor.getID() + ", Name: " + doctor.getName() + ": ");
        System.out.println("Enter date: ");
        LocalDate date = ScheduleView.inputDate(scanner);
        if (date == null) return;
        while (date.isBefore(doctor.getSchedule().getWorkingSlots().get(0).getDate())) {
            System.out.println("No record found! ");
            date = ScheduleView.inputDate(scanner);
            if (date == null) return;
        }
        ScheduleView.viewAllSlots(date, doctor.getSchedule());
    }

    /**
     * Displays the list of pending appointment requests for the doctor.
     *
     * @param requests The list of appointment requests
     */
    public void viewRequests(ArrayList<AppointmentRequest> requests) {
        int num = 0;
        for (AppointmentRequest request : requests) {
            if (request.getStatus() == Status.PENDING) {
                System.out.println(request);
                num++;
            }
        }
        if (num == 0) {
            System.out.println("No appointment requests yet!");
            return;
        }
    }

    /**
     * Displays the list of confirmed appointments for the doctor.
     *
     * @param appointments The list of confirmed appointments
     * @return 1 if there are confirmed appointments, otherwise 0
     */
    public int viewAppointments(ArrayList<Appointment> appointments) {
        int num = 0;
        for (Appointment appointment : appointments) {
            if (appointment.getStatus() == Status.CONFIRMED) {
                System.out.println(appointment);
                num++;
            }
        }
        if (num == 0) {
            System.out.println("No scheduled appointments! ");
            return 0;
        }
        return 1;
    }

    /**
     * Displays the outcomes of appointments, including uncompleted and completed appointments.
     *
     * @param appointments The list of appointments
     */
    public void viewAppointmentOutcomes(ArrayList<Appointment> appointments) {
        int num1 = 0, num2 = 0;
        if (appointments.size() == 0) {
            System.out.println("No scheduled appointments!");
            return;
        }
        System.out.println("Uncompleted appointments: ");
        for (Appointment appointment : appointments) {
            if (appointment.getStatus() == Status.CONFIRMED) {
                System.out.println(appointment);
                num1++;
            }
        }
        if (num1 == 0) {
            System.out.println("No Uncompleted appointments!");
        }
        System.out.println();
        System.out.println("Completed appointments: ");
        for (Appointment appointment : appointments) {
            if (appointment.getStatus() == Status.COMPLETED) {
                AppointmentView.printAppointmentOutcome(appointment);
                num2++;
            }
        }
        if (num2 == 0) {
            System.out.println("No completed appointments!");
        }
        System.out.println();
    }

    /**
     * Displays the doctor menu options.
     */
    public void showMenu() {
        System.out.println("\nDoctor Menu: ");
        System.out.println("1. View Inbox");
        System.out.println("2. View Patient Medical Records");
        System.out.println("3. Update Patient Medical Records");
        System.out.println("4. View Personal Schedule");
        System.out.println("5. Set Availability for Appointments / Update Personal Schedule");
        System.out.println("6. Accept or Decline Appointment Requests");
        System.out.println("7. View Upcoming Appointments / Cancel Appointment");
        System.out.println("8. Record Appointment Outcome");
        System.out.println("9. Exit to Main Menu");
        System.out.println("Choose an action:");
    }
}
