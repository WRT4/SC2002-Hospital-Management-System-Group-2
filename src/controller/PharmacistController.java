package controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

import application.Database;
import enums.Status;
import model.Administrator;
import model.Appointment;
import model.Medication;
import model.Patient;
import model.Pharmacist;
import model.RefillRequest;
import view.PatientView;
import view.PharmacistView;
import view.AppointmentView;

/**
 * Controls the operations related to pharmacists in the medical system.
 * Manages medication inventory, prescription statuses, and appointment outcomes.
 * Provides methods to submit replenishment requests and update prescription statuses.
 * @author Lee Kuan Rong, Lim Wee Keat, Tan Wen Rong, Yeoh Kai Wen
 * @version 1.0
 * @since 2024-11-18
 */
public class PharmacistController extends SessionController {

    private PharmacistView pharmacistView;
    private Pharmacist pharmacist;

    /**
     * Constructs a PharmacistController object with a given pharmacist and scanner for input.
     *
     * @param p The Pharmacist instance
     * @param s The Scanner instance for user input
     */
    public PharmacistController(Pharmacist p, Scanner s) {
        pharmacist = p;
        this.scanner = s;
        pharmacistView = new PharmacistView(scanner);
        unreadIndex = p.getUnreadIndex();
        startTime = LocalTime.now();
        startDate = LocalDate.now();
    }

    /**
     * Displays the pharmacist menu and handles user input for menu options.
     */
    public void showMenu() {
        int choice;
        do {
            pharmacistView.showMessageBox(pharmacist.getMessages(), unreadIndex);
            pharmacistView.showMenu();
            choice = pharmacistView.getChoice();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    unreadIndex = pharmacistView.viewInbox(pharmacist.getMessages(), unreadIndex);
                    pharmacist.setUnreadIndex(unreadIndex);
                    break;
                case 2:
                    Patient patient = getPatient();
                    if (patient == null) {
                        System.out.println("No valid patient selected. Returning to menu.");
                        break;
                    }
                    PatientView.viewAppointmentOutcomes(patient.getAppointments());
                    break;
                case 3:
                    updatePrescriptionStatus();
                    break;
                case 4:
                    pharmacistView.viewMedicationInventory();
                    break;
                case 5:
                    submitReplenishmentRequest();
                    break;
                case 6:
                    System.out.println("Exiting Pharmacist menu...");
                    String log = "Pharmacist " + pharmacist.getID() + " accessed system from " + startTime.format(FORMATTER) + " on " + startDate + " to " + LocalTime.now().format(FORMATTER) + " on " + LocalDate.now();
                    Database.SYSTEM_LOGS.add(log);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);
    }

    /**
     * Retrieves a patient by their ID.
     *
     * @return The patient with the matching ID, or null if not found
     */
    public Patient getPatient() {
        for (Patient pat : Database.PATIENTS) {
            System.out.println(pat);
        }
        System.out.println("Enter Patient ID or -1 to go back: ");
        String id = pharmacistView.enterID().trim();
        if (id.equals("-1")) return null;
        for (Patient pat : Database.PATIENTS) {
            if (pat.getID().equals(id)) {
                return pat;
            }
        }
        System.out.println("Patient not found!");
        return null;
    }

    /**
     * Retrieves an administrator by their ID.
     *
     * @return The administrator with the matching ID, or null if not found
     */
    public Administrator getAdmin() {
        for (Administrator admin : Database.ADMINISTRATORS) {
            System.out.println(admin);
        }
        System.out.println("Enter Admin ID or -1 to go back: ");
        String id = pharmacistView.enterID().trim();
        if (id.equals("-1")) return null;
        for (Administrator admin : Database.ADMINISTRATORS) {
            if (admin.getID().equals(id)) {
                return admin;
            }
        }
        System.out.println("Administrator not found!");
        return null;
    }

