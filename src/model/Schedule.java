package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.time.temporal.ChronoUnit;

//schedule has appointments and workDays()
public class Schedule implements Serializable{
	
	private static final long serialVersionUID = -9056252775996453213L;
	private String doctorID;
    private ArrayList<Appointment> appointments;
    private ArrayList<TimeSlot> workingSlots;


    public Schedule(String doctorID){
		this.doctorID = doctorID;
        //create working slots for 2 weeks timeframe
		workingSlots = new ArrayList<TimeSlot>();
		createWorkingSlots30Days();
        appointments = new ArrayList<Appointment>();
    }

	public void addWorkingSlots(LocalDate startDate, int dayNum){
		LocalDate day;
		for (int i=0; i <=dayNum; i++){
			day = startDate.plusDays(i);
			LocalTime temp = LocalTime.of(8,0);
			while (temp.isBefore(LocalTime.of(17,0))){
				workingSlots.add(new TimeSlot(day, temp, temp.plusMinutes(30)));
				temp = temp.plusMinutes(30);
			}
		}
	}
    
    public void createWorkingSlots30Days(){
		LocalDate today = LocalDate.now();
		LocalDate lastDay = today.plusDays(30);
		System.out.println("Creating Working Schedule from " + today.toString() + " to " + lastDay.toString());
		addWorkingSlots(today, 30);
    }

	public ArrayList<TimeSlot> getWorkingSlots() {
		return workingSlots;
	}

	//checkFilledSchedule(): check if there are 30 days working slots in advance
	public boolean checkFilledSchedule(){
		//check the last entry of WorkingSlots, if difference between that date and today is more than 30 days, we would fill up the working slots
		TimeSlot lastDay = workingSlots.get(workingSlots.size() - 1);
		if (ChronoUnit.DAYS.between(LocalDate.now(), lastDay.getDate()) < 30){
			return false;
		}
		else return true;
	}

	//fillAdvancedSchedule(): fill in for up to 30 days only when checkFilledSchedule() is false + inform doctor filled up to what date\
	public void fillAdvancedSchedule(){
		if (!checkFilledSchedule()){
			TimeSlot lastDay = workingSlots.get(workingSlots.size() - 1);
			int numDays = (int)ChronoUnit.DAYS.between(lastDay.getDate().plusDays(1), LocalDate.now().plusDays(30));
			addWorkingSlots(lastDay.getDate().plusDays(1), numDays);
		}
	}

	public ArrayList<Appointment> getAppointments(){
    	return appointments;
    }

    public void addAppointment(Appointment appointment) {
        //need to check clash??
        appointments.add(appointment);
        System.out.println("Appointment added: " + appointment);
    }

    public void removeAppointment(int appointmentId) {
        appointments.removeIf(appt -> appt.getAppointmentID() == appointmentId);
        System.out.println("Appointment " + appointmentId + " removed");
    }
    
    public TimeSlot findTimeSlot(LocalDate date, LocalTime time) {
    	for (TimeSlot timeslot : workingSlots) {
    		if (time.equals(timeslot.getStartTime()) && date.equals(timeslot.getDate())) return timeslot;
    	}
    	return null;
    }
	//free up time slots
    
    public void setAvailability(TimeSlot timeslot) {
		timeslot.free();
    }

	public boolean checkOverlapping (TimeSlot requestSlot){
		return requestSlot.getOccupied();
	}

   /* public static void main(String[] args) {
        Schedule doctorSchedule = new Schedule(1);
        doctorSchedule.setAvailability("2024-10-20", "10:00");
        doctorSchedule.addAppointment(new Appointment(1, "2024-10-20", "10:00", 101));
        doctorSchedule.viewSchedule();
        doctorSchedule.removeAppointment(1);
        doctorSchedule.viewSchedule();
    }*/
	
	public Appointment findAppointment(TimeSlot timeslot) {
		Appointment apt = null;
		for (Appointment appointment : appointments) {
			if (appointment.getTimeSlot().equals(timeslot)) {
				apt = appointment;
				break;
			}
		}
		return apt;
	}

	public String getDoctorID() {
		return doctorID;
	}

}


