package model;

import java.util.ArrayList;
import java.util.Scanner;

import controller.PharmacistController;
import controller.SessionController;

public class Pharmacist extends User {
    //private MedicationBank medicationBank;
    private ArrayList<RefillRequest> requests;
    private String gender;
    private int age;
    
    public Pharmacist(String id, String name, String gender, int age) {
        super(id, name, "password", "Pharmacist");
        this.gender = gender;
        this.age = age;
    }

    public Pharmacist(String id, String name) {
        super(id, name, "Pharmacist");
        this.setRequests(new ArrayList<>());
    }
    
    public Pharmacist(String id, String password, String name) {
    	super(id,name,password,"Pharmacist");
    	this.setRequests(new ArrayList<>());
    }
    
    public String toString() {
    	return "Pharmacist ID: " + getID() + " Name: " + getName();
    }

    public ArrayList<RefillRequest> getRequests() {
        if (requests == null) {
            requests = new ArrayList<>(); // Initialize if null
        }
        return requests;
    }

	public void setRequests(ArrayList<RefillRequest> requests) {
		this.requests = requests;
	}
	
	public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }
    
    
    @Override
	public SessionController createController(Scanner scanner) {
    	System.out.println("Accessing Pharmacist Dashboard...");
		return new PharmacistController(this, scanner);
	}
}

