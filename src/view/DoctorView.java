package view;

import model.*;

import java.time.LocalDate;
import java.util.List;

public class DoctorView extends UserView {

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displaySchedule(Schedule schedule, LocalDate date) {
        System.out.println("Schedule for Date: " + date);
        schedule.viewAllSlots(date);
    }

    public void displayAppointment(Appointment appointment) {
        System.out.println("Appointment: " + appointment);
    }

    public void displayMedicalRecord(MedicalRecord record) {
        System.out.println("Patient Medical Record:");
        record.printMedicalRecord();
    }

    public void displayAppointmentRequests(List<AppointmentRequest> requests) {
        System.out.println("Pending Appointment Requests:");
        for (AppointmentRequest request : requests) {
            if (request.getStatus() == Status.PENDING) {
                System.out.println(request);
            }
        }
    }
}
