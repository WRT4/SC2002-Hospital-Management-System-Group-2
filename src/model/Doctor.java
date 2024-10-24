package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Doctor extends User {
    private Schedule schedule;
    private ArrayList<AppointmentRequest> requests;
 
    Scanner scanner = new Scanner(System.in);

    public Doctor(String id, String name) {
        super(id, name, "Doctor");
        this.schedule = new Schedule(id);
        this.requests = new ArrayList<AppointmentRequest>();
    }

    public void setAvailability(LocalDate date, LocalTime time) {
        schedule.setAvailability(date, time);
    }

    public void viewAppointments() {
        for (Appointment appointment : schedule.getAppointments()) {
            System.out.println(appointment);
        }
    }

    public void viewSchedule() {
        schedule.viewSchedule();
    }

    public void viewMedicalRecords(Patient patient) {
        for (Appointment appointment : schedule.getAppointments()) {
            System.out.println(appointment);
        }
    }

    public void updateMedicalRecord(Patient patient, String diagnosis, String prescription) {
        MedicalRecord record = patient.getMedicalRecord();
        record.addDiagnosis(diagnosis, this);
        record.addPrescription(prescription);
        System.out.println("Medical record updated for patient: " + patient.getName());
    }

    @Override
    public void showMenu() {
        System.out.println("1. View Patient Medical Records");
        System.out.println("2. Update Patient Medical Records");
        System.out.println("3. View Personal Schedule");
        System.out.println("4. Set Availability for Appointments");
        System.out.println("5. Accept or Decline Appointment Requests");
        System.out.println("6. View Upcoming Appointments");
        System.out.println("7. Record Appointment Outcome");
        System.out.println("8. Logout");
        int choice;
        do {
            System.out.println("Choose an action:");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Assuming you have a method to get a patient object
                    Patient patient = getPatient();
                    viewMedicalRecords(patient);
                    break;
                case 2:
                    // Assuming you have methods to get diagnosis and prescription
                    patient = getPatient();
                    String diagnosis = getDiagnosis();
                    String prescription = getPrescription();
                    updateMedicalRecord(patient, diagnosis, prescription);
                    break;
                case 3:
                    viewSchedule();
                    break;
                case 4:
                    System.out.println("Enter date (YYYY-MM-DD):");
                    LocalDate date = Schedule.inputDate();
                    System.out.println("Enter time (HH:MM):");
                    LocalTime time = Schedule.inputTime();
                    setAvailability(date, time);
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
                	viewAppointmentOutcomes();
                    break;
                case 8:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 8);
    }

    // Placeholder methods for getting patient, diagnosis, and prescription
    private Patient getPatient() {
        // Implement method to get a patient object
        return new Patient("P001", "John Doe",null);
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

	public void addRequest(AppointmentRequest request) {
		requests.add(request);
	}
	
	public void viewRequests() {
		for (AppointmentRequest request: requests) {
			if (request.getStatus() == Status.PENDING)
				System.out.println(request);
		}
		System.out.println("Which requestID would you like to accept/reject?");
		Scanner sc = new Scanner(System.in);
		int requestID;
		AppointmentRequest request = null;
		while (true) {
			try {
				System.out.println("Enter requestID or -1 to exit: ");
				requestID = sc.nextInt();
				if (requestID == -1) return;
				int i = 0;
				for (i = 0; i < requests.size(); i++) {
		            if (requests.get(i).getRequestID() == requestID) {
		                request = requests.get(i);
		                break;
		            }
		        }
				if (i == requests.size()) throw new RuntimeException("ID does not exist! ");
				break;
			}
			catch (InputMismatchException e) {
				System.out.println("Wrong input type! Try Again!");
				sc.nextLine();
				continue;
			}
			catch (RuntimeException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
				continue;
			}
			catch (Exception e) {
				System.out.println("Error! Try Again!");
				sc.nextLine();
				continue;
			}
		}
		System.out.println("Please input 1 for Accept or 2 for Reject");
		int choice;
		while (true) {
			try {
				choice = sc.nextInt();
				if (choice != 1 || choice != 2) throw new RuntimeException("Choice does not exist! ");
				break;
			}
			catch (InputMismatchException e) {
				System.out.println("Wrong input type! Try Again!");
				sc.nextLine();
				continue;
			}
			catch (RuntimeException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
				continue;
			}
			catch (Exception e) {
				System.out.println("Error! Try Again!");
				sc.nextLine();
				continue;
			}
		}
		if (choice == 1) {
			request.acceptRequest();
		}
		else {
			request.declineRequest();
		}
		
	}
	
	public void viewAppointmentOutcomes() {
		ArrayList<Appointment> appointments = schedule.getAppointments();
		for (Appointment appointment : appointments) {
			if (appointment.getStatus() == Status.COMPLETED) {
				appointment.printAppointmentOutcome();
			}
		}
	}

	public int findAppointment(int id) {
		for (int i = 0; i < requests.size(); i++) {
			if (schedule.getAppointments().get(i).getAppointmentID() == id) {
				return i;
			}
		}
		return -1;
	}

	public void removeAppointment(Appointment appointment) {
		schedule.getAppointments().remove(appointment);
	}

}
