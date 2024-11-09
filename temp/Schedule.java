package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.time.temporal.ChronoUnit;

//schedule has appointments and workDays()
public class Schedule {
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

	public void viewAvailableSlots(LocalDate date){
		System.out.println("Free slots on " + date + " :");
		for (TimeSlot timeSlot: workingSlots){
			if (!timeSlot.getOccupied() && timeSlot.getDate().equals(date))
				timeSlot.printTime();
		}
	}

	public void viewSchedule() {
		System.out.println("Schedule for Doctor ID: " + doctorID);
		for (Appointment appt : appointments) {
			System.out.println(appt);
		}
	}
    
    public TimeSlot findTimeSlot(LocalDate date, LocalTime time) {
    	for (TimeSlot timeslot : workingSlots) {
    		if (time.equals(timeslot.getStartTime()) && date.equals(timeslot.getDate())) return timeslot;
    	}
    	return null;
    }
	//free up time slots
    
    public void setAvailability(LocalDate date, TimeSlot timeslot) {
		timeslot.free();
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
    
	public static LocalDate inputDate(boolean isPatient) {
	    Scanner sc = new Scanner(System.in);
	    String dayStr, monthStr, yearStr;
	    int day = 0, month = 0, year = 0;
	    LocalDate finalDate = null;
	    while (true) {
	        try {
	            System.out.println("Enter Day (01-31) or -1 to exit: ");
	            dayStr = sc.next();
	            day = Integer.parseInt(dayStr);
	            if (day == -1) return null;
	            if (day < 1 || day > 31) {
	                throw new RuntimeException("Invalid input for Day! Please enter a valid day between 01 and 31.");
	            }
	            
	            System.out.println("Enter Month (01-12) or -1 to exit: ");
	            monthStr = sc.next();
	            month = Integer.parseInt(monthStr);
	            if (month == -1) return null;
	            if (month < 1 || month > 12) {
	                throw new RuntimeException("Invalid input for Month! Please enter a valid month between 01 and 12.");
	            }
	            
	            System.out.println("Enter Year (Current year - Current year + 1) or -1 to exit: ");
	            yearStr = sc.next();
	            year = Integer.parseInt(yearStr);
	            if (year == -1) return null;
	            if (year < LocalDate.now().getYear() || year > LocalDate.now().getYear() + 1) {
	                throw new RuntimeException("Invalid input for Year! Please enter a valid year.");
	            }

				//system check 1:patient cannot book backwards in time
				finalDate = LocalDate.of(year, month, day);
				if (finalDate.isBefore(LocalDate.now()) && isPatient){
					System.out.println("Date has lapsed! Unable to perform operation!");
					System.out.println("Please re-enter.");
				}
				//system check 2: patient cannot schedule for more than 30 days in-advance
				else if (ChronoUnit.DAYS.between(LocalDate.now(), finalDate) > 30){
					System.out.println("At most 30 days in advance.");
					System.out.println("Please re-enter.");
				}
				else break; // Exit the loop if all inputs are valid

	        } catch (InputMismatchException e) {
	            System.out.println("Wrong input type! Please enter a valid number.");
	            sc.nextLine(); // Clear the invalid input
	        } catch (RuntimeException e) {
	            System.out.println(e.getMessage());
	            sc.nextLine(); // Clear the input
	        } catch (Exception e) {
	            System.out.println("Error! Try Again!");
	            sc.nextLine(); // Clear the input
	        }

	    }
	    return finalDate;
	}

	
	public static LocalTime inputTime() {
	    Scanner sc = new Scanner(System.in);
	    String hourStr, minStr;
	    int hour = 0, min = 0;
		LocalTime finalTime = null;
	    while (true) {
	        try {
	            System.out.println("Enter Hour (00-23) or -1 to exit: ");
	            hourStr = sc.next();
	            hour = Integer.parseInt(hourStr);
	            if (hour == -1) return null;
	            if (hour < 0 || hour > 23) {
	                throw new RuntimeException("Invalid input for Hour! Please enter a valid hour between 00 and 23.");
	            }
	            
	            System.out.println("Enter Minute (00-59) or -1 to exit: ");
	            minStr = sc.next();
	            min = Integer.parseInt(minStr);
	            if (min == -1) return null;
	            if (min < 0 || min > 59) {
	                throw new RuntimeException("Invalid input for Minute! Please enter a valid minute between 00 and 59.");
	            }

				//system check 1:
				finalTime = LocalTime.of(hour, min);
				if (finalTime.isBefore(LocalTime.of(8,0))){
					System.out.println("First appointment slot is at 8am! Unable to perform operation!");
					System.out.println("Please re-enter.");
				}
				else break; // Exit the loop if all inputs are valid
	        } catch (InputMismatchException e) {
	            System.out.println("Wrong input type! Please enter a valid number.");
	            sc.nextLine(); // Clear the invalid input
	        } catch (RuntimeException e) {
	            System.out.println(e.getMessage());
	            sc.nextLine(); // Clear the input
	        } catch (Exception e) {
	            System.out.println("Error! Try Again!");
	            sc.nextLine(); // Clear the input
	        }
	    }
	    return finalTime;
	}

	public void viewAllSlots(LocalDate date) {
		String occupied = "";
		for (TimeSlot timeSlot: workingSlots){
			if (timeSlot.getDate().equals(date)) {
				if (timeSlot.getOccupied() == false) {
					occupied = "FREE";
				}
				else if (timeSlot.getOccupied() == true) {
					Appointment temp = findAppointment(timeSlot);
					if (temp != null && temp.getStatus() != Status.CANCELLED) occupied = temp.toString();
					else occupied = "BUSY";
				}
				System.out.println(timeSlot + ": " + occupied);
				System.out.println();
			}
		}
	}
	
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

}

