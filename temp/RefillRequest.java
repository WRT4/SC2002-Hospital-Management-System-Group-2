package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class RefillRequest implements Request {
    private String medication;
    private int requestedAmount;
    private Status status;  // Updated to use Status enum
    private LocalDate requestDate;
    private LocalTime requestTime;

    public RefillRequest(String medication, int requestedAmount) {
        this.medication = medication;
        this.requestedAmount = requestedAmount;
        this.status = Status.PENDING;  // Default to PENDING
        this.requestDate = LocalDate.now();
        this.requestTime = LocalTime.now();
    }

    public String getMedication() { return medication; }
    public int getRequestedAmount() { return requestedAmount; }
    public Status getStatus() { return status; }
    public LocalDate getRequestDate() { return requestDate; }
    public LocalTime getRequestTime() { return requestTime; }

    @Override
    public void acceptRequest() {
        this.status = Status.ACCEPTED;
        System.out.println("Refill request approved for " + medication);
    }

    @Override
    public void declineRequest() {
        this.status = Status.DECLINED;
        System.out.println("Refill request declined for " + medication);
    }

    @Override
    public String toString() {
        return "Medication: " + medication + 
               ", Requested Amount: " + requestedAmount + 
               ", Status: " + status + 
               ", Requested on: " + requestDate + " at " + requestTime;
    }
}
