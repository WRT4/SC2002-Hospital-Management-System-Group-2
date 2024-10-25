package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;
import java.util.InputMismatchException;

//schedule has appointments and workDays()
public class Schedule {
	private String doctorID;
    private ArrayList<Appointment> appointments;
    private ArrayList<TimeSlot> workingSlots;


    public Schedule(String doctorID){
		this.doctorID = doctorID;
        //create working slots for 2 weeks timeframe
		workingSlots = new ArrayList<>();
        createWorkingSlots();
        appointments = new ArrayList<>();
    }
    
    public void createWorkingSlots(){
		LocalDate today = LocalDate.now();
		LocalDate lastDay = today.plusDays(14);
		System.out.println("Creating Working Schedule from" + today.toString() + " to " + lastDay.toString());
		LocalDate day;
		for (int i=0; i <14; i++){
			day = today.plusDays(i);
			workingSlots.add(new TimeSlot(day, LocalTime.of(8,0), LocalTime.of(17,0)));
		}
    }

	public ArrayList<TimeSlot> getWorkingSlots() {
		return workingSlots;
	}

	public ArrayList<Appointment> getAppointments(){
    	return appointments;
    }

    public void addAppointment(Appointment appointment) {
        //need to check clash??
        appointments.add(appointment);
        System.out.println("Appointment added: " + appointment);
    }

    public void removeAppointment(String appointmentId) {
        appointments.removeIf(appt -> appt.getAppointmentID() == appointmentId);
        System.out.println("Appointment " + appointmentId + " removed");
    }

    public void viewSchedule() {
        System.out.println("Schedule for Doctor ID: " + doctorID);
        for (Appointment appt : appointments) {
            System.out.println(appt);
        }
    }
	//free up time slots
    public void setAvailability(LocalDate date, LocalTime timeslot) {
		timeslot.
        Appointment availability = new Appointment(null, null, date, time); // -1 indicates availability slot
        appointments.add(availability);
        System.out.println("Availability set for " + date + " at " + time);
    }

	public boolean checkOverlapping (TimeSlot requestSlot){
		for (TimeSlot slot: workingSlots){
			if(slot.getOccupied()){
				return requestSlot.isOverlap(slot);
			}
		}
		return false;
	}

   /* public static void main(String[] args) {
        Schedule doctorSchedule = new Schedule(1);
        doctorSchedule.setAvailability("2024-10-20", "10:00");
        doctorSchedule.addAppointment(new Appointment(1, "2024-10-20", "10:00", 101));
        doctorSchedule.viewSchedule();
        doctorSchedule.removeAppointment(1);
        doctorSchedule.viewSchedule();
    }*/
    
    public static LocalDate inputDate() {
		Scanner sc = new Scanner(System.in);
		int day=0, month=0, year = 0;
		while (true) {
			try {
				System.out.println("Enter Day: ");
				day = sc.nextInt();
				if (day > 31 || day < 1) {
					throw new RuntimeException("Invalid input for Day!");
				}
				System.out.println("Enter Month: ");
				month = sc.nextInt();
				if (month > 12 || month < 1) {
					throw new RuntimeException("Invalid input for Month!");
				}
				System.out.println("Enter Year: ");
				year = sc.nextInt();
				if (year < LocalDate.now().getYear() || year > LocalDate.now().getYear()+1) {
					throw new RuntimeException("Invalid input for Year!");
				}
				break;
			}
			catch (InputMismatchException e) {
				System.out.println("Wrong input type! Try Again!");
				sc.nextLine();
				continue;
			}
			catch (RuntimeException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
				continue;
			}
			catch (Exception e) {
				System.out.println("Error! Try Again!");
				sc.nextLine();
				continue;
			}
		}
		sc.close();
		return LocalDate.of(year, month, day);
	}
	
	public static LocalTime inputTime() {
		Scanner sc = new Scanner(System.in);
		int hour=0, min=0;
		while (true) {
			try {
				System.out.println("Enter Hour (24 hour format): ");
				hour = sc.nextInt();
				if (hour > 23 || hour < 0) {
					throw new RuntimeException("Invalid input for Hour!");
				}
				System.out.println("Enter Minute: ");
				min = sc.nextInt();
				if (min > 59 || min < 0) {
					throw new RuntimeException("Invalid input for Minute!");
				}
				break;
			}
			catch (InputMismatchException e) {
				System.out.println("Wrong input type! Try Again!");
				sc.nextLine();
				continue;
			}
			catch (RuntimeException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
				continue;
			}
			catch (Exception e) {
				System.out.println("Error! Try Again!");
				sc.nextLine();
				continue;
			}
		}
		sc.close();
		return LocalTime.of(hour, min);
	}
}


