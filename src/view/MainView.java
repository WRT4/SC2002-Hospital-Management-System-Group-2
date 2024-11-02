package view;

import model.*;
import java.util.Scanner;

public class MainView {
    public static void main(String[] args) {
        Administrator admin = new Administrator("admin1", "Admin User");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the MVC Application");

        while (true) {
            System.out.println("1. Login as Administrator");
            System.out.println("2. Login as Doctor");
            System.out.println("3. Login as Patient");
            System.out.println("4. Login as Pharmacist");
            System.out.println("5. Exit");
            System.out.print("Select option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> new AdminView(admin).display();
                case 2 -> new DoctorView(new Doctor("doc1", "Doctor User")).display();
                case 3 -> new PatientView(new Patient("pat1", "Patient User", new MedicalRecord()), admin).display();
                case 4 -> new PharmacistView(new Pharmacist("pharm1", "Pharmacist User", new MedicationBank()), admin).display();
                case 5 -> {
                    System.out.println("Exiting the application...");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }
}
