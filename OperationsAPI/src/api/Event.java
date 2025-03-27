package api;

import java.time.Duration;
import java.time.LocalDateTime;

public class Event {
    private int eventId;
    private String eventName;
    private String eventType;
    private LocalDateTime eventStartTime;
    private Duration eventDuration;
    private int roomId;

    public Event(int eventId, String eventName, String eventType, LocalDateTime eventStartTime, Duration eventDuration, int roomId) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventType = eventType;
        this.eventStartTime = eventStartTime;
        this.eventDuration = eventDuration;
        this.roomId = roomId;
    }

    // GETTER
    public int getEventId() {
        return eventId;
    }
    public String getEventName() {
        return eventName;
    }
    public String getEventType() {
        return eventType;
    }
    public LocalDateTime getEventStartTime() {
        return eventStartTime;
    }
    public Duration getEventDuration() {
        return eventDuration;
    }
    public int getRoomId() {
        return roomId;
    }

    // SETTER
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    public void setEventStartTime(LocalDateTime eventStartTime) {
        this.eventStartTime = eventStartTime;
    }
    public void setEventDuration(Duration eventDuration) {
        this.eventDuration = eventDuration;
    }
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}
