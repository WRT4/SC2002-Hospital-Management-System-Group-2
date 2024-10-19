import java.util.ArrayList;

public class Administrator extends User {
    private ArrayList<User> staff;

    public Administrator(String id, String name) {
        super(id, name, "Administrator");
        this.staff = new ArrayList<>();
    }

    // Manage staff members (add/remove) with duplication check
    public void manageStaff(User staffMember, String action) {
        if (action.equals("add")) {
            if (!staff.contains(staffMember)) {
                staff.add(staffMember);
                System.out.println(staffMember.getName() + " added to the staff.");
            } else {
                System.out.println(staffMember.getName() + " is already part of the staff.");
            }
        } else if (action.equals("remove")) {
            if (staff.remove(staffMember)) {
                System.out.println(staffMember.getName() + " removed from the staff.");
            } else {
                System.out.println(staffMember.getName() + " is not part of the staff.");
            }
        }
    }

    // Lock or unlock a user account
    public void toggleAccountLock(User user, boolean lock) {
        if (lock) {
            user.lockAccount(); // Lock the account
            System.out.println(user.getName() + "'s account has been locked.");
        } else {
            user.unlockAccount(); // Unlock the account
            System.out.println(user.getName() + "'s account has been unlocked.");
        }
    }

    // Reset a staff member's password to the default
    public void resetPassword(User user) {
        user.changePassword("password");
        System.out.println(user.getName() + "'s password has been reset to the default.");
    }

    // View activity log of a specific staff member
    public void viewStaffActivityLog(User staffMember) {
        System.out.println("Activity Log for " + staffMember.getName() + ":");
        staffMember.displayActivityLog();
    }

    @Override
    public void showMenu() {
        System.out.println("Administrator Menu:");
        System.out.println("1. Manage Staff");
        System.out.println("2. Lock/Unlock Accounts");
        System.out.println("3. Reset Staff Passwords");
        System.out.println("4. View Staff Activity Logs");
        System.out.println("5. Logout");
    }
}
