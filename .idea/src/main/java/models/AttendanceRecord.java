package models;

import java.time.LocalDateTime;

public class AttendanceRecord {
    private int attendanceId;
    private int eventId;
    private int personId;
    private LocalDateTime checkInTime;
    private String status; // e.g., "PRESENT", "ABSENT"

    // Constructor for creating a new record before it has a DB auto-generated ID
    public AttendanceRecord(int eventId, int personId, String status, LocalDateTime checkInTime) {
        this.eventId = eventId;
        this.personId = personId;
        this.status = status;
        this.checkInTime = checkInTime;
    }

    // Constructor for pulling complete records out of the database
    public AttendanceRecord(int attendanceId, int eventId, int personId, String status, LocalDateTime checkInTime) {
        this.attendanceId = attendanceId;
        this.eventId = eventId;
        this.personId = personId;
        this.status = status;
        this.checkInTime = checkInTime;
    }


    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AttendanceRecord{" +
                "attendanceId=" + attendanceId +
                ", eventId=" + eventId +
                ", personId=" + personId +
                ", checkInTime=" + checkInTime +
                ", status='" + status + '\'' +
                '}';
    }
}