package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

import controller.SessionController;

/**
 * Represents a user in the Hospital Management System (HMS).
 * This class provides login functionality, password management, and role-based access.
 * It serves as a base class for different types of users in the system, such as admins, doctors, or patients.
 * It also handles user account locking, message management, and first-time login scenarios.
 * @author Hoo Jing Huan, Lee Kuan Rong, Lim Wee Keat, Tan Wen Rong, Yeoh Kai Wen
 * @version 1.0
 * @since 2024-11-18
 */
public abstract class User implements Serializable{

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
     * Constructs a User with an ID, name, and role. Sets the password to a default value ("password").
     * Also marks the user as having a first-time login and initializes message list.
     *
     * @param id The unique identifier for the user.
     * @param name The name of the user.
     * @param role The role of the user (e.g., admin, doctor, patient).
     */
    public User(String id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.setPassword("password"); // Default password
        this.setFirstLogin(true); // Default to true for new users
        this.messages = new ArrayList<String>();
        this.unreadIndex = 0;
    }

    /**
     * Constructs a User with an ID, name, password, and role.
     * Initializes the user's account, including setting a password.
     *
     * @param id The unique identifier for the user.
     * @param name The name of the user.
     * @param password The password of the user.
     * @param role The role of the user (e.g., admin, doctor, patient).
     */
    public User(String id, String name, String password, String role) {
        this(id, name, role);
        this.setPassword(password);
    }

    /**
     * Checks if the user's account is locked.
     *
     * @return True if the account is locked, otherwise false.
     */
    public boolean isLocked() {
        return isLocked;
    }

    /**
     * Sets the locked status of the user's account.
     *
     * @param locked True if the account should be locked, otherwise false.
     */
    public void setLocked(boolean locked) {
        this.isLocked = locked;
    }

    /**
     * Gets the index of the next unread message for the user.
     *
     * @return The unread message index.
     */
    public int getUnreadIndex() {
        return this.unreadIndex;
    }

    /**
     * Sets the unread message index to the given value.
     *
     * @param i The new unread message index.
     */
    public void setUnreadIndex(int i) {
        this.unreadIndex = i;
    }

    /**
     * Gets the role of the user.
     *
     * @return The role of the user (e.g., admin, doctor, patient).
     */
    public String getRole() {
        return this.role;
    }

    /**
     * Gets the unique ID of the user.
     *
     * @return The unique identifier for the user.
     */
    public String getID() {
        return this.id;
    }

    /**
     * Sets the password for the user.
     *
     * @param newPassword The new password to be set for the user.
     */
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    /**
     * Checks if this is the user's first login.
     *
     * @return True if this is the user's first login, otherwise false.
     */
    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    /**
     * Sets whether this is the user's first login.
     *
     * @param isFirstLogin True if this is the user's first login, otherwise false.
     */
    public void setFirstLogin(boolean isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    /**
     * Gets the password of the user.
     *
     * @return The user's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the list of messages for the user.
     *
     * @return A list of messages associated with the user.
     */
    public ArrayList<String> getMessages(){
        return messages;
    }

    /**
     * Creates and returns a session controller for managing user sessions.
     * This method should be implemented by subclasses to define specific user session behaviors.
     *
     * @param scanner A Scanner object for user input during session creation.
     * @return A SessionController object for managing the user's session.
     */
    public abstract SessionController createController(Scanner scanner);

    /**
     * Unlocks the user's account, setting the locked status to false.
     */
    public void unLock() {
        this.isLocked = false;
    }

    /**
     * Displays the user information, including ID and role.
     */
    public void displayUserInfo() {
        System.out.println("User ID: " + id);
        System.out.println("Role: " + role);
    }
}
