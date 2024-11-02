package controller;

import model.User;
import java.util.Scanner;

public abstract class BaseController {
    protected Scanner scanner;

    public BaseController() {
        this.scanner = new Scanner(System.in);
    }

    public abstract void displayMenu();

    protected void print(String message) {
        System.out.println(message);
    }

    protected int getIntInput(String prompt) {
        print(prompt);
        return scanner.nextInt();
    }

    protected String getStringInput(String prompt) {
        print(prompt);
        return scanner.nextLine();
    }

    // Method to log out and close the scanner if needed
    public void logout() {
        print("Logging out...");
    }
}
