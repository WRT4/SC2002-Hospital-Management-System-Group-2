package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.time.temporal.ChronoUnit;

/**
 * Represents a doctor's schedule, including working time slots and appointments.
 * Provides functionality for managing appointments and maintaining availability for up to 30 days in advance.
 * @author Hoo Jing Huan, Tan Wen Rong
 * @version 1.0
 * @since 2024-11-18
 */
public class Schedule implements Serializable {

	private static final long serialVersionUID = -9056252775996453213L;
	private String doctorID;
	private ArrayList<Appointment> appointments;
	private ArrayList<TimeSlot> workingSlots;

	/**
	 * Constructs a Schedule for a specific doctor by initializing working slots for 30 days and creating an empty appointment list.
	 *
	 * @param doctorID The unique ID of the doctor for this schedule
	 */
	public Schedule(String doctorID) {
		this.doctorID = doctorID;
		workingSlots = new ArrayList<>();
		createWorkingSlots30Days();
		appointments = new ArrayList<>();
	}

	/**
	 * Adds working time slots starting from a given date for a specified number of days.
	 *
	 * @param startDate The start date for adding working slots
	 * @param dayNum    The number of days for which to add working slots
	 */
	public void addWorkingSlots(LocalDate startDate, int dayNum) {
		LocalDate day;
		for (int i = 0; i <= dayNum; i++) {
			day = startDate.plusDays(i);
			LocalTime temp = LocalTime.of(8, 0);
			while (temp.isBefore(LocalTime.of(17, 0))) {
				workingSlots.add(new TimeSlot(day, temp, temp.plusMinutes(30)));
				temp = temp.plusMinutes(30);
			}
		}
	}

	/**
	 * Creates working slots for a period of 30 days starting from the current date.
	 */
	public void createWorkingSlots30Days() {
		LocalDate today = LocalDate.now();
		LocalDate lastDay = today.plusDays(30);
		System.out.println("Creating Working Schedule from " + today.toString() + " to " + lastDay.toString());
		addWorkingSlots(today, 30);
	}

	/**
	 * Retrieves the list of working time slots for the schedule.
	 *
	 * @return An ArrayList of working TimeSlot objects
	 */
	public ArrayList<TimeSlot> getWorkingSlots() {
		return workingSlots;
	}

	/**
	 * Checks if the schedule is filled for a 30-day period in advance.
	 *
	 * @return true if the schedule is filled for 30 days, false otherwise
	 */
	public boolean checkFilledSchedule() {
		TimeSlot lastDay = workingSlots.get(workingSlots.size() - 1);
		return ChronoUnit.DAYS.between(LocalDate.now(), lastDay.getDate()) >= 30;
	}

	/**
	 * Fills the schedule with working slots for up to 30 days if the schedule is not already filled.
	 */
	public void fillAdvancedSchedule() {
		if (!checkFilledSchedule()) {
			TimeSlot lastDay = workingSlots.get(workingSlots.size() - 1);
			int numDays = (int) ChronoUnit.DAYS.between(lastDay.getDate().plusDays(1), LocalDate.now().plusDays(30));
			addWorkingSlots(lastDay.getDate().plusDays(1), numDays);
		}
	}

	/**
	 * Retrieves the list of appointments in the schedule.
	 *
	 * @return An ArrayList of Appointment objects
	 */
	public ArrayList<Appointment> getAppointments() {
		return appointments;
	}

	/**
	 * Adds an appointment to the schedule.
	 *
	 * @param appointment The appointment to add
	 */
	public void addAppointment(Appointment appointment) {
		appointments.add(appointment);
		System.out.println("Appointment added: " + appointment);
	}

	/**
	 * Removes an appointment from the schedule based on its appointment ID.
	 *
	 * @param appointmentId The ID of the appointment to remove
	 */
	public void removeAppointment(int appointmentId) {
		appointments.removeIf(appt -> appt.getAppointmentID() == appointmentId);
		System.out.println("Appointment " + appointmentId + " removed");
	}

	/**
	 * Finds a specific time slot in the working schedule based on a given date and time.
	 *
	 * @param date The date of the desired time slot
	 * @param time The start time of the desired time slot
	 * @return The matching TimeSlot object if found, otherwise null
	 */
	public TimeSlot findTimeSlot(LocalDate date, LocalTime time) {
		for (TimeSlot timeslot : workingSlots) {
			if (time.equals(timeslot.getStartTime()) && date.equals(timeslot.getDate())) {
				return timeslot;
			}
		}
		return null;
	}

	/**
	 * Frees up a specified time slot in the schedule, making it available.
	 *
	 * @param timeslot The time slot to free
	 */
	public void setAvailability(TimeSlot timeslot) {
		timeslot.free();
	}

	/**
	 * Checks if a given time slot is occupied.
	 *
	 * @param requestSlot The time slot to check
	 * @return true if the time slot is occupied, false otherwise
	 */
	public boolean checkOverlapping(TimeSlot requestSlot) {
		return requestSlot.getOccupied();
	}

	/**
	 * Finds an appointment in the schedule that matches a given time slot.
	 *
	 * @param timeslot The time slot to search for
	 * @return The matching Appointment if found, otherwise null
	 */
	public Appointment findAppointment(TimeSlot timeslot) {
		for (Appointment appointment : appointments) {
			if (appointment.getTimeSlot().equals(timeslot)) {
				return appointment;
			}
		}
		return null;
	}

	/**
	 * Retrieves the doctor ID associated with this schedule.
	 *
	 * @return The doctor ID
	 */
	public String getDoctorID() {
		return doctorID;
	}
}
