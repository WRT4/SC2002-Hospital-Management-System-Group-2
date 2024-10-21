package model;

public class Appointment {
    private Patient patient;
    private Doctor doctor;
    private String date;
    private String time;
    private String status;
    private String prescriptionStatus;
    private String AppointmenID;
    private static int count;
    private String prescription;

    public Appointment(Patient patient, Doctor doctor, String date, String time) {
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.time = time;
        this.status = "PENDING";
        count++;
        this.AppointmenID = "" + count;
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
                "\n with Doctor: " + doctor.getName() );//getName for doctor
        System.out.println("Status: " + status);
    }

    //print Appointment Outcome Record (for COMPLETED appointments)
    public void printAppointmentOutcome(int appointmentNum){
        System.out.println("Appointment " + appointmentNum + ": " + date + " at " + time + " with Doctor " + doctor.getName()); //getDoctorName
    }

	public String getAppointmentID() {
		// TODO Auto-generated method stub
		return this.AppointmenID;
	}

	public String getPrescription() {
		return this.prescription;
	}
	
	public void setPrescription(String prescription) {
		this.prescription = prescription;
	}
}
