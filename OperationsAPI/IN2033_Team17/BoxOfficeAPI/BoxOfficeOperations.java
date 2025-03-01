import java.time.LocalDateTime;
import java.util.List;

public interface BoxOfficeOperations {
    double getUpdatedTicketPrice(int eventID);
    List<Seat> getSeatingAvailability(int eventID);
    List<EventSchedule> getEventSchedule();
    List<Event> searchEventScheduleByDate(String date);
    List<Event> searchEventScheduleByType(String eventType, int eventID);
    void notifyEventChanges(int eventID);
    int getVenueCapacity(int hallID);
    List<Show> getListOfAllShows();
    List<ShowTime> getShowTimeList();
    LocalDateTime getShowStartTime(int eventID);
    int getShowDuration(int eventID);
}