package model;
import java.time.LocalDate;
import java.time.LocalTime;
public class testScheduleTime {
    public static void main(String[] args) {
        // Create a new schedule for testing
        Schedule testSchedule = new Schedule("Dr123");
        testSchedule.getWorkingSlots().get(5).setOccupied();
        System.out.println(testSchedule.getWorkingSlots().get(5));

        testSchedule.viewAvailableSlot();   

        // TimeSlot testSlot = new TimeSlot(LocalDate.now(), LocalTime.now(), LocalTime.now().plusMinutes(30));
        // System.out.println(testSlot);

    }
}
