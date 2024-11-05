// UserController.java
package controller;

import model.User;
import java.time.LocalDate;

public class UserController {
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private static final int PASSWORD_EXPIRY_DAYS = 90;

    public boolean login(User user, String enteredId, String enteredPassword) {
        if (user.isLocked()) {
            user.logActivity("Login attempt failed - account is locked.");
            System.out.println("Account is locked. Please contact the administrator.");
            return false;
        }

        if (user.getId().equals(enteredId) && user.getPassword().equals(enteredPassword)) {
            if (isPasswordExpired(user)) {
                user.logActivity("Login successful but password expired.");
                System.out.println("Password expired. Please change your password.");
                return false;
            }
            resetFailedAttempts(user);
            user.logActivity("Login successful.");
            return true;
        } else {
            incrementFailedAttempts(user);
            user.logActivity("Login attempt failed.");
            return false;
        }
    }

    public void changePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        user.logActivity("Password changed.");
        System.out.println("Password changed successfully.");
    }

    private boolean isPasswordExpired(User user) {
        return LocalDate.now().isAfter(user.getPasswordLastChanged().plusDays(PASSWORD_EXPIRY_DAYS));
    }

    public void lockAccount(User user) {
        user.setLocked(true);
        user.logActivity("Account locked due to multiple failed login attempts.");
        System.out.println("Account locked. Please contact the administrator.");
    }

    public void unlockAccount(User user) {
        user.setLocked(false);
        resetFailedAttempts(user);
        user.logActivity("Account unlocked.");
    }

    private void resetFailedAttempts(User user) {
        user.setFailedLoginAttempts(0);
    }

    private void incrementFailedAttempts(User user) {
        user.incrementFailedLoginAttempts();
        if (user.getFailedLoginAttempts() >= MAX_LOGIN_ATTEMPTS) {
            lockAccount(user);
        }
    }
}
