package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import controller.DoctorController;
import controller.SessionController;

/**
 * Represents a doctor in the Hospital Management System (HMS).
 * Manages doctor-specific attributes, such as schedule, appointment requests, and patient counters.
 * Provides functionality to handle appointments and their related actions.
 * @author Hoo Jing Huan, Lee Kuan Rong, Lim Wee Keat, Tan Wen Rong, Yeoh Kai Wen
 * @version 1.0
 * @since 2024-11-18
 */
public class Doctor extends User {

    private static final long serialVersionUID = -1805621604075705717L;
    private Schedule schedule;
    private ArrayList<AppointmentRequest> requests;
    private HashMap<Patient, Integer> appointmentCounter;
    private String gender;
    private int age;

    /**
     * Constructs a {@code Doctor} with the specified ID, name, gender, and age.
     * Initializes the schedule, appointment requests list, and patient appointment counter.
     *
     * @param id     The unique ID of the doctor.
     * @param name   The name of the doctor.
     * @param gender The gender of the doctor.
     * @param age    The age of the doctor.
     */
    public Doctor(String id, String name, String gender, int age) {
        super(id, name, "password", "Doctor");
        this.gender = gender;
        this.age = age;
        this.requests = new ArrayList<>();
        this.appointmentCounter = new HashMap<>();
        this.schedule = new Schedule(id);
    }

    /**
     * Constructs a {@code Doctor} with the specified ID, name, and password.
     * Initializes the schedule, appointment requests list, and patient appointment counter.
     *
     * @param id       The unique ID of the doctor.
     * @param name     The name of the doctor.
     * @param password The password for the doctor.
     */
    public Doctor(String id, String name, String password) {
        super(id, name, password, "Doctor");
        this.schedule = new Schedule(id);
        this.requests = new ArrayList<>();
        this.appointmentCounter = new HashMap<>();
    }

    /**
     * Constructs a {@code Doctor} with the specified ID and name.
     * Password defaults to "password".
     *
     * @param id   The unique ID of the doctor.
     * @param name The name of the doctor.
     */
    public Doctor(String id, String name) {
        this(id, name, "password");
    }

    /**
     * Returns a string representation of the doctor, including their ID and name.
     *
     * @return A string representation of the doctor.
     */
    @Override
    public String toString() {
        return "Doctor ID: " + getID() + " Name: " + getName();
    }

    /**
     * Retrieves the schedule of the doctor.
     *
     * @return The {@code Schedule} object for the doctor.
     */
    public Schedule getSchedule() {
        return schedule;
    }

    /**
     * Retrieves the gender of the doctor.
     *
     * @return The gender of the doctor.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Retrieves the age of the doctor.
     *
     * @return The age of the doctor.
     */
    public int getAge() {
        return age;
    }

    /**
     * Retrieves the appointment counter mapping patients to their appointment counts.
     *
     * @return A {@code HashMap} of patients and their appointment counts.
     */
    public HashMap<Patient, Integer> getAppointmentCounter() {
        return appointmentCounter;
    }

    /**
     * Adds a new appointment request to the doctor's list of requests.
     *
     * @param request The {@code AppointmentRequest} to add.
     */
    public void addRequest(AppointmentRequest request) {
        requests.add(request);
    }

    /**
     * Finds an appointment by its ID in the doctor's schedule.
     *
     * @param id The unique ID of the appointment.
     * @return The {@code Appointment} object if found, or {@code null} otherwise.
     */
    public Appointment findAppointment(int id) {
        for (Appointment apt : schedule.getAppointments()) {
            if (apt.getAppointmentID() == id) {
                return apt;
            }
        }
        return null;
    }

    /**
     * Removes an appointment from the doctor's schedule.
     *
     * @param appointment The {@code Appointment} to remove.
     */
    public void removeAppointment(Appointment appointment) {
        schedule.getAppointments().remove(appointment);
    }

    /**
     * Finds a timeslot in the doctor's schedule by date and time.
     *
     * @param date The date of the timeslot.
     * @param time The time of the timeslot.
     * @return The {@code TimeSlot} object if found, or {@code null} otherwise.
     */
    public TimeSlot findTimeSlot(LocalDate date, LocalTime time) {
        TimeSlot timeslot = this.schedule.findTimeSlot(date, time);
        if (timeslot == null) {
            System.out.println("TimeSlot not found!");
            return null;
        }
        return timeslot;
    }

    /**
     * Retrieves the list of appointment requests associated with the doctor.
     *
     * @return An {@code ArrayList} of {@code AppointmentRequest} objects.
     */
    public ArrayList<AppointmentRequest> getRequests() {
        return requests;
    }

    /**
     * Increments the appointment counter for a specified patient.
     *
     * @param patient The {@code Patient} whose counter to increment.
     */
    public void addAppointmentCounter(Patient patient) {
        appointmentCounter.put(patient, appointmentCounter.getOrDefault(patient, 0) + 1);
    }

    /**
     * Decrements the appointment counter for a specified patient.
     * If the count reaches zero, the patient is removed from the counter.
     *
     * @param patient The {@code Patient} whose counter to decrement.
     */
    public void subtractAppointmentCounter(Patient patient) {
        int currentCount = appointmentCounter.get(patient);
        if (currentCount > 1) {
            appointmentCounter.put(patient, currentCount - 1);
        } else {
            appointmentCounter.remove(patient);
        }
    }

    /**
     * Creates a {@code SessionController} for the doctor.
     * Accesses the doctor's dashboard.
     *
     * @param scanner A {@code Scanner} for user input.
     * @return A new {@code DoctorController} instance for the doctor.
     */
    @Override
    public SessionController createController(Scanner scanner) {
        System.out.println("Accessing Doctor Dashboard...");
        return new DoctorController(this, scanner);
    }
}
