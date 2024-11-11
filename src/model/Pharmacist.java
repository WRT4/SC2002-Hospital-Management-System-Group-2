package model;

import java.util.ArrayList;

public class Pharmacist extends User {
    //private MedicationBank medicationBank;
    private ArrayList<RefillRequest> requests;

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
		return requests;
	}

	public void setRequests(ArrayList<RefillRequest> requests) {
		this.requests = requests;
	}
}


