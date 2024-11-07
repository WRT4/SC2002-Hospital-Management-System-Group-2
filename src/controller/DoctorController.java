package controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import model.*;

public class DoctorController {
    static Scanner scanner = new Scanner(System.in);
    
    public static void remindPendingRequests(Doctor doctor){
		for (AppointmentRequest request: doctor.getRequests()){
			if (request.getStatus()==Status.PENDING && ChronoUnit.DAYS.between(LocalDate.now(), request.getDate()) < 3){
				String pendingRequest = "Please be reminded that you have a pending appointment request: \n" + request.toString();
				doctor.getMessages().add(0, pendingRequest);
			}
		}
	}
    
    public static void sendAcceptanceMessage(AppointmentRequest request, boolean accepted) {
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
    	request.getPatient().getMessages().add(0, message);
    }
    
    public static void sendCancellationMessage(Appointment tempApp) {
    	String messageToPatient;
    	Doctor doctor = tempApp.getDoctor();
    	TimeSlot timeslot = tempApp.getTimeSlot();
		messageToPatient = "Message at " + LocalDate.now() + ": Appointment cancelled by " + doctor.getName() + "\n" +
				"Appointment Details - " + timeslot.toString();
		tempApp.getPatient().getMessages().add(0,messageToPatient);
    }
    
