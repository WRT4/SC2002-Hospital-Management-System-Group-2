package controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;


public abstract class SessionController {
	protected Scanner scanner;
    protected int unreadIndex;
    protected LocalDate startDate;
    protected LocalTime startTime;
}
