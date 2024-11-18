package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controller.AdministratorController;
import controller.SessionController;

/**
 * Represents an Administrator in the Hospital Management System (HMS).
 * This class extends the User class and provides additional functionality specific to administrators,
 * such as managing staff and handling refill requests.
 * @author Hoo Jing Huan, Lee Kuan Rong, Lim Wee Keat, Tan Wen Rong, Yeoh Kai Wen
 * @version 1.0
 * @since 2024-11-18
 */
public class Administrator extends User {

    private static final long serialVersionUID = -3513955881484371321L;
    private List<User> staff; // List to manage staff members
    private List<RefillRequest> refillRequests; // List to store refill requests for review
    private String gender;
    private int age;

    /**
     * Constructs an Administrator with an ID, name, gender, and age.
     * Sets the default password to "password" and the role to "Administrator".
     *
     * @param id The unique identifier for the administrator.
     * @param name The name of the administrator.
     * @param gender The gender of the administrator.
     * @param age The age of the administrator.
     */
    public Administrator(String id, String name, String gender, int age) {
        super(id, name, "password", "Administrator");
        this.gender = gender;
        this.age = age;
    }

    /**
     * Constructs an Administrator with an ID and name.
     * Sets the default password to "password" and the role to "Administrator".
     *
     * @param id The unique identifier for the administrator.
     * @param name The name of the administrator.
     */
    public Administrator(String id, String name) {
        this(id, name, "password");
    }

    /**
     * Constructs an Administrator with an ID, name, and password.
     * Sets the role to "Administrator" and initializes the staff and refill requests lists.
     *
     * @param id The unique identifier for the administrator.
     * @param name The name of the administrator.
     * @param password The password for the administrator.
     */
    public Administrator(String id, String name, String password) {
        super(id, name, password, "Administrator");
        this.staff = new ArrayList<>();
        this.setRefillRequests(new ArrayList<>());
    }

    /**
     * Gets the list of staff members managed by the administrator.
     *
     * @return A list of users representing the staff members.
     */
    public List<User> getStaff() {
        return this.staff;
    }

    /**
     * Receives a refill request from a pharmacist and adds it to the refill requests list.
     *
     * @param request The refill request to be added.
     */
    public void receiveRefillRequest(RefillRequest request) {
        if (getRefillRequests() == null) {
            setRefillRequests(new ArrayList<>()); // Initialize if null
        }
        getRefillRequests().add(request);
    }

    /**
     * Gets the list of refill requests currently in the system.
     *
     * @return A list of refill requests.
     */
    public List<RefillRequest> getRefillRequests() {
        if (refillRequests == null) {
            refillRequests = new ArrayList<>(); // Initialize if null
        }
        return refillRequests;
    }

    /**
     * Sets the list of refill requests for the administrator.
     *
     * @param refillRequests A list of refill requests to be set.
     */
    public void setRefillRequests(List<RefillRequest> refillRequests) {
        this.refillRequests = refillRequests;
    }

    /**
     * Provides a string representation of the administrator, including their ID, name, gender, and age.
     *
     * @return A string representing the administrator.
     */
    @Override
    public String toString() {
        return "Administrator ID: " + getID() + ", Name: " + getName() + ", Gender: " + gender + ", Age: " + age;
    }

    /**
     * Gets the gender of the administrator.
     *
     * @return The gender of the administrator.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Gets the age of the administrator.
     *
     * @return The age of the administrator.
     */
    public int getAge() {
        return age;
    }

    /**
     * Creates a session controller for the administrator, allowing them to access the administrator dashboard.
     *
     * @param scanner A Scanner object for reading user input during the session.
     * @return An AdministratorController object for managing the administrator's session.
     */
    @Override
    public SessionController createController(Scanner scanner) {
        System.out.println("Accessing Administrator Dashboard...");
        return new AdministratorController(this, scanner);
    }
}
