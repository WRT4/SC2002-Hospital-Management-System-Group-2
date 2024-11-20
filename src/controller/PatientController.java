package controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import model.Appointment;
import model.AppointmentRequest;
import application.Database;
import model.Doctor;
import model.Patient;
import model.TimeSlot;
import enums.Status;
import view.AppointmentView;
import view.PatientView;
import view.ScheduleView;

/**
 * Represents the controller for a patient in the medical system.
 * Manages patient-related tasks such as viewing and updating personal information,
 * managing appointments, handling appointment requests, and interacting with the patient view.
 * @author Hoo Jing Huan, Lim Wee Keat, Tan Wen Rong
 * @version 1.0
 * @since 2024-11-18
 */
public class PatientController extends SessionController {
    
    private Patient patient;
    private PatientView patientView;
    
    /**
     * Constructs a PatientController with a given patient and scanner for input.
     *
     * @param patient The patient associated with this controller
     * @param scanner The scanner for user input
     */
    public PatientController(Patient patient, Scanner scanner) {
        this.patient = patient;
        this.scanner = scanner;
        patientView = new PatientView(scanner);
        unreadIndex = patient.getUnreadIndex();
        startTime = LocalTime.now();
        startDate = LocalDate.now();
    }
    
    /**
     * Displays the main menu for the patient and handles user input.
     * Includes options for viewing messages, personal information, available slots, and managing appointments.
     */
    public void showMenu() {
        int choice = -1;
        do {
            patientView.showMessageBox(patient.getMessages(), unreadIndex);
            patientView.showMenu();
            choice = patientView.getChoice();
            scanner.nextLine(); 
            switch (choice) {
                case 1:
                    unreadIndex = patientView.viewInbox(patient.getMessages(), unreadIndex);
                    patient.setUnreadIndex(unreadIndex);
                    break;
                case 2:
                    patientView.viewRecord(patient.getRecord());
                    break;
                case 3:
                    updatePersonalInfo();
                    break;
                case 4:
                    System.out.println("\nViewing available slots: \n");
                    System.out.println("Enter date: ");
                    patientView.viewAvailableSlots(inputDate());
                    break;
                case 5:
                    scheduleAppointment();
                    break;
                case 6:
                    rescheduleAppointments();
                    break;
                case 7:
                    cancelAppointment();
                    break;
                case 8:
                    viewRequests();
                    break;
                case 9:
                    patientView.viewScheduledAppointments(patient.getAppointments());
                    break;
                case 10:
                    PatientView.viewAppointmentOutcomes(patient.getAppointments());
                    break;
                case 11:
                    System.out.println("Exiting Patient menu...");
                    String log = "Patient " + patient.getID() + " accessed system from " + startTime.format(FORMATTER) + " on " + startDate + " to " + LocalTime.now().format(FORMATTER) + " on " + LocalDate.now(); 
                    Database.SYSTEM_LOGS.add(log);
            }
        }
        while (choice != 11);
    }
    
    /**
     * Updates the personal information of the patient.
     * Provides options to enter basic info or update contact info.
     */
    public void updatePersonalInfo() {
        System.out.println("\nUpdating Personal Information...\n");
        System.out.println("1. Enter basic info");
        System.out.println("2. Update contact info");
        System.out.println("-1. Go back");
        int choice3;
        choice3 = patientView.getChoice();
        scanner.nextLine();
        if (choice3 == 1) {
            enterBasicInfo();
        }
        else if (choice3 == 2) {
            updateContactInfo();
        }
    }
    
    /**
     * Prompts the user to input a valid date.
     * Ensures the date is not in the past.
     *
     * @return The input date, or null if invalid
     */
    private LocalDate inputDate() {
        LocalDate finalDate = ScheduleView.inputDate(scanner);
        if (finalDate == null) return null;
        
        // System check: patient cannot book backwards in time
        while (finalDate.isBefore(LocalDate.now())) {
            System.out.println("Date has lapsed! Unable to perform operation!");
            System.out.println("Please re-enter.");
            finalDate = ScheduleView.inputDate(scanner);
            if (finalDate == null) return null;
        }

        return finalDate;
    }
    
    /**
     * Updates the contact information of the patient.
     */
    public void updateContactInfo() {
        System.out.println("\nUpdating Contact Information...\n");
        patient.getRecord().viewContactInfo();
        System.out.println("1. Update Contact info");
        System.out.println("-1. Exit");
        int choice = 0, choice2 = 0;
        choice = patientView.getChoice();
        while (choice != 1 && choice != -1) {
            System.out.println("Option doesn't exist! ");
            choice = patientView.getChoice();
        }
        while (choice == 1) {
            System.out.println("What would you like to update?");
            System.out.println("1. Email");
            System.out.println("2. Phone Number");
            System.out.println("-1. Go back");
            choice2 = patientView.getChoice();
            if (choice2 == 1) {
                System.out.println("Enter new email: ");
                String email = scanner.next();
                patient.updateEmail(email);
            }
            else if (choice2 == 2) {
                System.out.println("Enter new Phone Number: ");
                String phoneNumber = scanner.next();
                patient.updatePhoneNumber(phoneNumber);
            }
            else break;
        }
        if (choice == -1) {
            return;
        }
    }
    
