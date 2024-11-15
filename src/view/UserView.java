package view;

import java.util.ArrayList;
import java.util.Scanner;

import application.Database;
import model.User;

public class UserView {
	protected Scanner scanner;
	protected final String RED = "\u001B[31m";
    protected final String RESET = "\u001B[0m";
    
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
    
    /**
     * Displays the activity log for the user (placeholder implementation).
     */
    public void displayActivityLog(User user) {
        System.out.println("Displaying activity log for user ID: " + user.getID());
        // Placeholder: In a real system, this would display the user's actual activity log.
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
		for (int i = messages.size() - 1; i >= index; i--){
			System.out.println(RED + "UNREAD - " + messages.get(i) + RESET);
			System.out.println();
			counter++;
		}
		if (counter ==0)
			System.out.println("No unread messages!");
	}

	public void showMenu() {
    	System.out.println("\nMain Menu: ");
        System.out.println("1. Access Role-Specific Dashboard");
        System.out.println("2. Change Password");
        System.out.println("3. Display Activity Log");
        System.out.println("4. Logout");
        System.out.println("Choose an action:");
    }
	
	public static User getUser(Scanner scanner) {
		System.out.print("Enter User ID (-1 to exit): ");
        User user = null;
        while (user == null) {
            String userID = scanner.nextLine();
            if (userID.equals("-1")) {
                return null; 
            }
            user = Database.getUser(userID);
            if (user == null) {
                System.out.print("Invalid User ID. Please try again: ");
            }
        }
        return user;
	}
}
