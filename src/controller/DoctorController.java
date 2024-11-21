package controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import application.Database;
import model.*;
import view.AppointmentView;
import view.DoctorView;
import view.ScheduleView;
import enums.Status;

/**
 * Represents the controller for a doctor in the medical system.
 * Manages doctor-related tasks such as viewing and updating medical records,
 * managing schedules, handling appointment requests, and interacting with the doctor view.
 * @author Hoo Jing Huan, Lim Wee Keat, Tan Wen Rong
 * @version 1.0
 * @since 2024-11-18
 */
public class DoctorController extends SessionController {
    
    private Doctor doctor;
    private DoctorView doctorView;
    
    /**
     * Constructs a DoctorController with a given doctor and scanner for input.
     *
     * @param d The doctor associated with this controller
     * @param s The scanner for user input
     */
    public DoctorController(Doctor d, Scanner s) {
        doctor = d;
        scanner = s;
        doctorView = new DoctorView(s);
        unreadIndex = d.getUnreadIndex();
        startTime = LocalTime.now();
        startDate = LocalDate.now();
    }
    
    /**
     * Displays the main menu for the doctor and handles user input.
     * Includes options for viewing messages, medical records, schedule, and managing appointments.
     */
    public void showMenu() {
        refreshWorkingSchedule();
        remindPendingRequests();
        int choice;
        do {
            doctorView.showMessageBox(doctor.getMessages(), unreadIndex);
            doctorView.showMenu();
            choice = doctorView.getChoice();
            scanner.nextLine(); 
            switch (choice) {
                case 1:
                    unreadIndex = doctorView.viewInbox(doctor.getMessages(), unreadIndex);
                    doctor.setUnreadIndex(unreadIndex);
                    break;
                case 2:
                    viewMedicalRecord();
                    break;
                case 3:
                    // Assuming you have methods to get diagnosis and prescription
                    updateMedicalRecord();
                    break;
                case 4:
                    viewSchedule();
                    break;
                case 5:
                    setAvailability();
                    break;
                case 6:
                    // Implement accept or decline appointment requests
                    viewRequests();
                    break;
                case 7:
                    viewAppointments();
                    break;
                case 8:
                    // Implement record appointment outcome
                    recordAppointmentOutcomes();
                    break;
                case 9:
                    System.out.println("Exiting Doctor menu...");
                    String log = "Doctor " + doctor.getID() + " accessed system from " + startTime.format(FORMATTER) + " on " + startDate + " to " + LocalTime.now().format(FORMATTER) + " on " + LocalDate.now(); 
                    Database.SYSTEM_LOGS.add(log);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 9);
    }
    
    /**
     * Sets the unread index for the doctor's messages.
     *
     * @param i The new unread index
     */
    public void setUnreadIndex(int i) {
        this.unreadIndex = i;
    }
    
    /**
     * Displays the personal schedule of a doctor for a given date.
     *
     * @param doctor The doctor whose schedule is to be viewed
     */
    public void viewSchedule() {
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
     * Refreshes the doctor's working schedule.
     *
     */
    public void refreshWorkingSchedule(){
        doctor.getSchedule().fillAdvancedSchedule();
    }
    
    /**
     * Checks for any pending appointment requests that need immediate attention.
     *
     * @return A list of pending appointment requests
     */
    public ArrayList<AppointmentRequest> checkPendingRequests(){
        ArrayList<AppointmentRequest> pendingRequests = new ArrayList<>();
        for (AppointmentRequest request: doctor.getRequests()){
            if (request.getStatus() == Status.PENDING && ChronoUnit.DAYS.between(LocalDate.now(), request.getDate()) < 3){
                pendingRequests.add(request);
            }
        }
        return pendingRequests;
    }
    
    /**
     * Sends a reminder message for an urgent pending appointment request.
     *
     * @param urgentRequest The urgent appointment request
     */
    public void pushPendingRequestMessage(AppointmentRequest urgentRequest){
        String pendingRequest = "Urgent: Please be reminded that you have a pending appointment request: \n" + urgentRequest.toString();
        doctor.getMessages().add(pendingRequest);
    }
    
    /**
     * Sends reminder messages for all pending appointment requests.
     */
    public void remindPendingRequests(){
        for (AppointmentRequest request: checkPendingRequests()){
            pushPendingRequestMessage(request);
        }
    }
    
    /**
     * Sends a message to the patient regarding the acceptance or rejection of an appointment request.
     *
     * @param request  The appointment request
     * @param accepted True if the request is accepted, false otherwise
     */
    public void sendAcceptanceMessage(AppointmentRequest request, boolean accepted) {
        String message;
        Doctor doctor = request.getDoctor();
        TimeSlot timeslot = request.getTimeSlot();
        if (accepted) {
            message = "Message at " + LocalDate.now() + ": Appointment request accepted by " + doctor.getName() + "\n" +
                    "Appointment Details - " + timeslot.toString();
        }
        else {
            message = "Message at " + LocalDate.now() + ": Appointment request rejected by " + doctor.getName() + "\n" +
                    "Rejected request Details - " + timeslot.toString();
        }
        request.getPatient().getMessages().add(message);
    }
    
    /**
     * Sends a cancellation message to the patient for a cancelled appointment.
     *
     * @param tempApp The cancelled appointment
     */
    public void sendCancellationMessage(Appointment tempApp) {
        String messageToPatient;
        TimeSlot timeslot = tempApp.getTimeSlot();
        messageToPatient = "Message at " + LocalDate.now() + ": Appointment cancelled by " + doctor.getName() + "\n" +
                "Appointment Details - " + timeslot.toString();
        tempApp.getPatient().getMessages().add(messageToPatient);
    }
    
    /**
     * Prompts the doctor to enter a diagnosis for a patient.
     *
     * @return The entered diagnosis
     */
    private String getDiagnosis() {
        System.out.println("Enter diagnosis:");
        return scanner.nextLine();
    }

    /**
     * Prompts the doctor to enter a prescription for a patient.
     *
     * @return The entered prescription
     */
    private String getPrescription() {
        System.out.println("Enter prescription:");
        return scanner.nextLine();
    }
    
    /**
     * Retrieves a patient under the care of the doctor by patient ID.
     *
     * @return The patient object, or null if not found or no patients under care
     */
    public Patient getPatient() {
        ArrayList<Patient> patientsUnderCare = new ArrayList<>(doctor.getAppointmentCounter().keySet());
        if (patientsUnderCare == null || patientsUnderCare.size() == 0){
            System.out.println("No patients under care.");
            return null;
        }
        // Implement method to get a patient object
        for (Patient patient : patientsUnderCare) {
            System.out.println(patient);
        }
        System.out.println("Enter Patient ID or -1 to exit: ");
        String choice = doctorView.enterID().trim();
        if (choice.equals("-1")) return null;
        for (Patient patient : patientsUnderCare) {
            if (choice.equals(patient.getID())) {
                return patient;
            }
        }
        System.out.println("Patient not found.");
        return null;
    }
    
    /**
     * Views the medical record of a selected patient.
     */
    public void viewMedicalRecord(){
        Patient patient = getPatient();
        if (patient == null){
            return;
        }
        if (doctor.getAppointmentCounter().containsKey(patient) && doctor.getAppointmentCounter().get(patient) > 0) {
            doctorView.viewMedicalRecords(patient);
        } else {
            System.out.println("Cannot view medical record of " + patient.getName() + " as no appointments have been scheduled.");
        }
    }
    
    /**
     * Updates the medical record of a selected patient with diagnosis and prescription.
     */
    public void updateMedicalRecord() {
        Patient patient = getPatient();
        if (patient == null){
            return;
        }

        if (scanner.hasNextLine()) {
            scanner.nextLine(); // This ensures any leftover newline is cleared
        }

        System.out.println("Enter -1 to exit.");
        String diagnosis = getDiagnosis();
        if (diagnosis.equals("-1")) return;

        String prescription = getPrescription();
        if (prescription.equals("-1")) return;

        MedicalRecord record = patient.getRecord();
        record.addDiagnosis(diagnosis, doctor);
        record.addPrescription(prescription, doctor);
        System.out.println("Medical record updated for patient: " + patient.getName());
    }
    
    /**
     * Sets or frees a timeslot as unavailable for the doctor. Allows doctor to cancel appointment if he attempts to free a slot with one.
     */
    public void setAvailability() {
        System.out.println("Would you like to \n1. Set unavailable timeslot \n2. Free unavailable timeslot \n-1. Exit");
        int choice = doctorView.getChoice();
        while (choice != 1 && choice != 2 && choice != -1) {
            System.out.println("No such option! Try again!");
            choice = doctorView.getChoice();
        }
        if (choice == 1) {
            LocalDate date = ScheduleView.inputDate(scanner);
            if (date == null) return;
            LocalTime time = ScheduleView.inputTime(scanner);
            if (time == null) return;
            TimeSlot timeslot = doctor.getSchedule().findTimeSlot(date, time);
            if (timeslot == null) {
                System.out.println("No such timeslot!");
                return;
            }
            if (timeslot.getOccupied() == true) {
                System.out.println("Timeslot already busy!");
                return;
            }
            timeslot.setOccupied();
            System.out.println("Date: " + date + " Time: " + time + " successfully set as unavailable!");
            return;
        } 
        else if (choice == 2) {
            LocalDate date = ScheduleView.inputDate(scanner);
            if (date == null) return;
            LocalTime time = ScheduleView.inputTime(scanner);
            if (time == null) return;
            Schedule schedule = doctor.getSchedule();
            Appointment temp = schedule.findAppointment(schedule.findTimeSlot(date, time));
            TimeSlot timeslot = schedule.findTimeSlot(date, time);
            if (timeslot == null) {
                System.out.println("No such timeslot!");
                return;
            }
            if (temp == null) {
                timeslot.free();
                System.out.println("Date: " + date + " Time: " + time + " successfully freed!");
                return;
            } 
            // allow doctors to cancel appointments directly
            else {
	            System.out.println("Cancel " + temp + "?" + "\n1. Yes \n2. No");
	            int choice2 = doctorView.getChoice();
	            while (choice2 != 1 && choice2 != 2) {
	                System.out.println("No such option! Try again!");
	                choice2 = doctorView.getChoice();
	            }
                if (choice2 == 1) {
                    temp.getTimeSlot().free();
                    doctor.subtractAppointmentCounter(temp.getPatient());
                    temp.setStatus(Status.CANCELLED);
                    sendCancellationMessage(temp);
                    System.out.println("Successfully cancelled!");
                    return;
                } 
                else
                    return;
            }
            // ask for confirmation to cancel appointment
        } 
        else if (choice == -1) {
            return;
        }
    }

    /**
     * Views the pending appointment requests and allows the doctor to accept or reject them.
     */
    public void viewRequests() {
        ArrayList<AppointmentRequest> requests = doctor.getRequests();
        doctorView.viewRequests(requests);
        System.out.println("Which requestID would you like to accept/reject?");
        AppointmentRequest request = findRequest(requests);
        if (request == null) return;
        System.out.println("Please input 1 for Accept or 2 for Reject. Enter -1 to exit.");
        int choice = doctorView.getChoice();
        while (choice != 1 && choice != 2 && choice != -1) {
            System.out.println("Invalid option! Try again!");
            choice = doctorView.getChoice();
        }
        if (choice == 1) {
            // check overlap of requests with a busy timeslot or patient already has a scheduled appointment at that time
            if (doctor.getSchedule().checkOverlapping(request.getTimeSlot()) || request.getPatient().checkOverlapping(request.getDate(), request.getTimeSlot().getStartTime())) {
                System.out.println("Timeslot is not available! Would you like to reject request instead? 1. Yes -1. Exit");
                int choice2 = doctorView.getChoice();
                while (choice2 != 1 && choice2 != -1) {
                    choice2 = doctorView.getChoice();
                }
                if (choice2 == -1) return;
                else {
                    request.declineRequest();
                    sendAcceptanceMessage(request, false);
                    return;
                }
            }
            request.acceptRequest();
            sendAcceptanceMessage(request, true);
            doctor.addAppointmentCounter(request.getPatient());
        } else if (choice == 2) {
            request.declineRequest();
            sendAcceptanceMessage(request, false);
        } else
            return;
    }

    /**
     * Views the appointments and allows the doctor to cancel them.
     */
    public void viewAppointments() {
        int notEmpty = doctorView.viewAppointments(doctor.getSchedule().getAppointments());
        if (notEmpty == 0) return;
        System.out.println("Would you like to cancel an appointment? 1. Yes -1. Exit");
        int choice = doctorView.getChoice();
        while (choice != 1 && choice != -1) {
            System.out.println("Option doesn't exist!");
            choice = doctorView.getChoice();
        }
        if (choice == -1) return;
        if (choice == 1) {
            int success = cancelAppointment();
            if (success == 0) return;
        }
    }

    /**
     * Cancels an appointment and updates the patient's appointment counter.
     *
     * @return 1 if the appointment was successfully cancelled, 0 otherwise
     */
    public int cancelAppointment() {
        System.out.println("Which AppointmentID would you like to cancel? Enter ID or -1 to exit.");
        Appointment apt = AppointmentView.promptForAppointment(doctor.getSchedule().getAppointments(), true, scanner);
        if (apt == null) return 0;
        System.out.println("Confirm cancellation for " + apt + "? Enter 1 to confirm and -1 to exit.");
        int choice = doctorView.getChoice();
        while (choice != 1 && choice != -1) {
        	System.out.println("Option doesn't exist! ");
        	choice = doctorView.getChoice();
        }
        if (choice == -1) return 0;
        else {
	        apt.setStatus(Status.CANCELLED);
	        apt.getTimeSlot().free();
	        doctor.subtractAppointmentCounter(apt.getPatient());
	        sendCancellationMessage(apt);
	        System.out.println("Successfully cancelled!");
	        return 1;
        }
    }

    /**
     * Records the outcomes of appointments, including service type, prescription, and consultation notes.
     */
    public void recordAppointmentOutcomes() {
        ArrayList<Appointment> appointments = doctor.getSchedule().getAppointments();
        doctorView.viewAppointmentOutcomes(appointments);
        System.out.println();
        System.out.println("Which appointment ID would you like to record outcome for? Enter ID or -1 to exit:");
        Appointment apt = AppointmentView.promptForAppointment(appointments, false, scanner);
        if (apt == null) return;
        if (apt.getStatus() == Status.CONFIRMED) {
            if (apt.getTimeSlot().getDate().isBefore(LocalDate.now().plusDays(1))) {
                if (apt.getTimeSlot().getDate().isBefore(LocalDate.now())) {
                    apt.setStatus(Status.COMPLETED);
                } else if (apt.getTimeSlot().getEndTime().isBefore(LocalTime.now())) {
                    apt.setStatus(Status.COMPLETED);
                } else if (apt.getTimeSlot().getStartTime().isBefore(LocalTime.now())) {
                    System.out.println("Would you like to set appointment " + apt.getAppointmentID() + " to completed? Enter 1 for yes or -1 to exit.");
                    int choice2 = doctorView.getChoice();
                    while (choice2 != 1 && choice2 != -1) {
                        System.out.println("Invalid option! Try again!");
                        choice2 = doctorView.getChoice();
                    }
                    if (choice2 == -1) return;
                    else if (choice2 == 1) {
                        apt.setStatus(Status.COMPLETED);
                    }
                } else {
                    System.out.println("Appointment has not started!");
                    return;
                }
            } else {
                System.out.println("Appointment has not started!");
                System.out.println("Would you like to forcefully set as completed? \n1. Yes \n-1. Exit");
                int choice3 = doctorView.getChoice();
                while (choice3 != 1 && choice3 != -1) {
                    System.out.println("Invalid option! Try again!");
                    choice3 = doctorView.getChoice();
                }
                if (choice3 == 1) {
                    apt.setStatus(Status.COMPLETED);
                    apt.setPrescriptionStatus(Status.COMPLETED);
                } else return;
            }
        }
        if (apt.getStatus() == Status.COMPLETED) {
            System.out.println("What would you like to Record? \n1. Service Type \n2. Prescription \n3. Consultation notes \n-1. Exit");
            int choice = doctorView.getChoice();
            while (choice != 1 && choice != 2 && choice != -1 && choice != 3) {
                System.out.println("Invalid option! Try again!");
                choice = doctorView.getChoice();
            }
            if (choice == -1) return;
            else if (choice == 1) {
                AppointmentController.setServiceType(apt, scanner);
            } else if (choice == 2) {
                AppointmentController.setPrescription(apt, scanner);
            } else if (choice == 3) {
                AppointmentController.setNotes(apt, scanner);
            }
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
				requestID = doctorView.getChoice();
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
						throw new RuntimeException("Request already accepted!");
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