    /**
     * Sends a cancellation message for a cancelled appointment.
     *
     * @param tempApp The cancelled appointment
     */
    public static void sendCancellationMessage(Appointment tempApp){
        String messageToDoctor;
        messageToDoctor = "Message at " + LocalDate.now() + ": Appointment cancelled by " + tempApp.getPatient().getName() + "\n" +
                "Appointment Details - " + tempApp.getTimeSlot().toString();
        tempApp.getDoctor().getMessages().add(messageToDoctor);
    }
    
    /**
     * Sends a request message for an appointment request.
     *
     * @param request The appointment request
     * @param rescheduled True if the request is a reschedule, false otherwise
     */
    public static void sendRequestMessage(AppointmentRequest request, boolean rescheduled) {
        String messageToDoctor;
        if (!rescheduled) {
            messageToDoctor = "Message at " + LocalDate.now() + ": Appointment Request sent by " + request.getPatient().getName() + "\n" +
                    "Appointment Details - " + request.getTimeSlot().toString();
        } else {
            messageToDoctor = "Message at " + LocalDate.now() + ": Rescheduled appointment Request sent by " + request.getPatient().getName() + "\n" +
                    "Appointment Details - " + request.getTimeSlot().toString();
        }
        request.getDoctor().getMessages().add(messageToDoctor);
    }
    
    /**
     * Prompts the patient to enter basic information.
     */
    public void enterBasicInfo() {
        System.out.println("\nEntering Basic Information...\n");
        System.out.println("Enter your full name: ");
        String name = scanner.nextLine();
        patient.setName(name);

        System.out.println("Enter your gender (Female / Male): ");
        String gender = scanner.next();
        patient.setGender(gender);

        System.out.println("Enter your date of birth (in DD-MM-YYYY): ");
        String dob = scanner.next();
        patient.setDateOfBirth(dob);
    }
    
    /**
     * Schedules an appointment for the patient.
     * Prompts the patient to select a doctor and a timeslot.
     */
    public void scheduleAppointment() {
        System.out.println("\nScheduling Appointment...\n");
        patientView.viewDoctors();
        String id = patientView.enterID();
        if (id.equals("-1")) return;
        Doctor doctor = null;
        for (Doctor doctor1 : Database.DOCTORS) {
            if (doctor1.getID().equals(id)) doctor = doctor1;
        }
        if (doctor == null) {
            System.out.println("Doctor not found!");
            return;
        }

        ArrayList<Object> tempArr = getDateAndTime();
        if (tempArr == null) return;
        LocalDate date = (LocalDate) tempArr.get(0);
        LocalTime time = (LocalTime) tempArr.get(1);
        
        // Input date time -- end
        TimeSlot requestTime = doctor.findTimeSlot(date, time);
        if (requestTime == null) return;

        if (doctor.getSchedule().checkOverlapping(requestTime)) {
            System.out.println("Doctor " + doctor.getName() + " is not available at " + requestTime.toString());
            System.out.println("Please try again.");
        } else {
            AppointmentRequest temp = new AppointmentRequest(date, requestTime, doctor, patient);
            patient.getRequests().add(temp);
            doctor.addRequest(temp);
            sendRequestMessage(temp, false);
            System.out.println("Appointment Request Sent!");
        }
    }

    /**
     * Reschedules an appointment for the patient.
     * Prompts the patient to select a new date and time for the appointment.
     */
    public void rescheduleAppointments() {
        System.out.println("\nRescheduling Appointment...");
        ArrayList<Appointment> appointments = patient.getAppointments();
        patientView.viewScheduledAppointments(appointments);
        System.out.println("Which appointment ID would you like to reschedule? Enter Appointment ID or -1 to exit.");
        Appointment temp = AppointmentView.promptForAppointment(appointments, true, scanner);
        if (temp == null) return;
        
        // Reschedule and free slot
        System.out.println("Changing appointment " + temp.getAppointmentID() + " ...");
        System.out.println("Enter new date:");

        ArrayList<Object> tempArr = getDateAndTime();
        if (tempArr == null) return;
        LocalDate date = (LocalDate) tempArr.get(0);
        LocalTime time = (LocalTime) tempArr.get(1);

        TimeSlot requestTime = temp.getDoctor().findTimeSlot(date, time);
        if (requestTime == null) return;

        if (temp.getDoctor().getSchedule().checkOverlapping(requestTime)) {
            System.out.println("Doctor " + temp.getDoctor().getName() + " is not available at " + requestTime.toString());
            System.out.println("Please try again.");
            return;
        }
        temp.setStatus(Status.CANCELLED);
        temp.getDoctor().subtractAppointmentCounter(patient);
        // New appointment - in the form of a request
        AppointmentRequest request = new AppointmentRequest(date, requestTime, temp.getDoctor(), temp.getPatient());
        temp.getDoctor().addRequest(request);
        patient.getRequests().add(request);
        // update doctor schedule and free slot
        temp.getDoctor().getSchedule().setAvailability(temp.getTimeSlot());
        // send messages to doctor
        sendCancellationMessage(temp);
        sendRequestMessage(request, true);
        System.out.println("Successfully Rescheduled! New request sent!");
    }

