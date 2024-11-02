package view;

import controller.PharmacistController;
import model.Pharmacist;
import model.Administrator;

public class PharmacistView {
    private PharmacistController pharmacistController;

    public PharmacistView(Pharmacist pharmacist, Administrator admin) {
        this.pharmacistController = new PharmacistController(pharmacist, admin);
    }

    public void display() {
        while (true) {
            pharmacistController.displayMenu();
        }
    }
}
