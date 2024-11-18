package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import enums.Status;

public class AppointmentRequest implements Request {

	private static final long serialVersionUID = 9181140749468462052L;
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

	public String toString() {
		return "Request ID: " + requestID + " Patient Name: " + patient.getName() + ", Doctor Name: " + doctor.getName() + " "+ timeslot.toString() + " Status: " + status;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public int getRequestID() {
		return requestID;
	}

	public Patient getPatient(){
		return this.patient;
	}

	public LocalDate getDate(){
		return this.date;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public TimeSlot getTimeSlot() {
		return timeslot;
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
				if (request == null) throw new RuntimeException("RequestID does not exist! ");
				if (request.getStatus() != Status.PENDING) {
					if (request.getStatus() == Status.CANCELLED) {
						throw new RuntimeException("Request already cancelled!");
					}
					else if (request.getStatus() == Status.ACCEPTED) {
						if (isPatient) throw new RuntimeException("Request already accepted! Cancel appointment instead!");
						throw new RuntimeException("Request already accepted!");
					}
					else if (request.getStatus() == Status.DECLINED) {
						throw new RuntimeException("Request already declined!");
					}
				}
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
		return request;
	}

}
