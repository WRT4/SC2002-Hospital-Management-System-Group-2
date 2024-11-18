package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import enums.Status;

/**
 * Represents a request for an appointment in the medical system.
 * It includes information about the requesting patient, the assigned doctor, date,
 * and timeslot. Each request can be accepted, declined, or cancelled.
 */
public class AppointmentRequest implements Request {
	private Doctor doctor;
	private Patient patient;
	private LocalDate date;
	private TimeSlot timeslot;
	private Status status;
	private static int count = 0;
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
		requestID = ++count;
	}

	/**
	 * Provides a string representation of the appointment request, including request ID,
	 * patient name, doctor name, timeslot, and request status.
	 *
	 * @return A string containing details of the appointment request
	 */
	public String toString() {
		return "Request ID: " + requestID + " Patient Name: " + patient.getName() + ", Doctor Name: " + doctor.getName() + " " + timeslot.toString() + " Status: " + status;
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

	/**
	 * Finds a specific appointment request by ID from a list of requests.
	 * Allows the user to input the request ID via a scanner. The request can be filtered based on its status.
	 * Throws exceptions if the request is invalid or in certain states.
	 *
	 * @param requests  A list of appointment requests to search through
	 * @param isPatient If true, restricts actions based on the role of the patient
	 * @param scanner   A Scanner instance for reading user input
	 * @return The matching AppointmentRequest or null if exited by the user
	 */
	public static AppointmentRequest findRequest(ArrayList<AppointmentRequest> requests, boolean isPatient, Scanner scanner) {
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
				if (request == null) throw new RuntimeException("RequestID does not exist!");
				if (request.getStatus() != Status.PENDING) {
					if (request.getStatus() == Status.CANCELLED) {
						throw new RuntimeException("Request already cancelled!");
					} else if (request.getStatus() == Status.ACCEPTED) {
						if (isPatient) throw new RuntimeException("Request already accepted! Cancel appointment instead!");
						throw new RuntimeException("Request already accepted!");
					} else if (request.getStatus() == Status.DECLINED) {
						throw new RuntimeException("Request already declined!");
					}
				}
				break;
			} catch (InputMismatchException e) {
				System.out.println("Wrong input type! Try Again!");
				scanner.nextLine();
				continue;
			} catch (RuntimeException e) {
				System.out.println(e.getMessage());
				scanner.nextLine();
				continue;
			} catch (Exception e) {
				System.out.println("Error! Try Again!");
				scanner.nextLine();
				continue;
			}
		}
		return request;
	}
}
