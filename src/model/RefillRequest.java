import java.time.LocalDate;
import java.time.LocalTime;

public class RefillRequest {
    private String medication;
    private int requestedAmount;
    private String status;  // "pending", "approved", "rejected"
    private LocalDate requestDate;
    private LocalTime requestTime;

    // Constructor
    public RefillRequest(String medication, int requestedAmount) {
        this.medication = medication;
        this.requestedAmount = requestedAmount;
        this.status = "pending";  // Default status is "pending" when created
        this.requestDate = LocalDate.now();  // Set current date
        this.requestTime = LocalTime.now();  // Set current time
    }

    // Getters
    public String getMedication() {
        return medication;
    }

    public int getRequestedAmount() {
        return requestedAmount;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public LocalTime getRequestTime() {
        return requestTime;
    }

    // Approve the request
    public void approve() {
        this.status = "approved";
    }

    // Reject the request
    public void reject() {
        this.status = "rejected";
    }

    // ToString method to display request details
    @Override
    public String toString() {
        return "Medication: " + medication + 
               ", Requested Amount: " + requestedAmount + 
               ", Status: " + status + 
               ", Requested on: " + requestDate + " at " + requestTime;
    }
}
