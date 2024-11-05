package view;

import model.RefillRequest;
import model.Request;
import model.User;

import java.util.List;

public class AdministratorView {

    public void displayMenu() {
        System.out.println("Administrator Menu:");
        System.out.println("1. Manage Staff");
        System.out.println("2. Lock/Unlock Accounts");
        System.out.println("3. Reset Staff Passwords");
        System.out.println("4. View Staff Activity Logs");
        System.out.println("5. View Pending Refill Requests");
        System.out.println("6. Approve Refill Request");
        System.out.println("7. Decline Refill Request");
        System.out.println("8. View Pending User Requests");
        System.out.println("9. Approve User Request");
        System.out.println("10. Decline User Request");
        System.out.println("11. Logout");
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayStaffList(List<User> staff) {
        if (staff.isEmpty()) {
            System.out.println("No staff members.");
        } else {
            System.out.println("Hospital Staff:");
            for (User user : staff) {
                System.out.println(user);
            }
        }
    }

    public void displayPendingRefillRequests(List<RefillRequest> requests) {
        if (requests.isEmpty()) {
            System.out.println("No pending refill requests.");
        } else {
            System.out.println("Pending Refill Requests:");
            for (int i = 0; i < requests.size(); i++) {
                System.out.println((i + 1) + ". " + requests.get(i));
            }
        }
    }

    public void displayPendingUserRequests(List<Request> requests) {
        if (requests.isEmpty()) {
            System.out.println("No pending user requests.");
        } else {
            System.out.println("Pending User Requests:");
            for (int i = 0; i < requests.size(); i++) {
                System.out.println((i + 1) + ". " + requests.get(i));
            }
        }
    }
}
