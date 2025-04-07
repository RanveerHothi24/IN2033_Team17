import java.time.LocalDateTime;

/**
 * Represents an available time slot in a venue.
 */
public class TimeSlot {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String roomId;

    public TimeSlot(LocalDateTime startTime, LocalDateTime endTime, String roomId) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomId = roomId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        return "TimeSlot{" +
               "roomId='" + roomId + '\'' +
               ", startTime=" + startTime +
               ", endTime=" + endTime +
               '}';
    }
}