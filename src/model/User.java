package model;

import java.util.ArrayList;
import java.util.Scanner;

import controller.SessionController;

/**
 * User.java
 * Represents a user in the Hospital Management System (HMS).
 * This class provides login functionality, password management, and role-based access.
 */
public abstract class User {
    protected String id;
    private String password;
    protected String role;
    private boolean isFirstLogin;
    protected String name;
    protected ArrayList<String> messages;
    protected int unreadIndex;
    private boolean isLocked = false;

    public User(String id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.setPassword("password"); // Default password
        //this.failedLoginAttempts = 0;
        this.isLocked = false;
        this.setFirstLogin(true); // Default to true for new users
        this.messages = new ArrayList<String>();
        this.unreadIndex = 0;
        //this.passwordLastChanged = LocalDate.now(); // Password set to current date
        //this.activityLog = new ArrayList<>();
        //this.userRequests = new ArrayList<>();
    }
    
    public boolean isLocked() {
    	return isLocked;
    }
    
    public User(String id, String name, String password, String role) {
        this(id, name, role);
        this.setPassword(password);
    }

    // Method to display user information
    public void displayUserInfo() {
        System.out.println("User ID: " + id);
        System.out.println("Role: " + role);
    }

    /**
     * Getter method for the user's name.
     * @return The name of the user.
     */
    public String getName() {
        return this.name;
    }
    
    public void setLocked(boolean locked) {
    	this.isLocked = locked;
    }

    public int getUnreadIndex() {
    	return this.unreadIndex;
    }
    
    public void setUnreadIndex(int i) {
    	this.unreadIndex = i;
    }


    public String getRole() {
        return this.role;
    }
    
    public String getID() {
        return this.id;
    }

	public void setPassword(String newPassword) {
		this.password = newPassword;
	}

	public boolean isFirstLogin() {
		return isFirstLogin;
	}

	public void setFirstLogin(boolean isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}

	public String getPassword() {
		return password;
	}
    
	public ArrayList<String> getMessages(){
		return messages;
	}
	
	public abstract SessionController createController(Scanner scanner);

	public void unLock() {
		this.isLocked = false;
	}
	
}
