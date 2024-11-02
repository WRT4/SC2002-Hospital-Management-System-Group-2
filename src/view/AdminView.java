package view;

import controller.AdminController;
import model.Administrator;

public class AdminView {
    private AdminController adminController;

    public AdminView(Administrator admin) {
        this.adminController = new AdminController(admin);
    }

    public void display() {
        while (true) {
            adminController.displayMenu();
        }
    }
}