    public static int getChoice() {
        System.out.print("Enter choice: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Enter a number.");
            scanner.next();
        }
        return scanner.nextInt();
    }
    
    private static String getDiagnosis() {
        System.out.println("Enter diagnosis:");
        return scanner.nextLine();
    }

    private static String getPrescription() {
        System.out.println("Enter prescription:");
        return scanner.nextLine();
    }
    
	public static void viewMedicalRecords(Doctor doctor) {
    	Patient patient = doctor.getPatient(scanner);
		if (patient != null){
			patient.getRecord().printMedicalRecord();
		}
    }
	
	public static void updateMedicalRecord(Doctor doctor) {
    	Patient patient = doctor.getPatient(scanner);
		if (patient==null){
			return;
		}
        System.out.println("Enter -1 to exit.");
        String diagnosis = getDiagnosis();
        if (diagnosis.equals("-1")) return;
        String prescription = getPrescription();
        if (prescription.equals("-1")) return;
        MedicalRecord record = patient.getRecord();
        record.addDiagnosis(diagnosis, doctor);
        record.addPrescription(prescription);
        System.out.println("Medical record updated for patient: " + patient.getName());
    }
	
	public static void viewSchedule(Doctor doctor) {
    	System.out.println("Viewing Personal Schedule for Doctor " + doctor.getId() + ", Name: " + doctor.getName() + ": ");
		System.out.println("Enter date: ");
		LocalDate date = ScheduleController.inputDate(false);
		if (date == null) return;
		while (date.isBefore(doctor.getSchedule().getWorkingSlots().get(0).getDate())) {
			System.out.println("No record found! ");
			date = ScheduleController.inputDate(false);
			if (date == null) return;
		}
		ScheduleController.viewAllSlots(date, doctor.getSchedule());
    }
	
	public static void setAvailability(Doctor doctor) {
    	System.out.println("Would you like to \n1. Set unavailable timeslot \n2. Free unavailable timeslot \n-1. Exit");
    	int choice = getChoice();
    	while (choice != 1 && choice != 2 && choice != -1) {
    		System.out.println("No such option! Try again!");
    		choice = getChoice();
    	}
    	if (choice == 1) {
    		System.out.println("Enter date (YYYY-MM-DD):");
            LocalDate date = ScheduleController.inputDate(false);
            if (date == null) return;
            System.out.println("Enter time (HH:MM):");
            LocalTime time = ScheduleController.inputTime();
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
    		System.out.println("Enter date (YYYY-MM-DD):");
            LocalDate date = ScheduleController.inputDate(false);
            if (date == null) return;
            System.out.println("Enter time (HH:MM):");
            LocalTime time = ScheduleController.inputTime();
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
            	int choice2 = getChoice();
            	while (choice2 != 1 && choice2 != 2 ) {
            		System.out.println("No such option! Try again!");
            		choice2 = getChoice();
            	}
            	if (choice2 == 1) {
            		temp.getDoctor().getSchedule().setAvailability(temp.getDate(), temp.getTimeSlot());
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
	
	public static AppointmentRequest findRequest(ArrayList<AppointmentRequest> requests, boolean isPatient) {
		AppointmentRequest request = null;
		while (true) {
			try {
				request = null;
				int requestID;
				System.out.println("Enter requestID or -1 to exit: ");
				requestID = scanner.nextInt();
				if (requestID == -1) return null;
				int i = 0;
				for (i = 0; i < requests.size(); i++) {
		            if (requests.get(i).getRequestID() == requestID) {
		                request = requests.get(i);
		                break;
		            }
		        }
				if (request == null) throw new RuntimeException("RequestID does not exist! ");
				if (request.getStatus() != Status.PENDING) {
					if (request.getStatus() == Status.CANCELLED) {
						throw new RuntimeException("Request already cancelled!");
					}
					else if (request.getStatus() == Status.ACCEPTED) {
						if (isPatient) throw new RuntimeException("Request already accepted! Cancel appointment instead!");
						throw new RuntimeException("Request already accepted!");
					}
					else if (request.getStatus() == Status.DECLINED) {
						throw new RuntimeException("Request already declined!");
					}
				}
				break;
			}
			catch (InputMismatchException e) {
				System.out.println("Wrong input type! Try Again!");
				scanner.nextLine();
				continue;
			}
			catch (RuntimeException e) {
				System.out.println(e.getMessage());
				scanner.nextLine();
				continue;
			}
			catch (Exception e) {
				System.out.println("Error! Try Again!");
				scanner.nextLine();
				continue;
			}
		}
		return request;
	}
	
	public static void viewRequests(Doctor doctor) {
		int num = 0;
		ArrayList<AppointmentRequest> requests = doctor.getRequests();
		for (AppointmentRequest request: requests) {
			if (request.getStatus() == Status.PENDING){
				System.out.println(request);
				num++;
			}
		}
		if (num == 0) {
			System.out.println("No appointment requests yet!");
			return;
		}
		System.out.println("Which requestID would you like to accept/reject?");
		AppointmentRequest request = findRequest(requests, false);
		if (request == null) return;
		System.out.println("Please input 1 for Accept or 2 for Reject. Enter -1 to exit.");
		int choice = getChoice();
		while (choice != 1 && choice != 2 && choice != -1) {
			System.out.println("Invalid option! Try again!");
			choice = getChoice();
		}
		if (choice == 1) {
			// check overlap of requests or busy
			if (doctor.getSchedule().checkOverlapping(request.getTimeSlot()) || request.getTimeSlot().getOccupied()) {
				System.out.println("Timeslot is not available! Would you like to reject request instead? 1. Yes -1. Exit");
				int choice2 = getChoice();
				while (choice2 != 1 && choice2 != -1) {
					choice2 = getChoice();
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
			doctor.getPatientsUnderCare().add(request.getPatient());
		}
		else if (choice == 2) {
			request.declineRequest();
			sendAcceptanceMessage(request,false);
		}
		else 
			return;
		
	}
	
	public static void viewAppointments(Doctor doctor) {
    	int num = 0;
        for (Appointment appointment : doctor.getSchedule().getAppointments()) {
            if (appointment.getStatus() == Status.CONFIRMED) {
            	System.out.println(appointment);
            	num++;
            }
        }
        if (num == 0) {
			System.out.println("No scheduled appointments! ");
			return;
		}
        System.out.println("Would you like to cancel an appointment? 1. Yes -1. Exit");
        int choice = getChoice();
		while(choice != 1 && choice != -1) {
			System.out.println("Option doesn't exist! ");
			choice = getChoice();
		}
		if (choice == -1) return;
		if (choice == 1) {
			System.out.println("Which AppoitnmentID would would like to cancel? Enter ID or -1 to exit.");
			Appointment apt = findAppointment(doctor.getSchedule().getAppointments(), true);
			if (apt == null) return;
			apt.setStatus(Status.CANCELLED);
			System.out.println("Successfully cancelled!");
		}
    }
	
	public static Appointment findAppointment(ArrayList<Appointment> appointments, boolean noConfirmed) {
		Appointment apt = null;
		while (true) {
			try {
				apt = null;
				int id = getChoice();
				if (id == -1) return null;
				for (Appointment appointment : appointments) {
					if (appointment.getAppointmentID() == id) {
						apt = appointment;
						break;
					}
				}
				if (apt == null) {
					throw new RuntimeException("AppointmentID does not exist! ");
				}
				if (apt.getStatus() == Status.CANCELLED) {
					throw new RuntimeException("Appointment had been cancelled! ");
				}
				if (noConfirmed) {
					if (apt.getStatus() == Status.COMPLETED) {
						throw new RuntimeException("Appointment had been completed! ");
					}
				}
				break;
			}
			catch (InputMismatchException e) {
				System.out.println("Wrong input type! Try Again!");
				scanner.nextLine();
				continue;
			}
			catch (RuntimeException e) {
				System.out.println(e.getMessage());
				scanner.nextLine();
				continue;
			}
			catch (Exception e) {
				System.out.println("Error! Try Again!");
				scanner.nextLine();
				continue;
			}
		}
		return apt;
	}
	
	public static void recordAppointmentOutcomes(Doctor doctor) {
		int num1 = 0,num2 = 0;
		ArrayList<Appointment> appointments = doctor.getSchedule().getAppointments();
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
				appointment.printAppointmentOutcome();
				num2++;
			}
		}
		if (num2 == 0) {
			System.out.println("No completed appointments!");
		}
		System.out.println();
		System.out.println("Which appointment ID would you like to record outcome for? Enter ID or -1 to exit: ");
		Appointment apt = findAppointment(appointments, false);
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
					int choice2 = getChoice();
					while (choice2 != 1 && choice2 != -1) {
						System.out.println("Invalid option! Try again!");
						choice2 = getChoice();
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
				int choice3 = getChoice();
				while (choice3 != 1 && choice3 != -1) {
					System.out.println("Invalid option! Try again!");
					choice3 = getChoice();
				}
				if (choice3 == 1) {
					apt.setStatus(Status.COMPLETED);
				}
				else return;
				
			}			
		}
		if (apt.getStatus() == Status.COMPLETED) {
			System.out.println("What would you like to Record? \n1. Service Type \n2. Prescription \n3. Consultation notes \n-1. Exit");
			int choice = getChoice();
			while (choice != 1 && choice != 2 && choice != -1 && choice != 3) {
				System.out.println("Invalid option! Try again!");
				choice = getChoice();
			}
			if (choice == -1) return;
			else if (choice == 1) {
				System.out.println("Setting Service Type...");
				System.out.println("Enter Service Type: ");
				String ser = scanner.next();
				apt.setServiceType(ser);
				System.out.println("Service Type added!");
			}
			else if (choice == 2) {
				System.out.println("Setting prescription...");
				System.out.println("Enter prescription: ");
				String pres = scanner.next();
				apt.setPrescription(pres);
				apt.getPatient().getRecord().addPrescription(pres);
				apt.setPrescriptionStatus(Status.PENDING);
				// send prescription request
				System.out.println("Prescription added!");
			}
			else if (choice == 3) {
				System.out.println("Setting Consultation notes...");
				System.out.println("Enter Consultation notes: ");
				String note = scanner.next();
				apt.setNotes(note);
				System.out.println("Consultation notes added!");
			}
			
		}
		
		
	}
	
	
}
