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
    private String prescriptionStatus;
    private int AppointmentID;
    private static int count;
    private String prescription;
    
    public Appointment(LocalDate date, TimeSlot time, Doctor doctor, Patient patient) {
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.timeslot = time;
        this.timeslot.setOccupied();
        this.status = Status.PENDING;
        count++;
        this.AppointmentID = count;
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

    public String getPrescriptionStatus(){
        return this.prescriptionStatus;
    }

    public void setPrescriptionStatus(String prescriptionStatus) {
        this.prescriptionStatus = prescriptionStatus;
    }

    //to confirm with "Schedule" entity
    public void printScheduledAppointment(){
        System.out.println("Appointment: " + date + " at " + timeslot.getStartTime() + "\n with Doctor: " + doctor.getName() );//getName for doctor
        System.out.println("Status: " + status);
        System.out.println();
    }

    //print Appointment Outcome Record (for COMPLETED appointments)
    public void printAppointmentOutcome(){
        System.out.println("Appointment " + AppointmentID + ": " + date + " at " + timeslot.getStartTime() + " with Doctor " + doctor.getName()); //add outcomes
    }
    
    public void printCancelledAppointments() {
    	System.out.println("Appointment " + AppointmentID + ": " + date + " at " + timeslot.getStartTime() + " with Doctor " + doctor.getName() + " CANCELLED.");
    }

	public int getAppointmentID() {
		// TODO Auto-generated method stub
		return this.AppointmentID;
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
		return "Appointment " + AppointmentID + ": " + date + " at " + timeslot.getStartTime() + " Patient: " + patient.getName() + " with Doctor: " + doctor.getName();
	}

}
