package model;

import java.time.LocalDate;
import application.Database;
import enums.Status;

/**
 * Represents a request for an appointment in the Hospital Management System (HMS).
 * It includes information about the requesting patient, the assigned doctor, date,
 * and timeslot. Each request can be accepted, declined, or cancelled.
 * @author Hoo Jing Huan, Lee Kuan Rong, Lim Wee Keat, Tan Wen Rong, Yeoh Kai Wen
 * @version 1.0
 * @since 2024-11-18
 */

public class AppointmentRequest implements Request {

	private static final long serialVersionUID = 9181140749468462052L;
	private Doctor doctor;
	private Patient patient;
	private LocalDate date;
	private TimeSlot timeslot;
	private Status status;
	private int requestID;

	/**
	 * Constructs an AppointmentRequest with a specified date, timeslot, doctor, and patient.
	 * The request is assigned a unique request ID and the initial status is set to PENDING.
	 *
	 * @param date     The date of the requested appointment
	 * @param timeslot The timeslot of the requested appointment
	 * @param doctor   The doctor for the requested appointment
	 * @param patient  The patient making the request
	 */
	public AppointmentRequest(LocalDate date, TimeSlot timeslot, Doctor doctor, Patient patient) {
		this.doctor = doctor;
		this.patient = patient;
		this.date = date;
		this.timeslot = timeslot;
		status = Status.PENDING;
		requestID = ++Database.appointmentRequestCount;
	}

	/**
	 * Provides a string representation of the appointment request, including request ID,
	 * patient name, doctor name, timeslot, and request status.
	 *
	 * @return A string containing details of the appointment request
	 */
	public String toString() {
		return "Request ID: " + requestID + ", Patient Name: " + patient.getName() + ", Doctor Name: " + doctor.getName() + ", " + timeslot.toString() + ", Status: " + status;
	}

	/**
	 * Gets the current status of the appointment request.
	 *
	 * @return The status of the appointment request
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * Updates the status of the appointment request.
	 *
	 * @param status The new status for the request
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * Gets the unique request ID for this appointment request.
	 *
	 * @return The request ID
	 */
	public int getRequestID() {
		return requestID;
	}

	/**
	 * Gets the patient associated with this appointment request.
	 *
	 * @return The patient making the request
	 */
	public Patient getPatient() {
		return this.patient;
	}

	/**
	 * Gets the date of the appointment request.
	 *
	 * @return The requested date for the appointment
	 */
	public LocalDate getDate() {
		return this.date;
	}

	/**
	 * Gets the doctor associated with this appointment request.
	 *
	 * @return The doctor assigned to this request
	 */
	public Doctor getDoctor() {
		return doctor;
	}

	/**
	 * Gets the timeslot for the appointment request.
	 *
	 * @return The requested timeslot for the appointment
	 */
	public TimeSlot getTimeSlot() {
		return timeslot;
	}

	/**
	 * Accepts the appointment request by creating an appointment and updating the status.
	 * The appointment is added to the doctor's schedule and the patient's appointments.
	 * The appointment status is set to CONFIRMED.
	 */
	public void acceptRequest() {
		this.status = Status.ACCEPTED;
		Appointment temp = new Appointment(date, timeslot, doctor, patient);
		patient.getAppointments().add(temp);
		doctor.getSchedule().getAppointments().add(temp);
		temp.setStatus(Status.CONFIRMED);
		System.out.println("Appointment Accepted!");
	}

	/**
	 * Declines the appointment request and updates the status.
	 * A message is displayed to indicate the request has been declined.
	 */
	public void declineRequest() {
		this.status = Status.DECLINED;
		System.out.println("Appointment Declined!");
	}

	/**
	 * Cancels the appointment request by updating its status to CANCELLED.
	 */
	public void cancelRequest() {
		this.status = Status.CANCELLED;
	}
}
