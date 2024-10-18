package model;

public class Appointment {
    private String patientID;
    private String doctorID;
    private String date;
    private String time;
    private String status;
    private String prescriptionStatus;

    public Appointment(String patientID, String doctorID, String date, String time) {
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.date = date;
        this.time = time;
        this.status = "PENDING";
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime(){
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus(){
        return this.status;
    }

    public void setStatus(String status) {
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
        System.out.println("Appointment: " + date + " at " + time +
                "\n with Doctor: " + doctorID);//getName for doctor
    }

    //print Appointment Outcome Record (for COMPLETED appointments)
    public void printAppointmentOutcome(int appointmentNum){
        System.out.println("Appointment " + appointmentNum + ": " + date + " at " + time + " with Doctor " + ); //getDoctorName
    }

}