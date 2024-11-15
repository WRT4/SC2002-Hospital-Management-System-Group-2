package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controller.AdministratorController;
import controller.SessionController;

public class Administrator extends User {
    private List<User> staff; // List to manage staff members
    private List<RefillRequest> refillRequests; // List to store refill requests for review
    private List<Request> userRequests; // List to store general user requests (password reset, unlock account)
    private String gender;
    private int age;
    
    public Administrator(String id, String name, String gender, int age) {
        super(id, name, "password", "Administrator");
        this.gender = gender;
        this.age = age;
    }

    public Administrator(String id, String name) {
        this(id, name, "password");
    }
    
    public Administrator(String id, String name, String password) {
    	super(id, name, password, "Administrator");
        this.staff = new ArrayList<>();
        this.setRefillRequests(new ArrayList<>());
        this.userRequests = new ArrayList<>();
    }
 
    public List<User> getStaff(){ 
    	return this.staff;
    }
    
    // Receive a refill request from pharmacists
    public void receiveRefillRequest(RefillRequest request) {
        getRefillRequests().add(request);
    }

	public List<RefillRequest> getRefillRequests() {
		return refillRequests;
	}

	public void setRefillRequests(List<RefillRequest> refillRequests) {
		this.refillRequests = refillRequests;
	}

	public String toString() {
        return "Administrator ID: " + getID() + ", Name: " + getName() + ", Gender: " + gender + ", Age: " + age;
    }
	
	public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

	@Override
	public SessionController createController(Scanner scanner) {
		System.out.println("Accessing Administrator Dashboard...");
		return new AdministratorController(this, scanner);
	}


//    //********************************* This part onwards is mainly extras, not for test cases ******************************************8**
//
//    // Keeping original methods for handling user requests, account locking, password resetting, and logging
//    public void receiveUserRequest(Request request) {
//        userRequests.add(request);
//        System.out.println("User request received: " + request);
//    }
//
//
//    // Approve a general user request
//    public void approveUserRequest(int requestIndex) {
//        if (requestIndex >= 0 && requestIndex < userRequests.size()) {
//            Request request = userRequests.get(requestIndex);
//            request.acceptRequest();
//            userRequests.remove(requestIndex); // Remove from list after approval
//            System.out.println("User request approved.");
//        } else {
//            System.out.println("Invalid request index.");
//        }
//    }
//
//    // Display all pending user requests
//    public void viewPendingUserRequests() {
//        if (userRequests.isEmpty()) {
//            System.out.println("No pending user requests.");
//        } else {
//            System.out.println("Pending User Requests:");
//            for (int i = 0; i < userRequests.size(); i++) {
//                System.out.println((i + 1) + ". " + userRequests.get(i));
//            }
//        }
//    }
//
// // Decline a general user request
//    public void declineUserRequest(int requestIndex) {
//        if (requestIndex >= 0 && requestIndex < userRequests.size()) {
//            Request request = userRequests.get(requestIndex);
//            request.declineRequest();
//            userRequests.remove(requestIndex); // Remove from list after decline
//            System.out.println("User request declined.");
//        } else {
//            System.out.println("Invalid request index.");
//        }
//    }
//
//    // Lock or unlock a user account
//    public void toggleAccountLock(User user, boolean lock) {
//        if (lock) {
//            user.lockAccount(); // Lock the account
//            System.out.println(user.getName() + "'s account has been locked.");
//        } else {
//            user.unlockAccount(); // Unlock the account
//            System.out.println(user.getName() + "'s account has been unlocked.");
//        }
//    }
//
//    // Method to receive a password reset request
//    public void receiveRequest(PasswordResetRequest request) {
//        userRequests.add(request);
//        System.out.println("Password reset request received from user: " + request.getUser().getName());
//    }
//
//    // Method to receive an unlock account request
//    public void receiveRequest(UnlockAccountRequest request) {
//        userRequests.add(request);
//        System.out.println("Unlock account request received from user: " + request.getUser().getName());
//    }
//
//
//    // Reset a staff member's password to the default
//    public void resetPassword(User user) {
//        user.changePassword("password"); // Reset to default password
//        System.out.println(user.getName() + "'s password has been reset to the default.");
//    }
//
//    // View the activity log of a specific staff member
//    public void viewStaffActivityLog(User staffMember) {
//        System.out.println("Activity Log for " + staffMember.getName() + ":");
//        staffMember.displayActivityLog();
//    }
//
//    public String toString() {
//    	return "Administrator ID: " + getId() + " Name: " + getName();
//    }
//
//	//No need for decline Refill request(?) Keeping it here jic.
//     // Decline a specific refill request
//    /*public void declineRefillRequest(int requestIndex) {
//        if (requestIndex >= 0 && requestIndex < refillRequests.size()) {
//            RefillRequest request = refillRequests.get(requestIndex);
//            request.declineRequest();
//            refillRequests.remove(requestIndex); // Remove from list after decline
//            System.out.println("Refill request declined for " + request.getMedication());
//        } else {
//            System.out.println("Invalid request index.");
//        }
//    }*/

    
}

