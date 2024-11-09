package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Doctor extends User {
    private Schedule schedule;
    private ArrayList<AppointmentRequest> requests;
	private ArrayList<String> messages;
	private HashMap<Patient, Integer> appointmentCounter;
	private int unreadIndex;

    public Doctor(String id, String password, String name) {
        super(id, password, name, "Doctor");
        this.schedule = new Schedule(id);
        this.requests = new ArrayList<AppointmentRequest>();
		this.messages = new ArrayList<String>();
		this.appointmentCounter = new HashMap<>();
		this.unreadIndex = 0;
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

	public void addAppointmentCounter (Patient patient) {
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

	public int getUnreadIndex() {
		return unreadIndex;
	}
	
	public void setUnreadIndex(int i) {
		this.unreadIndex = i;
	}
}
