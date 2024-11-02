package view;

import controller.DoctorController;
import model.Doctor;

public class DoctorView {
    private DoctorController doctorController;

    public DoctorView(Doctor doctor) {
        this.doctorController = new DoctorController(doctor);
    }

    public void display() {
        while (true) {
            doctorController.displayMenu();
        }
    }
}
