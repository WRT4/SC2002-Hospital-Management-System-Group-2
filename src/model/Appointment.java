package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Appointment {
    private Patient patient;
    private Doctor doctor;
    private LocalDate date;
    private LocalTime time;
    private String status;
    private String prescriptionStatus;
    private String AppointmentID;
    private static int count;
    private String prescription;
    
    public Appointment(Patient patient, Doctor doctor, LocalDate date, LocalTime time) {
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.time = time;
        this.status = "PENDING";
        count++;
        this.AppointmentID = "" + count;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime(){
        return this.time;
    }

    public void setTime(LocalTime time) {
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
    public void printAppointmentOutcome(){
        System.out.println("Appointment " + AppointmentID + ": " + date + " at " + time + " with Doctor " + doctor.getName()); //getDoctorName
    }

	public String getAppointmentID() {
		// TODO Auto-generated method stub
		return this.AppointmentID;
	}

	public String getPrescription() {
		return this.prescription;
	}
	
	public void setPrescription(String prescription) {
		this.prescription = prescription;
	}

	public Doctor getDoctor() {
		return doctor;	
	}

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
