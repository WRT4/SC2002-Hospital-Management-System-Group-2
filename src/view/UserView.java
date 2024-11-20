package view;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents the view layer for user interactions in the Hospital Management System (HMS).
 * Handles input and output related to the user interface, including menus, message boxes, and user credentials.
 * @author Tan Wen Rong, Yeoh Kai Wen
 * @version 1.0
 * @since 2024-11-18
 */
public class UserView {

    protected Scanner scanner;
    public static final String RED = "\u001B[31m";  // ANSI escape code for red text
    public static final String RESET = "\u001B[0m"; // ANSI escape code to reset text formatting

    /**
     * Constructs a {@code UserView} object with the specified {@code Scanner}.
     *
     * @param scanner A {@code Scanner} object for capturing user input.
     */
    public UserView(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Prompts the user to enter a choice from the menu.
     * Ensures the input is a valid integer.
     *
     * @return The user's choice as an integer.
     */
    public int getChoice() {
        System.out.print("Enter choice: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Enter a number.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    /**
     * Prompts the user to enter a user ID.
     *
     * @return The entered user ID as a {@code String}.
     */
    public String enterID() {
        System.out.print("Enter User ID: ");
        return scanner.nextLine();
    }

    /**
     * Prompts the user to enter a password.
     *
     * @return The entered password as a {@code String}.
     */
    public String enterPassword() {
        System.out.print("Enter Password: ");
        return scanner.nextLine();
    }

    /**
     * Displays the user's inbox, including unread messages and message history.
     * Unread messages are highlighted in red.
     *
     * @param messages    A list of all messages.
     * @param unreadIndex The index marking the first unread message.
     * @return The total number of messages in the inbox.
     */
    public int viewInbox(ArrayList<String> messages, int unreadIndex) {
        System.out.println("Inbox:\n");

        // Display unread messages
        System.out.println("Unread Messages:");
        int counter = 0;
        for (int i = messages.size() - 1; i >= unreadIndex; i--) {
            System.out.println(RED + (i + 1) + ". UNREAD - " + messages.get(i) + RESET);
            System.out.println();
            counter++;
        }
        if (counter == 0) {
            System.out.println("No unread messages!\n");
        }

        // Display message history
        System.out.println("Message history:");
        int counter2 = 0;
        for (int i = unreadIndex - 1; i >= 0; i--) {
            System.out.println((i + 1) + ". " + messages.get(i));
            System.out.println();
            counter2++;
        }
        if (counter2 == 0) {
            System.out.println("No message history!\n");
        }
        return messages.size();
    }

    /**
     * Displays a message box indicating the number of unread messages.
     *
     * @param messages A list of all messages.
     * @param index    The index marking the first unread message.
     */
    public void showMessageBox(ArrayList<String> messages, int index) {
        System.out.println("\nMessage Box: ");
        int counter = messages.size() - index;
        if (counter == 0) {
            System.out.println("No unread messages!");
            return;
        }
        System.out.println(RED + "YOU HAVE " + counter + " UNREAD MESSAGES!" + RESET);
    }

    /**
     * Displays the main menu for the user, showing available actions.
     */
    public void showMenu() {
        System.out.println("\nMain Menu: ");
        System.out.println("1. Access Role-Specific Dashboard");
        System.out.println("2. Change Password");
        System.out.println("3. Logout");
        System.out.println("Choose an action:");
    }
}
