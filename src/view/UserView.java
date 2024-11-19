package view;

import java.util.ArrayList;
import java.util.Scanner;

public class UserView {
	protected Scanner scanner;
	public static final String RED = "\u001B[31m";
    public static final String RESET = "\u001B[0m";
    
    public UserView(Scanner scanner) {
		this.scanner = scanner;
	}
    
    public int getChoice() {
        System.out.print("Enter choice: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Enter a number.");
            scanner.next();
        }
        return scanner.nextInt();
    }
    
    public String enterID() {
    	System.out.print("Enter User ID: ");
        String inputID = scanner.nextLine();
        return inputID;
    }
    
    public String enterPassword() {
        System.out.print("Enter Password: ");
        String inputPassword = scanner.nextLine();
        return inputPassword;
    }
    
    public int viewInbox(ArrayList<String> messages, int unreadIndex) {
		System.out.println("Inbox:\n");
		int counter = 0;
		System.out.println("Unread Messages:");
		for (int i = messages.size()-1; i >= unreadIndex; i--) {
			System.out.println(RED + (i+1) + ". " +  "UNREAD - " + messages.get(i) + RESET);
			System.out.println();
			counter++;
		}
		if (counter == 0) System.out.println("No unread messages!\n");
		int counter2 = 0;
		System.out.println("Message history:");
		for (int i = unreadIndex - 1; i >= 0; i--) {
			System.out.println((i+1) + ". " + messages.get(i));
			System.out.println();
			counter++;
		}
		if (counter2 == 0) System.out.println("No message history!\n");
		return messages.size();
	}
	
	public void showMessageBox(ArrayList<String> messages, int index) {
		System.out.println("\nMessage Box: ");
		int counter = 0;
		counter = messages.size() - index;
		if (counter ==0) {
			System.out.println("No unread messages!");
			return;
		}
		System.out.println(RED + "YOU HAVE " + counter + " UNREAD MESSAGES!" + RESET);
	}

	public void showMenu() {
    	System.out.println("\nMain Menu: ");
        System.out.println("1. Access Role-Specific Dashboard");
        System.out.println("2. Change Password");
        System.out.println("3. Logout");
        System.out.println("Choose an action:");
    }
}
