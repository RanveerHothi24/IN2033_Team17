import java.time.LocalDateTime;

public class ShowTime {
    private LocalDateTime showStartTime;

    public ShowTime(LocalDateTime showStartTime) {
        this.showStartTime = showStartTime;
    }

    // Getter
    public LocalDateTime getShowStartTime() {
        return showStartTime;
    }

    // Setter
    public void setShowStartTime(LocalDateTime showStartTime) {
        this.showStartTime = showStartTime;
    }


    @Override
    public String toString() {
        return "ShowTime{" +
                "StartTime=" + showStartTime +
                '}';
    }
}
