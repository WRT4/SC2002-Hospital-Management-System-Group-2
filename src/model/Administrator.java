package model;

import java.util.ArrayList;
import java.util.List;

public class Administrator extends User {

    private List<User> staff; // List to manage staff members
    private List<RefillRequest> refillRequests; // List to store refill requests
    private List<Request> userRequests; // List to store user requests (password reset, unlock account)

    public Administrator(String id, String name) {
        super(id, name, "Administrator");
        this.staff = new ArrayList<>();
        this.refillRequests = new ArrayList<>();
        this.userRequests = new ArrayList<>();
    }

    // Staff management methods
    public List<User> getStaff() {
        return staff;
    }

    public void addStaffMember(User staffMember) {
        staff.add(staffMember);
    }

    public void removeStaffMember(User staffMember) {
        staff.remove(staffMember);
    }

    // Request handling methods
    public List<RefillRequest> getRefillRequests() {
        return refillRequests;
    }

    public void addRefillRequest(RefillRequest request) {
        refillRequests.add(request);
    }

    public List<Request> getUserRequests() {
        return userRequests;
    }

    public void addUserRequest(Request request) {
        userRequests.add(request);
    }
}
