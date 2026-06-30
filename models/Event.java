package models;

import java.time.LocalDateTime;

public class Event {
    private int eventId;
    private EventType eventType;
    private String specialEventDetails;
    private LocalDateTime eventDate;
    private int groupId;


    public Event(int eventId, EventType eventType, String specialEventDetails, LocalDateTime eventDate, int groupId){
        this.eventId = eventId;
        this.eventType = eventType;
        this.specialEventDetails = specialEventDetails;
        this.eventDate = eventDate;
        this.groupId = groupId;
    }

    public int getEventId() {
        return eventId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getSpecialEventDetails() {
        return specialEventDetails;
    }

    public void setSpecialEventDetails(String specialEventDetails) {
        this.specialEventDetails = specialEventDetails;
    }
}
