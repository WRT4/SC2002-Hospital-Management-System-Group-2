package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Doctor extends User {
    private Schedule schedule;
    private ArrayList<AppointmentRequest> requests;
	private ArrayList<Patient> patientsUnderCare;
	private ArrayList<String> messages;
	private HashMap<Patient, Integer> appointmentCounter;

    public Doctor(String id, String name) {
        super(id, name, "Doctor");
        this.schedule = new Schedule(id);
        this.requests = new ArrayList<AppointmentRequest>();
		this.patientsUnderCare = new ArrayList<Patient>();
		this.messages = new ArrayList<String>();
		this.appointmentCounter = new HashMap<>();
    }

	@Override
	public void showMenu() {
		// TODO Auto-generated method stub

	}

    public String toString() {
    	return "Doctor ID: " + getId() + " Name: " + getName();
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

	public ArrayList<String> getMessages(){
		return messages;
	}

	public HashMap<Patient, Integer> getAppointmentCounter(){
		return appointmentCounter;
	}

	public void addRequest(AppointmentRequest request) {
		requests.add(request);
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

	public ArrayList<AppointmentRequest> getRequests() {
		return requests;
	}
	
	// Placeholder methods for getting patient, diagnosis, and prescription
    public Patient getPatient(Scanner scanner) {
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

	public ArrayList<Patient> getPatientsUnderCare() {
		return this.patientsUnderCare;
	}

	public void addAppointmentCounter (Patient patient) {
		getPatientsUnderCare().add(patient);
		appointmentCounter.put(patient, appointmentCounter.getOrDefault(patient, 0) + 1);
	}

	public void subtractAppointmentCounter(Patient patient) {
		int currentCount = appointmentCounter.get(patient);
		if (currentCount > 1) {
			appointmentCounter.put(patient, currentCount - 1);
		} else {
			// Remove the patient from the map if counter becomes 0
			appointmentCounter.remove(patient);
		}
	}

	public ArrayList<AppointmentRequest> checkPendingRequests(){
		ArrayList<AppointmentRequest> pendingRequests = new ArrayList<>();
		for (AppointmentRequest request: this.getRequests()){
			if (request.getStatus()==Status.PENDING && ChronoUnit.DAYS.between(LocalDate.now(), request.getDate()) < 3){
				pendingRequests.add(request);
			}
		}
		return pendingRequests;
	}

	public void pushPendingRequestMessage(AppointmentRequest urgentRequest){
		String pendingRequest = "Urgent: Please be reminded that you have a pending appointment request: \n" + urgentRequest.toString();
		getMessages().add(0,pendingRequest);
	}
}
