package model;

import java.util.Scanner;
import java.time.*;

import java.util.Scanner;

public class testPatientDoctorApp {

    public static void main(String[] args) {
        // Initialize database with sample data
        Database.doctors.add(new Doctor("1", "Wen Rong"));
        Database.patients.add(new Patient("1", "Jing Huan", new MedicalRecord()));

        Scanner sc = new Scanner(System.in);

        while (true) {
            int choice = 0;
            System.out.println("1. Doctor \n2. Patient \n3. Exit \nEnter Choice: ");
            //try {
                choice = sc.nextInt();
                
                // Handle user choices
                switch (choice) {
                    case 1:
                        Database.doctors.getFirst().showMenu();
                        break;
                    case 2:
                        Database.patients.getFirst().showMenu();
                        break;
                    //case 3:
                        //System.out.println("Exiting the application.");
                        
                        //sc.close(); // Close the scanner before exiting
                        //return; // Exit the main method
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
