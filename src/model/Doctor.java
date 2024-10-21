package model;

import java.util.ArrayList;
import java.util.Scanner;

public class Doctor extends User {
    private Schedule schedule;
    private ArrayList<Appointment> appointments;
    Scanner scanner = new Scanner(System.in);

    public Doctor(String id, String name) {
        super(id, name, "Doctor");
        this.schedule = new Schedule(id);
        this.appointments = new ArrayList<>();
    }

    public void setAvailability(String date, String time) {
        schedule.setAvailability(date, time);
    }

    public void viewAppointments() {
        for (Appointment appointment : appointments) {
            System.out.println(appointment);
        }
    }

    public void viewSchedule() {
        schedule.viewSchedule();
    }

    public void viewMedicalRecords(Patient patient) {
        for (Appointment appointment : appointments) {
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
                    String date = Appointment.inputDate();
                    System.out.println("Enter time (HH:MM):");
                    String time = Appointment.inputTime();
                    setAvailability(date, time);
                    break;
                case 5:
                    // Implement accept or decline appointment requests
                    break;
                case 6:
                    viewAppointments();
                    break;
                case 7:
                    // Implement record appointment outcome
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

}
