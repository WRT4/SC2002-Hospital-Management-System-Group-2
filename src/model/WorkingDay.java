package model;
import java.java.time.*;

public class WorkingDay {
    private String dayName; 
    private ArrayList<TimeSlot> workingSlot; 

    public WorkingDay(String dayName, LocalTime workStartTime, LocalTime workEndTime){
        this.dayName = dayName; 
        //create 15-min time slots
        workingSlot = new ArrayList<>();
        createWorkingTimeSlot(workStartTime, workEndTime);
    }

    public void createWorkingTimeSlot(LocalTime workStartTime, LocalTime workEndTime){
        LocalTime currentTime = workStartTime;
        while (currentTime.isBefore(workEndTime)){
            workingSlot.add(TimeSlot(currentTime, currentTime.plusMinutes(30)));
            currentTime = currentTime.plusMinutes(30);
        }
    }

    public String getDay(){
        return dayName;
    }

    public ArrayList<TimeSlot> getWorkingSlot(){
        return workingSlot;
    }
}
