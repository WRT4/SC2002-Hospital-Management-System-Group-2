package model;

import java.util.ArrayList;
import java.util.InputMismatchException;

public class Pharmacist extends User{
	private ArrayList<String> medicationInventory;
	private ArrayList<Integer> stockLevels;
	private static final int LOW_STOCK_THRESHOLD = 5;
	
	public Pharmacist(String id, String name) {
		super(id,name, "Pharmacist");
		this.medicationInventory = new ArrayList<>();
		this.stockLevels = new ArrayList<>();
		
	}
	
	public void updatePrescriptionStatus(Appointment appointment, String status) {
		try {
			if(appointment == null) {
				throw new NullPointerException ("Appointment cannot be null.");
				
			}
			if (status == null || status.isEmpty()) {
				throw new IllegalArgumentException("Prescription status cannot be null or empty.");
			}
			
			// Assuming valid statuses are "pending" or "dispensed"
			if (!status.equalsIgnoreCase("pending") && !status.equalsIgnoreCase("dispensed")) {
				throw new IllegalArgumentException("Invalid prescription status:"+ status);
				
			}
			
			appointment.setPrescriptionStatus(status); // in appointment class
			System.out.println("Prescripttion status updated to: "+ status);
			
		}catch (NullPointerException e) {
			System.out.println("Error:"+ e.getMessage());
		
		}catch(IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
			
		}catch(Exception e) {
			System.out.println("An unexpected error occured while updating the prescription status.");
		}
	}
	
	
	public void viewInventory() {
		try {
			if(medicationInventory ==null|| medicationInventory.isEmpty()) {
				throw new Exception("Medication inventory is empty.");
			}
			System.out.println("Current Medication Inventory:");
			for(int i = 0; i< medicationInventory.size(); i++) {
				System.out.println(medicationInventory.get(i)+ "- Stock:"+ stockLevels.get(i));
				if(stockLevels.get(i)<= LOW_STOCK_THRESHOLD);{
					System.out.println("!!Low Stock warning for:"+ medicationInventory.get(i));
				}
			}
		}catch (Exception e) {
			System.out.println("Error:"+ e.getMessage());
		}
	}
	
	
	public void addMedicationToInventory (String medication, int stock) {
		try {
			if(medication == null || medication.isEmpty()) {
				throw new IllegalArgumentException("Medication name cannot be null or empty.");
				
			}
			if(stock <=0) {
				throw new IllegalArgumentException("Stock level must be greater than zero.");
			}
			
			medicationInventory.add(medication); 
			stockLevels.add(stock);
			System.out.println(medication +"added to the inventory with stock: "+ stock);
		
		}catch(IllegalArgumentException e) {
			System.out.println("Error:" +e.getMessage());
			
		}
	}
	
	public void submitreplenishmentRequest(String medication) {
		int index= medicationInventory.indexOf(medication);
		if (index == -1) {
            System.out.println("Medication not found in inventory.");
            return;
        }
		if (stockLevels.get(index) <= LOW_STOCK_THRESHOLD) {
            System.out.println("Replenishment request submitted for " + medication + " (current stock: " + stockLevels.get(index) + ")");
            administrator.receiveReplenishmentRequest(medication); // Here we can notify the administrator via some notification mechanism
        }
		else {
            System.out.println("Stock is sufficient for " + medication);
        }
		
	}
	
	public void viewAppointmentOutcomeRecords(ArrayList<Appointment> appointments) {
        if (appointments == null || appointments.isEmpty()) {
            System.out.println("No appointment records available.");
            return;
        }
        System.out.println("Appointment Outcome Records:");
        for (Appointment appointment : appointments) {
            System.out.println("Appointment ID: " + appointment.getAppointmentID() +
                               ", Prescription: " + appointment.getPrescription() +
                               ", Status: " + appointment.getPrescriptionStatus());
        }
    }
	
	public void showMenu() {
        System.out.println("1. View Appointment Outcome Records");
        System.out.println("2. Update Prescription Status");
        System.out.println("3. View Medication Inventory");
        System.out.println("4. Add Medication to Inventory");
        System.out.println("5. Submit Replenishment Request");
        System.out.println("6. Logout");
    }
}

