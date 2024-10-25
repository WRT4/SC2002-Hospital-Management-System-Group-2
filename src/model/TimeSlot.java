package model;


import java.time.LocalDate;
import java.time.LocalTime;

public class TimeSlot {
    private LocalDate date;
    private LocalTime startTime;
	private LocalTime endTime;
    private boolean isOccupied;

    public TimeSlot(LocalDate date, LocalTime startTime, LocalTime endTime){
        this.date = date;
        this.startTime = startTime;
        this.endTime = startTime.plusMinutes(15);
    }
    public LocalDate getDate(){
        return date;
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

    public void freeSlot(){
        isOccupied = false;
        System.out.println("Time Slot <<" + printTimeSlot() + " >> is emptied.");
    }
    
    public String printTimeSlot(){
        return "Date: " + date + ", " + startTime + "-" + endTime;
    }

	public void setStartTime(LocalTime time) {
		this.startTime = time;
		this.endTime = time.plusMinutes(15);
	}

    //check if this timeslot overlaps with another timeslot (other)
    public boolean isOverlap (TimeSlot other){
        //if not on the same day, not overlapping
        if (!date.equals(other.date))
            return false;
        if (startTime.isBefore(other.endTime) && other.startTime.isBefore(endTime))
            return true;
    }
}
