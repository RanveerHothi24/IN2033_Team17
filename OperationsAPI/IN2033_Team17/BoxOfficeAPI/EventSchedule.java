import java.time.LocalDateTime;

public class EventSchedule {
    private int eventID;
    private LocalDateTime eventDateTime;
    private String venueName;

    public EventSchedule(int eventID, LocalDateTime eventDateTime, String venueName) {
        this.eventID = eventID;
        this.eventDateTime = eventDateTime;
        this.venueName = venueName;
    }

    // Getters
    public int getEventID() {
        return eventID;
    }

    public LocalDateTime getEventDateTime() {
        return eventDateTime;
    }

    public String getVenueName() {
        return venueName;
    }

    // Setters
    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public void setEventDateTime(LocalDateTime eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    @Override
    public String toString() {
        return "EventSchedule{" +
                "EventID=" + eventID +
                ", DateTime=" + eventDateTime +
                ", Venue='" + venueName + '\'' +
                '}';
    }
}
