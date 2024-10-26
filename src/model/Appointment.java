package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.time.temporal.ChronoUnit;

public class Appointment {
    private Patient patient;
    private Doctor doctor;
    private LocalDate date;
    private TimeSlot timeslot;
    private Status status;
    private Status prescriptionStatus;
    private int appointmentID;
    private static int count;
    private String prescription;
    private String diagnosis;
    private String serviceType;
    private String note;
    
    public void setServiceType (String s) {
    	this.serviceType = s;
    }
    
    public String getServiceType() {
    	return this.serviceType;
    }
    
    public Appointment(LocalDate date, TimeSlot time, Doctor doctor, Patient patient) {
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.timeslot = time;
        this.timeslot.setOccupied();
        this.status = Status.PENDING;
        count++;
        this.appointmentID = count;
    }
    
    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TimeSlot getTimeSlot(){
        return this.timeslot;
    }

    public void setTimeSlot(LocalTime time) {
        this.timeslot.setStartTime(time);
    }

    public Status getStatus(){
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getPrescriptionStatus(){
        return this.prescriptionStatus;
    }

    public void setPrescriptionStatus(Status prescriptionStatus) {
        this.prescriptionStatus = prescriptionStatus;
    }

    //to confirm with "Schedule" entity
    public void printScheduledAppointment(){
    	System.out.println("Appointment " + appointmentID + ": " + date + " at " + timeslot.getStartTime() + " with Doctor " + doctor.getName());//getName for doctor
        System.out.println("Status: " + status);
        System.out.println();
    }

    //print Appointment Outcome Record (for COMPLETED appointments)
    public void printAppointmentOutcome(){
        System.out.println("Appointment " + appointmentID + ": " + date + " at " + timeslot.getStartTime() + " with Doctor " + doctor.getName());//add outcomes
        System.out.println("Service Type: " + serviceType);
        System.out.println("Prescription: " + prescription + " Status :" + prescriptionStatus);
        System.out.println("Consultation notes: " + note);
        System.out.println();
    }
    
    public void printCancelledAppointments() {
    	System.out.println("Appointment " + appointmentID + ": " + date + " at " + timeslot.getStartTime() + " with Doctor " + doctor.getName() + " CANCELLED.");
    }

	public int getAppointmentID() {
		// TODO Auto-generated method stub
		return this.appointmentID;
	}

	public String getPrescription() {
		return this.prescription;
	}
	
	public void setPrescription(String prescription) {
		this.prescription = prescription;
	}

	public Doctor getDoctor() {
		return doctor;	
	}


	public Patient getPatient() {
		return patient;
	}
	
	public String toString() {
		return "Appointment " + appointmentID + ": " + date + " at " + timeslot.getStartTime() + " Patient: " + patient.getName() + " with Doctor: " + doctor.getName();
	}
	
	public void setDiagnosis(String d) {
		diagnosis = d;
	}
	
	public String getDiagnosis() {
		return diagnosis;
	}

	public void setNotes(String note) {
		this.note = note;
	}
	
	public String getNotes() {
		return note;
	}

}
