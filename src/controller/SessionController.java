package controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Abstract base class for session controllers.
 * Manages common properties and methods used by specific session controllers.
 * Provides an abstract method for displaying the menu.
 * @author: Tan Wen Rong
 * @version: 1.0
 * @since: 2024-11-18
 */
public abstract class SessionController {
    
    protected Scanner scanner;
    protected int unreadIndex;
    
    // session variables, useful for logging
    protected LocalDate startDate;
    protected LocalTime startTime;
    
    /**
     * Displays the main menu for the session.
     * This method must be implemented by subclasses to define specific menu behavior.
     */
    public abstract void showMenu();
    
    /**
     * Formatter to display time in HH:mm:ss format.
     */
    protected static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
}
