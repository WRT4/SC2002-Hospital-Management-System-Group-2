package controller;

import model.Pharmacist;
import model.Administrator;

public class PharmacistController extends BaseController {
    private Pharmacist pharmacist;
    private Administrator admin;

    public PharmacistController(Pharmacist pharmacist, Administrator admin) {
        this.pharmacist = pharmacist;
        this.admin = admin;
    }

    @Override
    public void displayMenu() {
        print("Pharmacist Menu:");
        print("1. Submit Refill Request");

        int choice = getIntInput("Enter your choice:");

        if (choice == 1) {
            String medication = getStringInput("Enter medication name:");
            int quantity = getIntInput("Enter quantity:");
            pharmacist.submitReplenishmentRequest(medication, quantity, admin);
        } else {
            print("Invalid choice, please try again.");
        }
    }
}
