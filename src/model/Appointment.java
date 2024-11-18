package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import application.Database;
import enums.Status;

/**
 * Represents a request for an appointment in the Hospital Management System (HMS).
 * It includes information about the appointment, the doctor, patient, timeslot, status
 * prescription status, diagnosis, service type and note.
 * @author Hoo Jing Huan, Lee Kuan Rong, Lim Wee Keat, Tan Wen Rong, Yeoh Kai Wen
 * @version 1.0
 * @since 2024-11-18
 */

public class Appointment implements Serializable{

	private static final long serialVersionUID = 2168626928105170600L;
	private Patient patient;
    private Doctor doctor;
    private LocalDate date;
    private TimeSlot timeslot;
    private Status status;
    private Status prescriptionStatus = Status.PENDING;
    private int appointmentID;
    private ArrayList<Medication> prescriptions = new ArrayList<>();
    private String diagnosis;
    private String serviceType;
    private String note;

    /**
     * Constructs an Appointment object with a given date, time slot, doctor, and patient.
     * The appointment status is set to PENDING by default, and the timeslot is marked as occupied.
     *
     * @param date   The date of the appointment
     * @param time   The time slot of the appointment
     * @param doctor The doctor assigned to the appointment
     * @param patient The patient scheduled for the appointment
     */
    public Appointment(LocalDate date, TimeSlot time, Doctor doctor, Patient patient) {
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.timeslot = time;
        this.timeslot.setOccupied();
        this.status = Status.PENDING;
        this.appointmentID = ++Database.appointmentCount;
    }

    /**
     * Changes the service type of this appointment.
     *
     * @param s The new type of service provided
     */
    public void setServiceType(String s) {
        this.serviceType = s;
    }

    /**
     * Gets the service type of the appointment.
     *
     * @return The service type of this appointment
     */
    public String getServiceType() {
        return this.serviceType;
    }

    /**
     * Gets the date of this appointment.
     *
     * @return The date of this appointment
     */
    public LocalDate getDate() {
        return this.date;
    }

    /**
     * Sets a new date for this appointment.
     *
     * @param date The new date for the appointment
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Gets the timeslot of this appointment.
     *
     * @return The TimeSlot object representing the appointment's time slot
     */
    public TimeSlot getTimeSlot() {
        return this.timeslot;
    }

    /**
     * Sets a new start time for the timeslot of this appointment.
     *
     * @param time The new start time for the timeslot
     */
    public void setTimeSlot(LocalTime time) {
        this.timeslot.setStartTime(time);
    }

    /**
     * Gets the current status of this appointment.
     *
     * @return The current status of the appointment
     */
    public Status getStatus() {
        return this.status;
    }

    /**
     * Updates the status of this appointment.
     *
     * @param status The new status for the appointment
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Gets the prescription status of this appointment.
     *
     * @return The prescription status of the appointment
     */
    public Status getPrescriptionStatus() {
        return this.prescriptionStatus;
    }

    /**
     * Sets the prescription status of this appointment.
     *
     * @param prescriptionStatus The new prescription status
     */
    public void setPrescriptionStatus(Status prescriptionStatus) {
        this.prescriptionStatus = prescriptionStatus;
    }

    /**
     * Gets the unique appointment ID.
     *
     * @return The appointment ID
     */
    public int getAppointmentID() {
        return this.appointmentID;
    }

    /**
     * Retrieves the list of medications prescribed in this appointment.
     * If there are no prescriptions, a message is displayed.
     *
     * @return The list of prescribed medications or null if no prescriptions
     */
    public ArrayList<Medication> getPrescriptions() {
        if (this.prescriptions == null) {
            System.out.println("No prescriptions yet.");
            return null;
        }
        else
            return this.prescriptions;
    }

    /**
     * Gets the doctor assigned to this appointment.
     *
     * @return The doctor assigned to the appointment
     */
    public Doctor getDoctor() {
        return doctor;
    }

    /**
     * Gets the patient associated with this appointment.
     *
     * @return The patient for this appointment
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * Provides a string representation of the appointment, including appointment ID,
     * date, start time, patient name and doctor name.
     *
     * @return A string containing details of the appointment
     */
    public String toString() {
        return "Appointment " + appointmentID + ": " + date + " at " + timeslot.getStartTime() + " Patient: " + patient.getName() + " with Doctor: " + doctor.getName();
    }

    /**
     * Sets the diagnosis for this appointment.
     *
     * @param d The diagnosis for the appointment
     */
    public void setDiagnosis(String d) {
        diagnosis = d;
    }

    /**
     * Gets the diagnosis for this appointment.
     *
     * @return The diagnosis associated with this appointment
     */
    public String getDiagnosis() {
        return diagnosis;
    }

    /**
     * Sets additional notes for this appointment.
     *
     * @param note The notes to add to the appointment
     */
    public void setNotes(String note) {
        this.note = note;
    }

    /**
     * Gets the notes for this appointment.
     *
     * @return The notes associated with this appointment
     */
    public String getNotes() {
        return note;
    }

    /**
     * Provides a summary of appointment details.
     *
     * @return A string containing the appointment details
     */
    public String getDetails() {
        return "Appointment ID: " + this.appointmentID + ", Date: " + this.date +
                ", Time: " + this.timeslot.getStartTime() + ", Doctor: " + this.doctor.getName() +
                ", Patient: " + this.patient.getName() + ", Status: " + this.status;
    }

    /**
     * Finds an appointment by ID from a list of appointments, and optionally filters out completed appointments.
     * Throws an exception if the appointment is cancelled or completed based on conditions.
     *
     * @param appointments A list of appointments to search
     * @param noConfirmed  If true, completed appointments are not returned
     * @param id           The ID of the appointment to find
     * @return The appointment found or throws a RuntimeException if not found or invalid
     * @throws RuntimeException If the appointment is not found, cancelled, or completed based on conditions
     */
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
