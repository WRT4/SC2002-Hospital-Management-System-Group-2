package controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

import application.Database;
import model.*;
import view.AppointmentView;
import view.DoctorView;
import view.ScheduleView;
import enums.Status;

public class DoctorController extends SessionController {
	
    private Doctor doctor;
    private DoctorView doctorView;
    
    public DoctorController(Doctor d, Scanner s) {
    	doctor = d;
    	scanner = s;
    	doctorView = new DoctorView(s);
    	unreadIndex = d.getUnreadIndex();
    	startTime = LocalTime.now();
		startDate = LocalDate.now();
    }
    
    public void showMenu() {
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
                	doctorView.viewSchedule(doctor);
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
                    System.out.println("Logging out...");
                    String log = "Doctor " + doctor.getID() + " accessed system from " + startTime + " on " + startDate + " to " + LocalTime.now() + " on " + LocalDate.now(); 
                    Database.systemLogs.add(log);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 9);
    }
    
    public void setUnreadIndex(int i) {
    	this.unreadIndex = i;
    }
    
    public ArrayList<AppointmentRequest> checkPendingRequests(){
		ArrayList<AppointmentRequest> pendingRequests = new ArrayList<>();
		for (AppointmentRequest request: doctor.getRequests()){
			if (request.getStatus()==Status.PENDING && ChronoUnit.DAYS.between(LocalDate.now(), request.getDate()) < 3){
				pendingRequests.add(request);
			}
		}
		return pendingRequests;
	}
    
	public void pushPendingRequestMessage(AppointmentRequest urgentRequest){
		String pendingRequest = "Urgent: Please be reminded that you have a pending appointment request: \n" + urgentRequest.toString();
		doctor.getMessages().add(pendingRequest);
	}
    
    public void remindPendingRequests(){
		for (AppointmentRequest request: checkPendingRequests()){
			pushPendingRequestMessage(request);
		}
	}
    
    public void sendAcceptanceMessage(AppointmentRequest request, boolean accepted) {
    	String message;
    	Doctor doctor = request.getDoctor();
    	TimeSlot timeslot = request.getTimeSlot();
    	if (accepted == true) {
    		message = "Message at " + LocalDate.now() + ": Appointment request accepted by " + doctor.getName() + "\n" +
    				"Appointment Details - " + timeslot.toString();
    	}
    	else {
    		message = "Message at " + LocalDate.now() + ": Appointment request rejected by " + doctor.getName() + "\n" +
    				"Rejected request Details - " + timeslot.toString();
    	}
    	request.getPatient().getMessages().add(message);
    }
    
    public void sendCancellationMessage(Appointment tempApp) {
    	String messageToPatient;
    	TimeSlot timeslot = tempApp.getTimeSlot();
		messageToPatient = "Message at " + LocalDate.now() + ": Appointment cancelled by " + doctor.getName() + "\n" +
				"Appointment Details - " + timeslot.toString();
		tempApp.getPatient().getMessages().add(messageToPatient);
    }
    
    private String getDiagnosis() {
        System.out.println("Enter diagnosis:");
        return scanner.nextLine();
    }

    private String getPrescription() {
        System.out.println("Enter prescription:");
        return scanner.nextLine();
    }

	public void viewMedicalRecord(){
		Patient patient = doctorView.getPatient(doctor);
		if (patient==null){
			return;
		}
		if (doctor.getAppointmentCounter().containsKey(patient) && doctor.getAppointmentCounter().get(patient) > 0) {
			doctorView.viewMedicalRecords(patient, doctor);
		} else {
			System.out.println("Cannot view medical record of " + patient.getName() + " as no appointments have been scheduled.");
		}
	}
	
	public void updateMedicalRecord() {
    	Patient patient = doctorView.getPatient(doctor);
		if (patient==null){
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
	
	public void setAvailability() {
    	System.out.println("Would you like to \n1. Set unavailable timeslot \n2. Free unavailable timeslot \n-1. Exit");
    	int choice = doctorView.getChoice();
    	while (choice != 1 && choice != 2 && choice != -1) {
    		System.out.println("No such option! Try again!");
    		choice = doctorView.getChoice();
    	}
    	if (choice == 1) {
            LocalDate date = ScheduleView.inputDate(false);
            if (date == null) return;
            LocalTime time = ScheduleView.inputTime();
            if (time == null) return;
            TimeSlot timeslot = doctor.getSchedule().findTimeSlot(date, time);
            if (timeslot == null) {
            	System.out.println("No such timeslot!");
            	return;
            }
            if (timeslot.getOccupied() == true) {
            	System.out.println("Timeslot already busy! ");
            	return;
            }
            timeslot.setOccupied();
            System.out.println("Date: " + date + " Time: " + time + " successfully set as unavailable!");
            return;
    	}
    	else if (choice == 2) {
            LocalDate date = ScheduleView.inputDate(false);
            if (date == null) return;
            LocalTime time = ScheduleView.inputTime();
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
            	System.out.println("Date: " + date + " Time: " + time + " successfully freed");
            	return;
            }
            else {
            	System.out.println("Cancel " + temp + "?" + "\n1. Yes \n2. No");
            	int choice2 = doctorView.getChoice();
            	while (choice2 != 1 && choice2 != 2 ) {
            		System.out.println("No such option! Try again!");
            		choice2 = doctorView.getChoice();
            	}
            	if (choice2 == 1) {
            		temp.getDoctor().getSchedule().setAvailability(temp.getTimeSlot());
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
	
	public void viewRequests() {
		doctorView.viewRequests(doctor);
		ArrayList<AppointmentRequest> requests = doctor.getRequests();
		System.out.println("Which requestID would you like to accept/reject?");
		AppointmentRequest request = RequestController.findRequest(requests, false, scanner);
		if (request == null) return;
		System.out.println("Please input 1 for Accept or 2 for Reject. Enter -1 to exit.");
		int choice = doctorView.getChoice();
		while (choice != 1 && choice != 2 && choice != -1) {
			System.out.println("Invalid option! Try again!");
			choice = doctorView.getChoice();
		}
		if (choice == 1) {
			// check overlap of requests or busy
			if (doctor.getSchedule().checkOverlapping(request.getTimeSlot())) {
				System.out.println("Timeslot is not available! Would you like to reject request instead? 1. Yes -1. Exit");
				int choice2 = doctorView.getChoice();
				while (choice2 != 1 && choice2 != -1) {
					choice2 = doctorView.getChoice();
				}
				if (choice2 == -1) return;
				else {
					request.declineRequest();
					sendAcceptanceMessage(request,false);
					return;
				}
			}
			request.acceptRequest();
			sendAcceptanceMessage(request,true);
			doctor.addAppointmentCounter(request.getPatient());
//			doctor.getPatientsUnderCare().add();


		}
		else if (choice == 2) {
			request.declineRequest();
			sendAcceptanceMessage(request,false);
		}
		else 
			return;
	}
	
	public void viewAppointments() {
    	int notEmpty = doctorView.viewAppointments(doctor);
    	if (notEmpty == 0) return;
        System.out.println("Would you like to cancel an appointment? 1. Yes -1. Exit");
        int choice = doctorView.getChoice();
		while(choice != 1 && choice != -1) {
			System.out.println("Option doesn't exist! ");
			choice = doctorView.getChoice();
		}
		if (choice == -1) return;
		if (choice == 1) {
			int success = cancelAppointment();
			if (success == 0) return;
		}
    }
	
	public int cancelAppointment() {
		System.out.println("Which AppointmentID would would like to cancel? Enter ID or -1 to exit.");
		Appointment apt = AppointmentView.promptForAppointment(doctor.getSchedule().getAppointments(), true, scanner);
		if (apt == null) return 0;
		apt.setStatus(Status.CANCELLED);
		apt.getTimeSlot().free();
		doctor.subtractAppointmentCounter(apt.getPatient());
		sendCancellationMessage(apt);
		System.out.println("Successfully cancelled!");
		return 1;
	}
	
	public void recordAppointmentOutcomes() {
		doctorView.viewAppointmentOutcomes(doctor);
		ArrayList<Appointment> appointments = doctor.getSchedule().getAppointments();
		System.out.println();
		System.out.println("Which appointment ID would you like to record outcome for? Enter ID or -1 to exit: ");
		Appointment apt = AppointmentView.promptForAppointment(appointments, false, scanner);
		if (apt == null) return;
		if (apt.getStatus() == Status.CONFIRMED) {
			if (apt.getTimeSlot().getDate().isBefore(LocalDate.now().plusDays(1))) {
				if (apt.getTimeSlot().getDate().isBefore(LocalDate.now())){
					apt.setStatus(Status.COMPLETED);
				}
				else if (apt.getTimeSlot().getEndTime().isBefore(LocalTime.now())) {
					apt.setStatus(Status.COMPLETED);
				}
				else if (apt.getTimeSlot().getStartTime().isBefore(LocalTime.now())){
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
				}
				else {
					System.out.println("Appointment has not started!");
					return;
				}
			}
			else {
				System.out.println("Appointment has not started!");
				System.out.println("Would you like to forcefully set as completed? \n1. Yes \n-1. Exit");
				int choice3 = doctorView.getChoice();
				while (choice3 != 1 && choice3 != -1) {
					System.out.println("Invalid option! Try again!");
					choice3 = doctorView.getChoice();
				}
				if (choice3 == 1) {
					apt.setStatus(Status.COMPLETED);
				}
				else return;
				
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
			}
			else if (choice == 2) {
				AppointmentController.setPrescription(apt, scanner);
			}
			else if (choice == 3) {
				AppointmentController.setNotes(apt, scanner);
			}
			
		}
	}

}
