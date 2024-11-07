package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Doctor extends User {
    private Schedule schedule;
    private ArrayList<AppointmentRequest> requests;
	private ArrayList<Patient> patientsUnderCare;
	private ArrayList<String> messages;

    Scanner scanner = new Scanner(System.in);

    public Doctor(String id, String name) {
        super(id, name, "Doctor");
        this.schedule = new Schedule(id);
        this.requests = new ArrayList<AppointmentRequest>();
		this.patientsUnderCare = new ArrayList<Patient>();
		this.messages = new ArrayList<String>();
    }
    
    @Override
    public void showMenu() {
		remindPendingRequests();
		System.out.println("Message Box: ");
		int counter = 0;
		for (String message: messages){
			System.out.println((counter+1) + " - " + message);
			System.out.println();
			counter++;
		}
		if (counter ==0)
			System.out.println("No messages yet!");

		int choice;
        do {
        	System.out.println("Doctor Menu: ");
            System.out.println("1. View Patient Medical Records");
            System.out.println("2. Update Patient Medical Records");
            System.out.println("3. View Personal Schedule");
            System.out.println("4. Set Availability for Appointments / Update Personal Schedule");
            System.out.println("5. Accept or Decline Appointment Requests");
            System.out.println("6. View Upcoming Appointments");
            System.out.println("7. Record Appointment Outcome");
            System.out.println("8. Logout");
            System.out.println("Choose an action:");
            choice = getChoice();
            while (choice < 1 || choice > 8) {
            	System.out.println("Invalid option! Try again!");
				choice = getChoice();
            }
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Assuming you have a method to get a patient object
					viewMedicalRecords();
                    break;
                case 2:
                    // Assuming you have methods to get diagnosis and prescription
                    updateMedicalRecord();
                    break;
                case 3:
                    viewSchedule();
                    break;
                case 4:
                    setAvailability();
                    break;
                case 5:
                    // Implement accept or decline appointment requests
                	viewRequests();
                    break;
                case 6:
                    viewAppointments();
                    break;
                case 7:
                    // Implement record appointment outcome
                	recordAppointmentOutcomes();
                    break;
                case 8:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 8);
    }
    
    private int getChoice() {
        System.out.print("Enter choice: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Enter a number.");
            scanner.next();
        }
        return scanner.nextInt();
    }
    
    public void setAvailability() {
    	System.out.println("Would you like to \n1. Set unavailable timeslot \n2. Free unavailable timeslot \n-1. Exit");
    	int choice = getChoice();
    	while (choice != 1 && choice != 2 && choice != -1) {
    		System.out.println("No such option! Try again!");
    		choice = getChoice();
    	}
    	if (choice == 1) {
    		System.out.println("Enter date (YYYY-MM-DD):");
            LocalDate date = Schedule.inputDate(false);
            if (date == null) return;
            System.out.println("Enter time (HH:MM):");
            LocalTime time = Schedule.inputTime();
            if (time == null) return;
            TimeSlot timeslot = schedule.findTimeSlot(date, time);
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
            LocalDate date = Schedule.inputDate(false);
            if (date == null) return;
            System.out.println("Enter time (HH:MM):");
            LocalTime time = Schedule.inputTime();
            if (time == null) return;
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
					String messageToPatient;
					messageToPatient = "Message at " + LocalDate.now() + ": Appointment cancelled by " + getName() + "\n" +
							"Appointment Details - " + timeslot.toString();
					temp.getPatient().getMessage().add(0,messageToPatient);
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

    public void viewAppointments() {
    	int num = 0;
        for (Appointment appointment : schedule.getAppointments()) {
            if (appointment.getStatus() == Status.CONFIRMED) {
            	System.out.println(appointment);
            	num++;
            }
        }
        if (num == 0) {
			System.out.println("No scheduled appointments! ");
		}
    }
    
    public String toString() {
    	return "Doctor ID: " + getId() + " Name: " + getName();
    }

    public void viewSchedule() {
    	System.out.println("Viewing Personal Schedule: ");
		System.out.println("Enter date: ");
		LocalDate date = Schedule.inputDate(false);
		if (date == null) return;
		while (date.isBefore(schedule.getWorkingSlots().get(0).getDate())) {
			System.out.println("No record found! ");
			date = Schedule.inputDate(false);
			if (date == null) return;
		}
		schedule.viewAllSlots(date);
    }

    public void viewMedicalRecords() {
    	Patient patient = getPatient();
		if (patient != null){
			patient.getRecord().printMedicalRecord();
		}
    }

    public void updateMedicalRecord() {
    	Patient patient = getPatient();
		if (patient==null){
			return;
		}
        System.out.println("Enter -1 to exit.");
        String diagnosis = getDiagnosis();
        if (diagnosis.equals("-1")) return;
        String prescription = getPrescription();
        if (prescription.equals("-1")) return;
        MedicalRecord record = patient.getMedicalRecord();
        record.addDiagnosis(diagnosis, this);
        record.addPrescription(prescription);
        System.out.println("Medical record updated for patient: " + patient.getName());
    }

    // Placeholder methods for getting patient, diagnosis, and prescription
    private Patient getPatient() {
		if (patientsUnderCare==null || patientsUnderCare.size()==0){
			System.out.println("No patients under care.");
			return null;
		}
        // Implement method to get a patient object
    	for (Patient patient : patientsUnderCare) {
    		System.out.println(patient);
    	}
        System.out.println("Enter Patient ID or -1 to exit: ");
        String choice = scanner.next();
        if (choice.equals("-1")) return null;
        for (Patient patient : patientsUnderCare) {
        	if (choice.equals(patient.getPatientId())) {
        		return patient;
        	}
        }
		System.out.println("Patient not found. ");
        return null;
    }

    private String getDiagnosis() {
        System.out.println("Enter diagnosis:");
        return scanner.nextLine();
    }

    private String getPrescription() {
        System.out.println("Enter prescription:");
        return scanner.nextLine();
    }

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public ArrayList<String> getMessage(){
		return messages;
	}

	public void addRequest(AppointmentRequest request) {
		requests.add(request);
	}
	
	public void viewRequests() {
		int num = 0;
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
		int requestID;
		AppointmentRequest request = null;
		while (true) {
			try {
				System.out.println("Enter requestID or -1 to exit: ");
				requestID = scanner.nextInt();
				if (requestID == -1) return;
				int i = 0;
				for (i = 0; i < requests.size(); i++) {
		            if (requests.get(i).getRequestID() == requestID) {
		                request = requests.get(i);
		                break;
		            }
		        }
				if (request == null) throw new RuntimeException("ID does not exist! ");
				if (request.getStatus() != Status.PENDING) throw new RuntimeException("Request has been already accepted, declined or cancelled! ");
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
		System.out.println("Please input 1 for Accept or 2 for Reject. Enter -1 to exit.");
		int choice = getChoice();
		while (choice != 1 && choice != 2 && choice != -1) {
			System.out.println("Invalid option! Try again!");
			choice = getChoice();
		}
		if (choice == 1) {
			request.acceptRequest();
			patientsUnderCare.add(request.getPatient());
		}
		else if (choice == 2) {
			request.declineRequest();
		}
		else 
			return;
		
	}
	
	public void recordAppointmentOutcomes() {
		int num1 = 0,num2 = 0;
		ArrayList<Appointment> appointments = schedule.getAppointments();
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
		int id = getChoice();
		if (id == -1) return;
		Appointment apt = null;
		for (Appointment appointment : appointments) {
			if (appointment.getAppointmentID() == id) {
				apt = appointment;
				break;
			}
		}
		if (apt == null) {
			System.out.println("Appointment does not exist! ");
			return;
		}
		if (apt.getStatus() == Status.CANCELLED) {
			System.out.println("Appointment had been cancelled! ");
			return;
		}
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
				String note = scanner.nextLine();
				apt.setNotes(note);
				System.out.println("Consultation notes added!");
			}
			
		}
	}

	public Appointment findAppointment(int id) {
		for (Appointment apt : schedule.getAppointments()) {
			if (apt.getAppointmentID() == id) {
				return apt;
			}
		}
		return null;
	}

	public void removeAppointment(Appointment appointment) {
		schedule.getAppointments().remove(appointment);
	}
	
	public TimeSlot findTimeSlot(LocalDate date, LocalTime time) {
		TimeSlot timeslot = this.schedule.findTimeSlot(date, time);
		if (timeslot == null) {
			System.out.println("TimeSlot not found!");
			return null;
		}
		return timeslot;
	}

	public void remindPendingRequests(){
		for (AppointmentRequest request: requests){
			if (request.getStatus()==Status.PENDING && ChronoUnit.DAYS.between(LocalDate.now(), request.getDate()) < 3){
				String pendingRequest = "Please be reminded that you have a pending appointment request: \n" + request.toString();
				messages.add(0, pendingRequest);
			}
		}
	}
}
