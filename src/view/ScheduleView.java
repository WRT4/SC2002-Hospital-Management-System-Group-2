package view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.InputMismatchException;
import java.util.Scanner;
import model.Appointment;
import model.Schedule;
import model.TimeSlot;
import enums.Status;

/**
 * Provides methods to interact with the scheduling system,
 * including inputting dates and times, and viewing available or all slots.
 * @author: Hoo Jing Huan, Tan Wen Rong
 * @version: 1.0
 * @since: 2024-11-18
 */
public class ScheduleView {

    /**
     * Prompts the user to input a date, ensuring the date is within valid ranges and at most 30 days in advance.
     *
     * @param scanner The Scanner object for user input
     * @return The input date, or null if the user exits
     */
    public static LocalDate inputDate(Scanner scanner) {
        String dayStr, monthStr, yearStr;
        int day = 0, month = 0, year = 0;
        LocalDate finalDate = null;
        System.out.println("\nEntering date...\n");
        while (true) {
            try {
                System.out.println("Enter Day (01-31) or -1 to exit: ");
                dayStr = scanner.next();
                day = Integer.parseInt(dayStr);
                if (day == -1) return null;
                if (day < 1 || day > 31) {
                    throw new RuntimeException("Invalid input for Day! Please enter a valid day between 01 and 31.");
                }
                
                System.out.println("Enter Month (01-12) or -1 to exit: ");
                monthStr = scanner.next();
                month = Integer.parseInt(monthStr);
                if (month == -1) return null;
                if (month < 1 || month > 12) {
                    throw new RuntimeException("Invalid input for Month! Please enter a valid month between 01 and 12.");
                }
                
                System.out.println("Enter Year (Current year - Current year + 1) or -1 to exit: ");
                yearStr = scanner.next();
                year = Integer.parseInt(yearStr);
                if (year == -1) return null;
                if (year < LocalDate.now().getYear() || year > LocalDate.now().getYear() + 1) {
                    throw new RuntimeException("Invalid input for Year! Please enter a valid year.");
                }
                
                // Construct the final date
                finalDate = LocalDate.of(year, month, day);
                
                // System check: patient cannot schedule for more than 30 days in advance
                if (ChronoUnit.DAYS.between(LocalDate.now(), finalDate) > 30){
                    throw new RuntimeException("At most 30 days in advance. Please re-enter. ");
                }
                
                break; // Exit the loop if all inputs are valid

            } catch (InputMismatchException e) {
                System.out.println("Wrong input type! Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                scanner.nextLine(); // Clear the input
            } catch (Exception e) {
                System.out.println("Error! Try Again!");
                scanner.nextLine(); // Clear the input
            }
        }
        return finalDate;
    }

    /**
     * Prompts the user to input a time, ensuring the time is within valid ranges and after 8:00 AM.
     *
     * @param scanner The Scanner object for user input
     * @return The input time, or null if the user exits
     */
    public static LocalTime inputTime(Scanner scanner) {
        String hourStr, minStr;
        int hour = 0, min = 0;
        LocalTime finalTime = null;
        System.out.println("\nEntering time...\n");
        while (true) {
            try {
                System.out.println("Enter Hour (00-23) or -1 to exit: ");
                hourStr = scanner.next();
                hour = Integer.parseInt(hourStr);
                if (hour == -1) return null;
                if (hour < 0 || hour > 23) {
                    throw new RuntimeException("Invalid input for Hour! Please enter a valid hour between 00 and 23.");
                }
                
                System.out.println("Enter Minute (00-59) or -1 to exit: ");
                minStr = scanner.next();
                min = Integer.parseInt(minStr);
                if (min == -1) return null;
                if (min < 0 || min > 59) {
                    throw new RuntimeException("Invalid input for Minute! Please enter a valid minute between 00 and 59.");
                }

                // System check: time cannot be before 8:00 AM
                finalTime = LocalTime.of(hour, min);
                if (finalTime.isBefore(LocalTime.of(8, 0))) {
                    System.out.println("First appointment slot is at 8am! Unable to perform operation!");
                    System.out.println("Please re-enter.");
                } else break; // Exit the loop if all inputs are valid
            } catch (InputMismatchException e) {
                System.out.println("Wrong input type! Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                scanner.nextLine(); // Clear the input
            } catch (Exception e) {
                System.out.println("Error! Try Again!");
                scanner.nextLine(); // Clear the input
            }
        }
        return finalTime;
    }
    
    /**
     * Displays all time slots for a given date, indicating whether they are free or occupied.
     *
     * @param date The date to view slots for
     * @param schedule The schedule containing the time slots
     */
    public static void viewAllSlots(LocalDate date, Schedule schedule) {
        String occupied = "";
        for (TimeSlot timeSlot : schedule.getWorkingSlots()) {
            if (timeSlot.getDate().equals(date)) {
                if (!timeSlot.getOccupied()) {
                    occupied = "FREE";
                } else {
                    Appointment temp = schedule.findAppointment(timeSlot);
                    if (temp != null && temp.getStatus() != Status.CANCELLED) {
                        occupied = temp.toString();
                    } else {
                        occupied = "BUSY";
                    }
                }
                System.out.println(timeSlot + ": " + occupied);
                System.out.println();
            }
        }
    }
    
    /**
     * Displays all available (free) time slots for a given date.
     *
     * @param date The date to view available slots for
     * @param schedule The schedule containing the time slots
     */
    public static void viewAvailableSlots(LocalDate date, Schedule schedule) {
        System.out.println("Free slots on " + date + " :");
        for (TimeSlot timeSlot : schedule.getWorkingSlots()) {
            if (!timeSlot.getOccupied() && timeSlot.getDate().equals(date)) {
                timeSlot.printTime();
            }
        }
    }
}
