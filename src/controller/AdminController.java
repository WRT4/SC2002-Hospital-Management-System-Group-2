package controller;

import model.Administrator;
import model.User;

public class AdminController extends BaseController {
    private Administrator admin;

    public AdminController(Administrator admin) {
        this.admin = admin;
    }

    @Override
    public void displayMenu() {
        admin.showMenu();
        int choice = getIntInput("Enter your choice:");

        switch (choice) {
            case 1 -> manageStaff();
            case 2 -> lockOrUnlockAccount();
            case 3 -> resetPassword();
            case 4 -> viewActivityLog();
            case 5 -> admin.viewPendingRefillRequests();
            case 6 -> approveRefillRequest();
            case 7 -> declineRefillRequest();
            case 8 -> admin.viewPendingUserRequests();
            case 9 -> approveUserRequest();
            case 10 -> declineUserRequest();
            case 11 -> logout();
            default -> print("Invalid choice, please try again.");
        }
    }

    private void manageStaff() {
        // Implement code for adding/removing staff
    }

    private void lockOrUnlockAccount() {
        // Implement code for locking/unlocking user accounts
    }

    private void resetPassword() {
        // Implement code for resetting a user's password
    }

    private void viewActivityLog() {
        // Implement code for viewing a staff member's activity log
    }

    private void approveRefillRequest() {
        // Implement code for approving refill requests
    }

    private void declineRefillRequest() {
        // Implement code for declining refill requests
    }

    private void approveUserRequest() {
        // Implement code for approving user requests
    }

    private void declineUserRequest() {
        // Implement code for declining user requests
    }
}
