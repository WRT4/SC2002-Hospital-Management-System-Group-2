package model;

import java.time.LocalDate;
import java.time.LocalTime;

import enums.Status;

public class RefillRequest implements Request {
    private Pharmacist pharmacist;
    private Administrator admin;
    private String medication;
    private int requestedAmount;
    private Status status;  // Updated to use Status enum
    private LocalDate requestDate;
    private LocalTime requestTime;
    private static int count = 0;
    private int reqID;
    

    public RefillRequest(String medication, int requestedAmount, Pharmacist p, Administrator a) {
    	this.admin = a;
    	this.pharmacist = p;
        this.medication = medication;
        this.requestedAmount = requestedAmount;
        this.status = Status.PENDING;  
        this.requestDate = LocalDate.now();
        this.requestTime = LocalTime.now();
        this.reqID = ++count;
    }
    

    public String getMedication() { return medication; }
    public Administrator getAdmin() {return this.admin;}
    public Pharmacist getPharmacist() {return this.pharmacist;}
    public int getRequestedAmount() { return requestedAmount; }
    public Status getStatus() { return status; }
    public LocalDate getRequestDate() { return requestDate; }
    public LocalTime getRequestTime() { return requestTime; }

    @Override
    public void acceptRequest() {
        this.status = Status.COMPLETED;
        System.out.println("Refill request approved for " + medication);
    }

    @Override
    public void declineRequest() {
        this.status = Status.DECLINED;
        System.out.println("Refill request declined for " + medication);
    }
    

    @Override
    public String toString() {
        return "Request ID:" + reqID +
        		" Medication: " + medication + 
               ", Requested Amount: " + requestedAmount + 
               ", Status: " + status + 
               ", Requested on: " + requestDate + " at " + requestTime;
    }
}

