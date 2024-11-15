package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import controller.DoctorController;
import controller.SessionController;

public class Doctor extends User {
    private Schedule schedule;
    private ArrayList<AppointmentRequest> requests;
	private HashMap<Patient, Integer> appointmentCounter;
	private String gender;
    private int age;
    
    public Doctor(String id, String name, String gender, int age) {
        super(id, name, "password", "Doctor");
        this.gender = gender;
        this.age = age;
        this.requests = new ArrayList<AppointmentRequest>();
        this.appointmentCounter = new HashMap<>();
        this.schedule = new Schedule(id);
    }

    public Doctor(String id, String name, String password) {
        super(id, name, password, "Doctor");
        this.schedule = new Schedule(id);
        this.requests = new ArrayList<AppointmentRequest>();
		this.appointmentCounter = new HashMap<>();
    }
    
    public Doctor(String id,String name) {
        this(id, name, "password");
    }
   

    public String toString() {
    	return "Doctor ID: " + getID() + " Name: " + getName();
    }

	public Schedule getSchedule() {
		return schedule;
	}
	
	public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
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
	
	@Override
	public SessionController createController(Scanner scanner) {
		System.out.println("Accessing Doctor Dashboard...");
		return new DoctorController(this, scanner);
	}

}
