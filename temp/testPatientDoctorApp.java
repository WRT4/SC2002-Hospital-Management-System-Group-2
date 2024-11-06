package model;

import java.util.Scanner;
import java.time.*;

import java.util.Scanner;

public class testPatientDoctorApp {

    public static void main(String[] args) {
        // Initialize database with sample data
        Database.doctors.add(new Doctor("1", "Wen Rong"));
        Database.doctors.add(new Doctor("2", "Kai Wen"));
        Database.patients.add(new Patient("1", "Jing Huan", new MedicalRecord()));
        Database.patients.add(new Patient("2", "Shane", new MedicalRecord()));

        Scanner sc = new Scanner(System.in);

        while (true) {
            int choice = 0;
            System.out.println("1. Doctor \n2. Patient \n3. Exit \nEnter Choice: ");
            //try {
                choice = sc.nextInt();
                
                // Handle user choices
                switch (choice) {
                    case 1:
                        for (Doctor doctor: Database.doctors){
                            System.out.println(doctor);
                        }
                        int doctorChoice = 0;
                        System.out.println("Choose Doctor ID");
                        doctorChoice = sc.nextInt();
                        if (doctorChoice !=1 && doctorChoice !=2 ){
                            System.out.println("Invalid Input of doctor");
                        }
                        Database.doctors.get(doctorChoice-1).showMenu();
                        break;
                    case 2:
                        for (Patient patient: Database.patients){
                            System.out.println(patient);
                        }
                        int patientChoice = 0;
                        System.out.println("Choose Patient ID");
                        patientChoice = sc.nextInt();
                        if (patientChoice !=1 && patientChoice !=2 ){
                            System.out.println("Invalid Input of patient");
                        }
                        Database.patients.get(patientChoice-1).showMenu();
                        break;
                    case 3:
                        System.out.println("Exiting the application.");
                        
                        sc.close(); // Close the scanner before exiting
                        return; // Exit the main method
                    default:
                        System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                        break;
                }
            //} catch (Exception e) {
           //     System.out.println("Error occured.");
            //    sc.nextLine(); // Consume the invalid input to avoid infinite loop
           // }
        }
    }
}
