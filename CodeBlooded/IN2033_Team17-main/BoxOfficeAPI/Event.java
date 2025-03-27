import java.time.LocalDateTime;

public class Event {
    private int eventID;
    private String eventName;
    private LocalDateTime eventDateTime;
    private String eventType;

    public Event(int eventID, String eventName, LocalDateTime eventDateTime, String eventType) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventDateTime = eventDateTime;
        this.eventType = eventType;
    }

    // Getters
    public int getEventID() {
        return eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public LocalDateTime getEventDateTime() {
        return eventDateTime;
    }

    public String getEventType() {
        return eventType;
    }

    // Setters
    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventDateTime(LocalDateTime eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "Event{" +
                "ID=" + eventID +
                ", Name='" + eventName + '\'' +
                ", DateTime=" + eventDateTime +
                ", Type='" + eventType + '\'' +
                '}';
    }
}
