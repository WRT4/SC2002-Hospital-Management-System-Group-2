package model;

import java.time.LocalDate;
import java.time.LocalTime;

import application.Database;
import enums.Status;

/**
 * Represents a refill request for a specific medication.
 * Tracks details such as the medication name, requested amount, request date and time,
 * and the associated pharmacist and administrator. Provides methods to approve or decline the request.
 * @author Hoo Jing Huan, Lee Kuan Rong, Lim Wee Keat, Tan Wen Rong, Yeoh Kai Wen
 * @version 1.0
 * @since 2024-11-18
 */
public class RefillRequest implements Request {

    private static final long serialVersionUID = 4779573415288173697L;
    private Pharmacist pharmacist;
    private Administrator admin;
    private String medication;
    private int requestedAmount;
    private Status status;
    private LocalDate requestDate;
    private LocalTime requestTime;
    private int reqID;

    /**
     * Constructs a {@code RefillRequest} with the specified details.
     * The request status is initialized to {@code PENDING}, and the request date and time are set to the current date and time.
     *
     * @param medication       The name of the medication being requested.
     * @param requestedAmount  The amount of medication requested.
     * @param p                The {@code Pharmacist} handling the request.
     * @param a                The {@code Administrator} overseeing the request.
     */
    public RefillRequest(String medication, int requestedAmount, Pharmacist p, Administrator a) {
        this.admin = a;
        this.pharmacist = p;
        this.medication = medication;
        this.requestedAmount = requestedAmount;
        this.status = Status.PENDING;
        this.requestDate = LocalDate.now();
        this.requestTime = LocalTime.now();
        this.reqID = ++Database.refillRequestCount;
    }

    /**
     * Retrieves the name of the medication being requested.
     *
     * @return The medication name.
     */
    public String getMedication() {
        return medication;
    }

    /**
     * Retrieves the administrator associated with the request.
     *
     * @return The {@code Administrator}.
     */
    public Administrator getAdmin() {
        return this.admin;
    }

    /**
     * Retrieves the pharmacist handling the request.
     *
     * @return The {@code Pharmacist}.
     */
    public Pharmacist getPharmacist() {
        return this.pharmacist;
    }

    /**
     * Retrieves the amount of medication requested.
     *
     * @return The requested amount.
     */
    public int getRequestedAmount() {
        return requestedAmount;
    }

    /**
     * Retrieves the current status of the request.
     *
     * @return The {@code Status} of the request.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Retrieves the date when the request was made.
     *
     * @return The {@code LocalDate} of the request.
     */
    public LocalDate getRequestDate() {
        return requestDate;
    }

    /**
     * Retrieves the time when the request was made.
     *
     * @return The {@code LocalTime} of the request.
     */
    public LocalTime getRequestTime() {
        return requestTime;
    }

    /**
     * Approves the refill request by setting its status to {@code APPROVED}.
     * Prints a confirmation message to the console.
     */
    @Override
    public void acceptRequest() {
        this.status = Status.APPROVED;
        System.out.println("Refill request approved for " + medication);
    }

    /**
     * Declines the refill request by setting its status to {@code DECLINED}.
     * Prints a confirmation message to the console.
     */
    @Override
    public void declineRequest() {
        this.status = Status.DECLINED;
        System.out.println("Refill request declined for " + medication);
    }

    /**
     * Returns a string representation of the refill request.
     * Includes the request ID, medication name, requested amount, status, and the date and time of the request.
     *
     * @return A string representation of the request.
     */
    @Override
    public String toString() {
        return "Request ID: " + reqID +
               " Medication: " + medication + 
               ", Requested Amount: " + requestedAmount + 
               ", Status: " + status + 
               ", Requested on: " + requestDate + " at " + requestTime;
    }
}
