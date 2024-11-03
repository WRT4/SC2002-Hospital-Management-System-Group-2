package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class User {
    protected String id;
    protected String password;
    protected String name;
    protected String role;
    private int failedLoginAttempts;
    private boolean isLocked;
    private LocalDate passwordLastChanged;
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static final int PASSWORD_EXPIRY_DAYS = 90;
    private List<String> activityLog;
    private List<Request> userRequests; // Track requests submitted by the user

    public User(String id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.password = "password"; // Default password
        this.failedLoginAttempts = 0;
        this.isLocked = false;
        this.passwordLastChanged = LocalDate.now(); // Password set to current date
        this.activityLog = new ArrayList<>();
        this.userRequests = new ArrayList<>();
    }

    // Login method with max attempts feature
    public boolean login(String enteredId, String enteredPassword) {
        if (isLocked) {
            logActivity("Login attempt failed - account is locked.");
            System.out.println("Account is locked. Please contact the administrator.");
            return false;
        }

        if (this.id.equals(enteredId) && this.password.equals(enteredPassword)) {
            if (isPasswordExpired()) {
                logActivity("Login successful but password expired.");
                System.out.println("Password expired. Please change your password.");
                return false;
            }
            resetFailedAttempts();
            logActivity("Login successful.");
            return true;
        } else {
            failedLoginAttempts++;
            logActivity("Login attempt failed.");
            if (failedLoginAttempts >= MAX_LOGIN_ATTEMPTS) {
                lockAccount();
            }
            return false;
        }
    }

    // Method to create a password reset request
    public void requestPasswordReset(Administrator admin) {
        PasswordResetRequest resetRequest = new PasswordResetRequest(this);
        userRequests.add(resetRequest);
        admin.receiveRequest(resetRequest);
        System.out.println("Password reset request sent to administrator.");
    }

    // Method to create an unlock account request
    public void requestAccountUnlock(Administrator admin) {
        UnlockAccountRequest unlockRequest = new UnlockAccountRequest(this);
        userRequests.add(unlockRequest);
        admin.receiveRequest(unlockRequest);
        System.out.println("Account unlock request sent to administrator.");
    }

    // Change password method with expiry reset
    public void changePassword(String newPassword) {
        this.password = newPassword;
        this.passwordLastChanged = LocalDate.now();
        logActivity("Password changed.");
        System.out.println("Password changed successfully.");
    }

    // Check if the password has expired
    private boolean isPasswordExpired() {
        return LocalDate.now().isAfter(passwordLastChanged.plusDays(PASSWORD_EXPIRY_DAYS));
    }

    // Lock account after max failed attempts
    public void lockAccount() {
        isLocked = true;
        logActivity("Account locked due to multiple failed login attempts.");
        System.out.println("Account locked. Please contact the administrator.");
    }

    // Unlock account
    public void unlockAccount() {
        isLocked = false;
        resetFailedAttempts();
        logActivity("Account unlocked.");
    }

    // Reset failed login attempts
    private void resetFailedAttempts() {
        failedLoginAttempts = 0;
    }

    // Log user activities
    private void logActivity(String message) {
        activityLog.add(LocalDate.now() + ": " + message);
    }

    // Display user activity log
    public void displayActivityLog() {
        System.out.println("Activity Log for " + name + ":");
        for (String log : activityLog) {
            System.out.println(log);
        }
    }

    // Abstract method for role-specific functionality
    public abstract void showMenu();

    public void logout() {
        logActivity("User logged out.");
        System.out.println("User logged out.");
    }

    public String getName() {
        return this.name;
    }
}
