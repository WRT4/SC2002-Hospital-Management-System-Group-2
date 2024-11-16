package controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


public abstract class SessionController {
	protected Scanner scanner;
    protected int unreadIndex;
    protected LocalDate startDate;
    protected LocalTime startTime;
    public abstract void showMenu();
 // Formatter to display time in HH:mm:ss format
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
}
