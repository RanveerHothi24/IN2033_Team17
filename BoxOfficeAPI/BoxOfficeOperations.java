import java.time.LocalDateTime;
import java.util.List;

public interface BoxOfficeOperations {

    /**
     * Retrieves the updated ticket price for an event.
     * @param eventID The ID of the event.
     * @return The ticket price, or -1.0 if not found.
     */
    double getUpdatedTicketPrice(int eventID);

    /**
     * Retrieves the seating availability for an event.
     * @param eventID The ID of the event.
     * @return A list of available seats.
     */
    List<Seat> getSeatingAvailability(int eventID);

    /**
     * Retrieves the full event schedule including show dates, times, and venues.
     * @return A list of event schedules.
     */
    List<EventSchedule> getEventSchedule();

    /**
     * Allows Box Office to search the event schedule within a specific date range.
     * @param startDate The start date of the range (format: YYYY-MM-DD).
     * @param endDate The end date of the range (format: YYYY-MM-DD).
     * @return A list of events within the date range.
     */
    List<Event> searchEventScheduleByDate(String startDate, String endDate);

    /**
     * Allows Box Office to search the event schedule by event type.
     * @param eventType The type of event (e.g., "LIVE SHOW", "COMEDY").
     * @param eventID Optional event ID to filter a specific event (can be -1 if not used).
     * @return A list of events matching the event type.
     */
    List<Event> searchEventScheduleByType(String eventType, int eventID);

    /**
     * Notifies Box Office of event changes (e.g., reschedules or cancellations).
     * @param eventID The ID of the event that has changed.
     */
    void notifyEventChanges(int eventID);

    /**
     * Retrieves the capacity of a venue.
     * @param hallID The ID of the hall (room_id in the database).
     * @return The capacity of the venue, or 0 if not found.
     */
    int getVenueCapacity(String hallID);

    /**
     * Retrieves a list of all shows scheduled.
     * @return A list of all shows.
     */
    List<Show> getListOfAllShows();

    /**
     * Retrieves a list of all shows with their scheduled times.
     * @return A list of all shows including their start times and durations.
     */
    List<Show> getShowTimeList();

    /**
     * Retrieves the start time of a specific show.
     * @param eventID The ID of the event.
     * @return The start time of the show, or null if not found.
     */
    LocalDateTime getShowStartTime(int eventID);

    /**
     * Retrieves the duration of a specific show.
     * @param eventID The ID of the event.
     * @return The duration of the show in minutes, or -1 if not found.
     */
    int getShowDuration(int eventID);
}
