package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentRequest implements Request {
	private Doctor doctor;
	private Patient patient;
	public AppointmentRequest(LocalDate date, LocalTime time, Doctor doctor, Patient patient) {
		// add enum status and request
		
	}
}
