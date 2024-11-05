package controller;

import model.Administrator;
import model.RefillRequest;
import model.Request;
import model.User;
import view.AdministratorView;

public class AdministratorController {

    private Administrator administrator;
    private AdministratorView view;

    public AdministratorController(Administrator administrator, AdministratorView view) {
        this.administrator = administrator;
        this.view = view;
    }

    // Staff management
    public void manageStaff(User staffMember, String action) {
        if (action.equals("add")) {
            if (!administrator.getStaff().contains(staffMember)) {
                administrator.addStaffMember(staffMember);
                view.displayMessage(staffMember.getName() + " added to staff.");
            } else {
                view.displayMessage(staffMember.getName() + " is already in staff.");
            }
        } else if (action.equals("remove")) {
            if (administrator.getStaff().remove(staffMember)) {
                view.displayMessage(staffMember.getName() + " removed from staff.");
            } else {
                view.displayMessage(staffMember.getName() + " is not in staff.");
            }
        }
    }

    // Refill request handling
    public void approveRefillRequest(int requestIndex) {
        if (requestIndex >= 0 && requestIndex < administrator.getRefillRequests().size()) {
            RefillRequest request = administrator.getRefillRequests().get(requestIndex);
            request.acceptRequest();
            administrator.getRefillRequests().remove(requestIndex);
            view.displayMessage("Refill request approved for " + request.getMedication());
        } else {
            view.displayMessage("Invalid request index.");
        }
    }

    public void declineRefillRequest(int requestIndex) {
        if (requestIndex >= 0 && requestIndex < administrator.getRefillRequests().size()) {
            RefillRequest request = administrator.getRefillRequests().get(requestIndex);
            request.declineRequest();
            administrator.getRefillRequests().remove(requestIndex);
            view.displayMessage("Refill request declined for " + request.getMedication());
        } else {
            view.displayMessage("Invalid request index.");
        }
    }

    // User request handling
    public void approveUserRequest(int requestIndex) {
        if (requestIndex >= 0 && requestIndex < administrator.getUserRequests().size()) {
            Request request = administrator.getUserRequests().get(requestIndex);
            request.acceptRequest();
            administrator.getUserRequests().remove(requestIndex);
            view.displayMessage("User request approved.");
        } else {
            view.displayMessage("Invalid request index.");
        }
    }

    public void declineUserRequest(int requestIndex) {
        if (requestIndex >= 0 && requestIndex < administrator.getUserRequests().size()) {
            Request request = administrator.getUserRequests().get(requestIndex);
            request.declineRequest();
            administrator.getUserRequests().remove(requestIndex);
            view.displayMessage("User request declined.");
        } else {
            view.displayMessage("Invalid request index.");
        }
    }

    // Account lock/unlock
    public void toggleAccountLock(User user, boolean lock) {
        if (lock) {
            user.lockAccount();
            view.displayMessage(user.getName() + "'s account has been locked.");
        } else {
            user.unlockAccount();
            view.displayMessage(user.getName() + "'s account has been unlocked.");
        }
    }

    // Password reset
    public void resetPassword(User user) {
        user.changePassword("password"); // Reset to default password
        view.displayMessage(user.getName() + "'s password has been reset to the default.");
    }
}
