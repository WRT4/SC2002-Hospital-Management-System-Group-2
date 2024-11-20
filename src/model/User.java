package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

import controller.SessionController;

/**
 * Abstract base class for a user in the Hospital Management System (HMS).
 * Provides core user attributes and functionalities such as password management,
 * messaging, and role-based access. Serves as a base class for specific user types.
 * @author Lim Wee Keat, Tan Wen Rong, Yeoh Kai Wen
 * @version 1.0
 * @since 2024-11-18
 */
public abstract class User implements Serializable {

    private static final long serialVersionUID = 1L;
    protected String id;
    private String password;
    protected String role;
    private boolean isFirstLogin;
    protected String name;
    protected ArrayList<String> messages;
    protected int unreadIndex;
    private boolean isLocked = false;

    /**
     * Constructs a {@code User} with the specified ID, name, and role.
     * The user is initialized with a default password, an unlocked account,
     * and first login status set to true.
     *
     * @param id   The unique ID of the user.
     * @param name The name of the user.
     * @param role The role of the user (e.g., "Patient", "Doctor").
     */
    public User(String id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.setPassword("password"); // Default password
        this.isLocked = false;
        this.setFirstLogin(true); // Default to true for new users
        this.messages = new ArrayList<>();
        this.unreadIndex = 0;
    }

    /**
     * Constructs a {@code User} with the specified ID, name, password, and role.
     * This constructor calls the main constructor and then sets the provided password.
     *
     * @param id       The unique ID of the user.
     * @param name     The name of the user.
     * @param password The password for the user.
     * @param role     The role of the user (e.g., "Patient", "Doctor").
     */
    public User(String id, String name, String password, String role) {
        this(id, name, role);
        this.setPassword(password);
    }

    /**
     * Displays basic user information, including their ID and role.
     */
    public void displayUserInfo() {
        System.out.println("User ID: " + id);
        System.out.println("Role: " + role);
    }

    /**
     * Retrieves the name of the user.
     *
     * @return The name of the user.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Checks if the user's account is locked.
     *
     * @return {@code true} if the account is locked, {@code false} otherwise.
     */
    public boolean isLocked() {
        return isLocked;
    }

    /**
     * Locks or unlocks the user's account.
     *
     * @param locked {@code true} to lock the account, {@code false} to unlock it.
     */
    public void setLocked(boolean locked) {
        this.isLocked = locked;
    }

    /**
     * Retrieves the index of the first unread message in the user's message list.
     *
     * @return The index of the first unread message.
     */
    public int getUnreadIndex() {
        return this.unreadIndex;
    }

    /**
     * Sets the index of the first unread message.
     *
     * @param i The new unread index.
     */
    public void setUnreadIndex(int i) {
        this.unreadIndex = i;
    }

    /**
     * Retrieves the role of the user.
     *
     * @return The role of the user.
     */
    public String getRole() {
        return this.role;
    }

    /**
     * Retrieves the ID of the user.
     *
     * @return The unique ID of the user.
     */
    public String getID() {
        return this.id;
    }

    /**
     * Sets a new password for the user.
     *
     * @param newPassword The new password.
     */
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    /**
     * Checks if this is the user's first login.
     *
     * @return {@code true} if this is the first login, {@code false} otherwise.
     */
    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    /**
     * Updates the first login status for the user.
     *
     * @param isFirstLogin {@code true} to indicate this is the first login, {@code false} otherwise.
     */
    public void setFirstLogin(boolean isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    /**
     * Retrieves the user's password.
     * 
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Retrieves the list of messages associated with the user.
     *
     * @return An {@code ArrayList} of messages.
     */
    public ArrayList<String> getMessages() {
        return messages;
    }

    /**
     * Creates a session controller for the user.
     * This method is abstract and must be implemented by subclasses to provide role-specific functionality.
     *
     * @param scanner A {@code Scanner} instance for user input.
     * @return A {@code SessionController} for managing the user's session.
     */
    public abstract SessionController createController(Scanner scanner);

    /**
     * Unlocks the user's account.
     */
    public void unLock() {
        this.isLocked = false;
    }
}
