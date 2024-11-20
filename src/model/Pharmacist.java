package model;

import java.util.ArrayList;
import java.util.Scanner;

import controller.PharmacistController;
import controller.SessionController;

/**
 * Represents a pharmacist in the medical system.
 * Each pharmacist has personal information and a list of refill requests to manage.
 * Provides methods to access personal details and manage refill requests.
 * @author: Lee Kuan Rong, Lim Wee Keat, Tan Wen Rong, Yeoh Kai Wen
 * @version: 1.0
 * @since: 2024-11-18
 */
public class Pharmacist extends User {
    
    private static final long serialVersionUID = 1L;
    private ArrayList<RefillRequest> requests;
    private String gender;
    private int age;
    
    /**
     * Constructs a Pharmacist object with detailed information.
     * Initializes the lists for requests.
     *
     * @param id          The unique ID of the pharmacist
     * @param name        The name of the pharmacist
     * @param gender      The gender of the pharmacist
     * @param age         The age of the pharmacist
     */
    public Pharmacist(String id, String name, String gender, int age) {
        super(id, name, "password", "Pharmacist");
        this.gender = gender;
        this.age = age;
    }

    /**
     * Constructs a Pharmacist object with a given ID and name.
     * Password defaults to "password".
     *
     * @param id   The unique ID of the pharmacist
     * @param name The name of the pharmacist
     */
    public Pharmacist(String id, String name) {
        super(id, name, "Pharmacist");
        this.setRequests(new ArrayList<>());
    }
    
    /**
     * Constructs a Pharmacist object with a given ID, password, and name.
     * Initializes the list for requests.
     *
     * @param id       The unique ID of the pharmacist
     * @param password The password for the pharmacist
     * @param name     The name of the pharmacist
     */
    public Pharmacist(String id, String password, String name) {
        super(id, name, password, "Pharmacist");
        this.setRequests(new ArrayList<>());
    }
    
    /**
     * Returns a string representation of the pharmacist, including their ID and name.
     *
     * @return A string with pharmacist details
     */
    public String toString() {
        return "Pharmacist ID: " + getID() + " Name: " + getName();
    }

    /**
     * Retrieves the list of refill requests managed by the pharmacist.
     * 
     * @return An ArrayList of refill requests
     */
    public ArrayList<RefillRequest> getRequests() {
        if (requests == null) {
            requests = new ArrayList<>(); // Initialize if null
        }
        return requests;
    }

    /**
     * Sets the list of refill requests for the pharmacist.
     *
     * @param requests The new list of refill requests
     */
    public void setRequests(ArrayList<RefillRequest> requests) {
        this.requests = requests;
    }
    
    /**
     * Retrieves the gender of the pharmacist.
     * 
     * @return The gender of the pharmacist
     */
    public String getGender() {
        return gender;
    }

    /**
     * Retrieves the age of the pharmacist.
     * 
     * @return The age of the pharmacist
     */
    public int getAge() {
        return age;
    }
    
    /**
     * Creates a session controller for the pharmacist, used for accessing the pharmacist dashboard.
     *
     * @param scanner A Scanner instance for user input
     * @return A new PharmacistController instance
     */
    @Override
    public SessionController createController(Scanner scanner) {
        System.out.println("Accessing Pharmacist Dashboard...");
        return new PharmacistController(this, scanner);
    }
}
