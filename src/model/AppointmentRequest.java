package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentRequest implements Request {
	private Doctor doctor;
	private Patient patient;
	private LocalDate date;
	private TimeSlot timeslot;
	private Status status;
	private static int count = 0;
	private int requestID;
	public AppointmentRequest(LocalDate date, TimeSlot timeslot, Doctor doctor, Patient patient) {
		this.doctor = doctor;
		this.patient = patient;
		this.date = date;
		this.timeslot = timeslot;
		status = Status.PENDING;
		requestID = ++count;
	}
	
	public void acceptRequest() {
		this.status = Status.ACCEPTED;
		Appointment temp = new Appointment(date, timeslot, doctor, patient);
		patient.getAppointments().add(temp);
		doctor.getSchedule().getAppointments().add(temp);
		temp.setStatus(Status.CONFIRMED);
		System.out.println("Appointment Accepted!");
	}
	
	public void declineRequest() {
		this.status = Status.DECLINED;
		System.out.println("Appointment Declined!");
	}
	
	public void cancelRequest() {
		this.status = Status.CANCELLED;
	}

	public String toString() {
		return "Request ID: " + requestID + " Name: " + patient.getName() + " Time: " + timeslot.toString() + " Status: " + status;
	}

	public Status getStatus() {
		return status;
	}
	
	public int getRequestID() {
		return requestID;
	}

}
