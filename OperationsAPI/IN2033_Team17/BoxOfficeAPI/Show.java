import java.time.LocalDateTime;

public class Show {
    private int showID;
    private String showName;
    private LocalDateTime showStartTime;
    private int durationMinutes;

    public Show(int showID, String showName, LocalDateTime showStartTime, int durationMinutes) {
        this.showID = showID;
        this.showName = showName;
        this.showStartTime = showStartTime;
        this.durationMinutes = durationMinutes;
    }

    // Getters
    public int getShowID() {
        return showID;
    }

    public String getShowName() {
        return showName;
    }

    public LocalDateTime getShowStartTime() {
        return showStartTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    // Setters
    public void setShowID(int showID) {
        this.showID = showID;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public void setShowStartTime(LocalDateTime showStartTime) {
        this.showStartTime = showStartTime;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    @Override
    public String toString() {
        return "Show{" +
                "ID=" + showID +
                ", Name='" + showName + '\'' +
                ", StartTime=" + showStartTime +
                ", Duration=" + durationMinutes + " mins" +
                '}';
    }
}


