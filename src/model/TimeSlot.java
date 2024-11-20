package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a time slot within a schedule in the Hospital Management System (HMS).
 * A time slot is defined by a date, start time, end time, and an occupied status.
 * Provides methods for managing and checking the state of the time slot.
 * @author Hoo Jing Huan, Tan Wen Rong
 * @version 1.0
 * @since 2024-11-18
 */
public class TimeSlot implements Serializable {

    private static final long serialVersionUID = -8117177383039659L;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean isOccupied;

    /**
     * Constructs a TimeSlot with a specified date, start time, and end time.
     * The end time is automatically set to 30 minutes after the start time, and the slot is initially unoccupied.
     *
     * @param date      The date of the time slot
     * @param startTime The start time of the time slot
     * @param endTime   The end time of the time slot (set automatically to 30 minutes after the start time)
     */
    public TimeSlot(LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = startTime.plusMinutes(30);
        this.isOccupied = false;
    }

    /**
     * Retrieves the date of the time slot.
     *
     * @return The date of the time slot
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Retrieves the start time of the time slot.
     *
     * @return The start time of the time slot
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Retrieves the end time of the time slot.
     *
     * @return The end time of the time slot
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Checks whether the time slot is occupied.
     *
     * @return true if the time slot is occupied, false otherwise
     */
    public boolean getOccupied() {
        return isOccupied;
    }

    /**
     * Frees up the time slot, setting its status to unoccupied.
     * Prints a message indicating that the slot is now available.
     */
    public void freeSlot() {
        isOccupied = false;
        System.out.println("Time Slot <<" + toString() + " >> is emptied.");
    }

    /**
     * Provides a string representation of the time slot, including the date and time range.
     *
     * @return A string representing the time slot
     */
    public String toString() {
        return "Date: " + date + ",Time: " + startTime + "-" + endTime;
    }

    /**
     * Prints the time range of the time slot to the console.
     */
    public void printTime() {
        System.out.println(startTime + "-" + endTime);
    }

    /**
     * Sets a new start time for the time slot and automatically adjusts the end time to 15 minutes later.
     *
     * @param time The new start time
     */
    public void setStartTime(LocalTime time) {
        this.startTime = time;
        this.endTime = time.plusMinutes(15);
    }

    /**
     * Marks the time slot as occupied.
     */
    public void setOccupied() {
        this.isOccupied = true;
    }

    /**
     * Frees the time slot, marking it as unoccupied.
     */
    public void free() {
        this.isOccupied = false;
    }
}
