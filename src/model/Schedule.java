package model;

import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private int doctorId;
    private List<Appointment> appointments;

    public Schedule(int doctorId) {
        this.doctorId = doctorId;
        this.appointments = new ArrayList<>();
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        System.out.println("Appointment added: " + appointment);
    }

    public void removeAppointment(int appointmentId) {
        appointments.removeIf(appt -> appt.getId() == appointmentId);
        System.out.println("Appointment " + appointmentId + " removed");
    }

    public void viewSchedule() {
        System.out.println("Schedule for Doctor ID: " + doctorId);
        for (Appointment appt : appointments) {
            System.out.println(appt);
        }
    }

    public void setAvailability(String date, String time) {
        Appointment availability = new Appointment(-1, date, time, -1); // -1 indicates availability slot
        appointments.add(availability);
        System.out.println("Availability set for " + date + " at " + time);
    }

   /* public static void main(String[] args) {
        Schedule doctorSchedule = new Schedule(1);
        doctorSchedule.setAvailability("2024-10-20", "10:00");
        doctorSchedule.addAppointment(new Appointment(1, "2024-10-20", "10:00", 101));
        doctorSchedule.viewSchedule();
        doctorSchedule.removeAppointment(1);
        doctorSchedule.viewSchedule();
    }
}*/