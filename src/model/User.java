// User.java
package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class User {
    protected String id;
    protected String password;
    protected String name;
    protected String role;
    private LocalDate passwordLastChanged;
    private List<String> activityLog;

    public User(String id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.password = "password"; // Default password
        this.passwordLastChanged = LocalDate.now(); // Password set to current date
        this.activityLog = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public LocalDate getPasswordLastChanged() {
        return passwordLastChanged;
    }

    public void setPassword(String password) {
        this.password = password;
        this.passwordLastChanged = LocalDate.now();
    }

    public List<String> getActivityLog() {
        return activityLog;
    }

    // Log user activities
    public void logActivity(String message) {
        activityLog.add(LocalDate.now() + ": " + message);
    }

    // Abstract method for role-specific functionality
    public abstract void showMenu();
}