    /**
     * Helper method to get date and time for scheduling or rescheduling an appointment.
     * Ensures the date and time are valid and not overlapping with existing appointments.
     *
     * @return An ArrayList containing the date and time
     */
    private ArrayList<Object> getDateAndTime() {
        ArrayList<Object> arr = new ArrayList<>();
        // Input date time -- start
        LocalDate date = inputDate();
        if (date == null) return null;

        LocalTime time = ScheduleView.inputTime(scanner);
        if (time == null) return null;
        
        // Check if time is in the past
        while (date.equals(LocalDate.now()) && time.isBefore(LocalTime.now())) {
            System.out.println("Time has lapsed! Please re-enter!");
            time = ScheduleView.inputTime(scanner);
            if (time == null) return null;
        }
        
        // Check if there is already an appointment at the requested time
        while (patient.checkOverlapping(date, time)) {
            System.out.println("You already have a scheduled appointment at this time! Please re-enter!");
            time = ScheduleView.inputTime(scanner);
            if (time == null) return null;
        }
        arr.add(date);
        arr.add(time);
        return arr;
    }

    /**
     * Cancels an appointment for the patient.
     * Prompts the patient to select an appointment to cancel and updates the status.
     */
    public void cancelAppointment() {
        System.out.println("\nCancelling Appointment...");
        ArrayList<Appointment> appointments = patient.getAppointments();
        int exit = patientView.viewScheduledAppointments(appointments);
        if (exit == -1) return;
        System.out.println("Which appointment would you like to cancel? Enter Appointment ID or -1 to exit.");
        Appointment temp = AppointmentView.promptForAppointment(appointments, true, scanner);
        if (temp == null) return;
        System.out.println("Confirm cancellation for " + temp + "? Enter 1 to confirm and -1 to exit.");
        int choice = patientView.getChoice();
        while (choice != 1 && choice != -1) {
        	System.out.println("Option doesn't exist! ");
        	choice = patientView.getChoice();
        }
        if (choice == -1) return;
        else {
	        temp.getDoctor().getSchedule().setAvailability(temp.getTimeSlot());
	        temp.setStatus(Status.CANCELLED);
	        temp.getDoctor().subtractAppointmentCounter(patient);
	        sendCancellationMessage(temp);
	        System.out.println("Successfully cancelled!");
        }
    }

    /**
     * Views the appointment requests made by the patient and allows cancellation of requests.
     */
    public void viewRequests() {
        ArrayList<AppointmentRequest> requests = patient.getRequests();
        patientView.viewRequests(requests);
        System.out.println("Would you like to cancel a request? 1. Yes -1. Exit ");
        int choice = patientView.getChoice();
        while (choice != 1 && choice != -1) {
            System.out.println("Option doesn't exist! ");
            choice = patientView.getChoice();
        }
        if (choice == -1) return;
        if (choice == 1) {
            System.out.println("Which requestID would you like to cancel? Enter ID or -1 to exit.");
            AppointmentRequest req = findRequest(requests);
            if (req == null) return;
            req.setStatus(Status.CANCELLED);
            System.out.println("Request Cancelled!");
        }
    }

    /**
     * Finds an appointment request by its ID from a list of requests.
     *
     * @param requests The list of appointment requests
     * @return The found appointment request, or null if not found or invalid
     */
    private AppointmentRequest findRequest(ArrayList<AppointmentRequest> requests) {
        AppointmentRequest request = null;
        while (true) {
            try {
                request = null;
                int requestID;
                System.out.println("Enter requestID or -1 to exit: ");
                requestID = patientView.getChoice();
                if (requestID == -1) return null;
                int i = 0;
                for (i = 0; i < requests.size(); i++) {
                    if (requests.get(i).getRequestID() == requestID) {
                        request = requests.get(i);
                        break;
                    }
                }
                if (request == null) throw new RuntimeException("RequestID does not exist!");
                if (request.getStatus() != Status.PENDING) {
                    if (request.getStatus() == Status.CANCELLED) {
                        throw new RuntimeException("Request already cancelled!");
                    } else if (request.getStatus() == Status.ACCEPTED) {
                        throw new RuntimeException("Request already accepted! Cancel appointment instead!");
                    } else if (request.getStatus() == Status.DECLINED) {
                        throw new RuntimeException("Request already declined!");
                    }
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Wrong input type! Try Again!");
                scanner.nextLine();
                continue;
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                scanner.nextLine();
                continue;
            } catch (Exception e) {
                System.out.println("Error! Try Again!");
                scanner.nextLine();
                continue;
            }
        }
        return request;
    }
}
    