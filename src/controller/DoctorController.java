package controller;

import model.Doctor;

public class DoctorController extends BaseController {
    private Doctor doctor;

    public DoctorController(Doctor doctor) {
        this.doctor = doctor;
    }

    @Override
    public void displayMenu() {
        print("Doctor Menu:");
        print("1. View Schedule");
        print("2. View Appointments");

        int choice = getIntInput("Enter your choice:");

        switch (choice) {
            case 1 -> doctor.getSchedule().viewSchedule();
            case 2 -> doctor.getSchedule().getAppointments().forEach(System.out::println);
            case 3 -> logout();
            default -> print("Invalid choice, please try again.");
        }
    }
}
