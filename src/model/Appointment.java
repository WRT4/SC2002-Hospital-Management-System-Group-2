package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import enums.Status;

public class Appointment implements Serializable{

	private static final long serialVersionUID = 2168626928105170600L;
	private Patient patient;
    private Doctor doctor;
    private LocalDate date;
    private TimeSlot timeslot;
    private Status status;
    private Status prescriptionStatus = Status.PENDING;
    private int appointmentID;
    private static int count;
    private ArrayList<Medication> prescriptions = new ArrayList<>();
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

	public int getAppointmentID() {
		return this.appointmentID;
	}

    public ArrayList<Medication> getPrescriptions() {
        if(this.prescriptions == null) {
            System.out.println("No prescriptions yet.");
            return null;
        }
        else
            return this.prescriptions;
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
	
	public String getDetails() {
	    return "Appointment ID: " + this.appointmentID + ", Date: " + this.date + 
	           ", Time: " + this.timeslot.getStartTime() + ", Doctor: " + this.doctor.getName() + 
	           ", Patient: " + this.patient.getName() + ", Status: " + this.status;
	}
	
	public static Appointment findAppointment(ArrayList<Appointment> appointments, boolean noConfirmed, int id) throws RuntimeException {
	    Appointment apt = null;
	    for (Appointment appointment : appointments) {
	        if (appointment.getAppointmentID() == id) {
	            apt = appointment;
	            break;
	        }
	    }
	    
	    if (apt == null) {
	        throw new RuntimeException("Appointment ID does not exist!");
	    }
	    if (apt.getStatus() == Status.CANCELLED) {
	        throw new RuntimeException("Appointment has been cancelled!");
	    }
	    if (noConfirmed && apt.getStatus() == Status.COMPLETED) {
	        throw new RuntimeException("Appointment has been completed!");
	    }
	    
	    return apt;
	}

}