    /**
     * Submits a replenishment request for a medication.
     */
    public void submitReplenishmentRequest() {
        System.out.print("Enter Medication Name for replenishment request: ");
        String medicationName = scanner.next();
        if (!Database.MEDICATION_BANK.inventory.containsKey(medicationName)) {
            System.out.println("Error: Medication not found in inventory.");
            return;
        }
        scanner.nextLine();
        System.out.print("Enter quantity to request: ");
        int requestedAmount = scanner.nextInt();
        Administrator admin = getAdmin();
        if (admin == null) {
            System.out.println("Replenishment request canceled.");
            return;
        }
        Medication medication = Database.MEDICATION_BANK.inventory.get(medicationName);
        if (medication.isStockLow()) {
            if (pharmacist.getRequests() == null) {
                pharmacist.setRequests(new ArrayList<>());
            }
            RefillRequest request = new RefillRequest(medicationName, requestedAmount, this.pharmacist, admin);
            pharmacist.getRequests().add(request);
            admin.receiveRefillRequest(request);
            System.out.println("Replenishment request submitted for " + medicationName);
            sendMessage(request);
        } else {
            System.out.println("Stock is sufficient for " + medicationName);
        }
    }

    /**
     * Sends a message to the administrator about the replenishment request.
     *
     * @param r The RefillRequest to send the message about
     */
    public void sendMessage(RefillRequest r) {
        String message = "Request at " + r.getRequestDate() + ": Refill Request for " + r.getMedication() + " of amount: " + r.getRequestedAmount();
        r.getAdmin().getMessages().add(message);
    }

    /**
     * Updates the prescription status for pending prescriptions.
     */
    public void updatePrescriptionStatus() {
        ArrayList<Appointment> pendingAppointments = new ArrayList<>();
        for (Patient patient : Database.PATIENTS) {
            for (Appointment apt : patient.getAppointments()) {
                if (apt.getPrescriptionStatus() == Status.PENDING) {
                    pendingAppointments.add(apt);
                }
            }
        }
        if (pendingAppointments.isEmpty()) {
            System.out.println("No appointments with pending prescription status found.");
            return;
        }
        System.out.println("Appointments with Pending Prescription Status:");
        for (Appointment apt : pendingAppointments) {
            AppointmentView.printAppointmentOutcome(apt);
        }
        System.out.println("Choose Appointment ID to update Prescription Status:");
        int apptChoice = pharmacistView.getChoice();
        Appointment selectedAppointment = null;
        for (Appointment apt : pendingAppointments) {
            if (apt.getAppointmentID() == apptChoice) {
                selectedAppointment = apt;
                break;
            }
        }
        if (selectedAppointment == null) {
            System.out.println("Invalid appointment ID selected.");
            return;
        }
        boolean dispensedSuccessfully = dispenseMedication(selectedAppointment);
        if (dispensedSuccessfully) {
            selectedAppointment.setPrescriptionStatus(Status.DISPENSED);
            System.out.println("Prescription status updated to DISPENSED for the selected appointment.");
        } else {
            System.out.println("Unable to update prescription status to DISPENSED due to dispensing issues.");
        }
    }

    /**
     * Attempts to dispense medication for an appointment and returns success status.
     *
     * @param appointment The appointment for which to dispense medication
     * @return true if all medications were dispensed successfully, false otherwise
     */
    private boolean dispenseMedication(Appointment appointment) {
        boolean allMedicationsDispensed = true;
        for (Medication prescribedMed : appointment.getPrescriptions()) {
            String medName = prescribedMed.getName();
            int requiredQuantity = prescribedMed.getDosage();
            if (!Database.MEDICATION_BANK.inventory.containsKey(medName)) {
                System.out.println("Medication " + medName + " is not available in the inventory.");
                allMedicationsDispensed = false;
                continue;
            }
            Medication inventoryMed = Database.MEDICATION_BANK.inventory.get(medName);
            if (inventoryMed.getStockLevel() < requiredQuantity) {
                System.out.println("Insufficient stock for " + medName + ". Unable to dispense.");
                allMedicationsDispensed = false;
            } else {
                inventoryMed.minusStock(requiredQuantity);
                System.out.println("Dispensed " + requiredQuantity + " units of " + medName + ". Updated stock level: " + inventoryMed.getStockLevel());
            }
        }
        return allMedicationsDispensed;
    }
}
