package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Doctor extends User {

    private Schedule schedule;
    private ArrayList<AppointmentRequest> requests;

    public Doctor(String id, String name) {
        super(id, name, "Doctor");
        this.schedule = new Schedule(id);
        this.requests = new ArrayList<>();
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public ArrayList<AppointmentRequest> getRequests() {
        return requests;
    }

    public void addRequest(AppointmentRequest request) {
        requests.add(request);
    }

    public void removeAppointment(Appointment appointment) {
        schedule.getAppointments().remove(appointment);
    }

    public TimeSlot findTimeSlot(LocalDate date, LocalTime time) {
        return this.schedule.findTimeSlot(date, time);
    }

    public Appointment findAppointment(int id) {
        for (Appointment apt : schedule.getAppointments()) {
            if (apt.getAppointmentID() == id) {
                return apt;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Doctor ID: " + getId() + " Name: " + getName();
    }
}

