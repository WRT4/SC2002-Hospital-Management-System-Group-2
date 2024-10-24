package model;


import java.time.LocalDate;
import java.time.LocalTime;

public class TimeSlot {
    private LocalTime startTime;
	private LocalTime endTime;
    private boolean isOccupied;

    public TimeSlot(LocalTime startTime, LocalTime endTime){
        this.startTime = startTime; 
        this.endTime = startTime.plusMinutes(15);
    }

    public LocalTime getStartTime(){
        return startTime;
    }

    public LocalTime getEndTime(){
        return endTime;
    }

    public boolean getOccupied(){
        return isOccupied;
    }

    public void printTimeSlot(){
        System.out.println(startTime + "-" + endTime);
    }
    
    public String getTimeSlot(){
        return (startTime + "-" + endTime);
    }

	public void setStartTime(LocalTime time) {
		this.startTime = time;
		this.endTime = time.plusMinutes(15);
	}
}
