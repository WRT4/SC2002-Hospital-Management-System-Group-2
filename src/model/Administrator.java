package model;

import java.util.ArrayList;
import java.util.List;

public class Administrator extends User {
    private List<User> staff; // List to manage staff members
    private List<RefillRequest> refillRequests; // List to store refill requests for review

    public Administrator(String id, String name) {
        super(id, name, "Administrator");
        this.staff = new ArrayList<>();
        this.refillRequests = new ArrayList<>();
    }

    // Manage staff members (add/remove) with duplication check
    public void manageStaff(User staffMember, String action) {
        if (action.equals("add")) {
            if (!staff.contains(staffMember)) {
                staff.add(staffMember);
                System.out.println(staffMember.getName() + " added to the staff.");
            } else {
                System.out.println(staffMember.getName() + " is already part of the staff.");
            }
        } else if (action.equals("remove")) {
            if (staff.remove(staffMember)) {
                System.out.println(staffMember.getName() + " removed from the staff.");
            } else {
                System.out.println(staffMember.getName() + " is not part of the staff.");
            }
        }
    }

    // Receive a refill request from pharmacists
    public void receiveRefillRequest(RefillRequest request) {
        refillRequests.add(request);
        System.out.println("Refill request received for medication: " + request.getMedication());
    }

    // Approve a specific refill request
    public void approveRefillRequest(int requestIndex) {
        if (requestIndex >= 0 && requestIndex < refillRequests.size()) {
            RefillRequest request = refillRequests.get(requestIndex);
            request.acceptRequest();
            refillRequests.remove(requestIndex); // Remove from list after approval
            System.out.println("Refill request approved for " + request.getMedication());
        } else {
            System.out.println("Invalid request index.");
        }
    }

    // Decline a specific refill request
    public void declineRefillRequest(int requestIndex) {
        if (requestIndex >= 0 && requestIndex < refillRequests.size()) {
            RefillRequest request = refillRequests.get(requestIndex);
            request.declineRequest();
            refillRequests.remove(requestIndex); // Remove from list after decline
            System.out.println("Refill request declined for " + request.getMedication());
        } else {
            System.out.println("Invalid request index.");
        }
    }

    // Display all pending refill requests
    public void viewPendingRefillRequests() {
        if (refillRequests.isEmpty()) {
            System.out.println("No pending refill requests.");
        } else {
            System.out.println("Pending Refill Requests:");
            for (int i = 0; i < refillRequests.size(); i++) {
                System.out.println((i + 1) + ". " + refillRequests.get(i));
            }
        }
    }

    // Lock or unlock a user account
    public void toggleAccountLock(User user, boolean lock) {
        if (lock) {
            user.lockAccount(); // Lock the account
            System.out.println(user.getName() + "'s account has been locked.");
        } else {
            user.unlockAccount(); // Unlock the account
            System.out.println(user.getName() + "'s account has been unlocked.");
        }
    }

    // Reset a staff member's password to the default
    public void resetPassword(User user) {
        user.changePassword("password"); // Reset to default password
        System.out.println(user.getName() + "'s password has been reset to the default.");
    }

    // View the activity log of a specific staff member
    public void viewStaffActivityLog(User staffMember) {
        System.out.println("Activity Log for " + staffMember.getName() + ":");
        staffMember.displayActivityLog();
    }

    // Administrator menu options
    @Override
    public void showMenu() {
        System.out.println("Administrator Menu:");
        System.out.println("1. Manage Staff");
        System.out.println("2. Lock/Unlock Accounts");
        System.out.println("3. Reset Staff Passwords");
        System.out.println("4. View Staff Activity Logs");
        System.out.println("5. View Pending Refill Requests");
        System.out.println("6. Approve Refill Request");
        System.out.println("7. Decline Refill Request");
        System.out.println("8. Logout");
    }
}
