package model;

import java.util.ArrayList;
import java.util.LinkedList;

public class Database {
	public static final ArrayList<Doctor> doctors = new ArrayList<Doctor>();
	public static final ArrayList<Patient> patients = new ArrayList<Patient>();
	public static final ArrayList<Pharmacist> pharmacists = new ArrayList<Pharmacist>();
	public static final ArrayList<Administrator> administrators = new ArrayList<Administrator>();
	
	public static final MedicationBank medicationBank = new MedicationBank();
	
	public static int getTotalStaffCount() {
	        return doctors.size() + pharmacists.size() + administrators.size();
	    }
}



