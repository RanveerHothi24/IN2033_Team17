import java.time.LocalDateTime;
import java.util.*;

public class BoxOfficeSystem implements BoxOfficeOperations {
    // Sample data storage
    private Map<Integer, Double> ticketPrices;
    private Map<Integer, Event> events;
    private Map<Integer, List<Seat>> seatingAvailability;
    private Map<Integer, Integer> venueCapacity;
    private List<EventSchedule> eventSchedules;
    private List<Show> showList;

    public BoxOfficeSystem() {
        // Initialize with hypothetical data
        ticketPrices = new HashMap<>();
        ticketPrices.put(101, 50.0);
        ticketPrices.put(102, 75.0);

        // Creating Event Objects
        events = new HashMap<>();
        events.put(101, new Event(101, "Concert", LocalDateTime.of(2025, 2, 18, 19, 0), "Music"));
        events.put(102, new Event(102, "Theatre", LocalDateTime.of(2025, 2, 18, 18, 0), "Drama"));

        // Seating Availability
        seatingAvailability = new HashMap<>();
        seatingAvailability.put(101, Arrays.asList(
                new Seat(1, "Row A, Seat 1", false, false),
                new Seat(2, "Row A, Seat 2", true, false)
        ));
        seatingAvailability.put(102, Arrays.asList(
                new Seat(3, "Row B, Seat 1", false, true),
                new Seat(4, "Row B, Seat 2", false, false)
        ));

        // Venue Capacity
        venueCapacity = new HashMap<>();
        venueCapacity.put(1, 370);
        venueCapacity.put(2, 285);

        // Event Schedules
        eventSchedules = Arrays.asList(
                new EventSchedule(101, LocalDateTime.of(2025, 2, 18, 19, 0), "Main Hall"),
                new EventSchedule(102, LocalDateTime.of(2025, 2, 18, 18, 0), "Small Hall")
        );

        // Show List
        showList = Arrays.asList(
                new Show(101, "Concert", LocalDateTime.of(2025, 2, 18, 19, 0), 120),
                new Show(102, "Theatre", LocalDateTime.of(2025, 2, 18, 18, 0), 90)
        );
    }

    @Override
    public double getUpdatedTicketPrice(int eventID) {
        return ticketPrices.getOrDefault(eventID, -1.0);
    }

    @Override
    public List<Seat> getSeatingAvailability(int eventID) {
        return seatingAvailability.getOrDefault(eventID, new ArrayList<>());
    }

    @Override
    public List<EventSchedule> getEventSchedule() {
        return eventSchedules;
    }

    @Override
    public List<Event> searchEventScheduleByDate(String date) {
        List<Event> filteredEvents = new ArrayList<>();
        for (Event event : events.values()) {
            if (event.getEventDateTime().toLocalDate().toString().equals(date)) {
                filteredEvents.add(event);
            }
        }
        return filteredEvents;
    }

    @Override
    public List<Event> searchEventScheduleByType(String eventType, int eventID) {
        List<Event> filteredEvents = new ArrayList<>();
        for (Event event : events.values()) {
            if (event.getEventType().equalsIgnoreCase(eventType)) {
                filteredEvents.add(event);
            }
        }
        return filteredEvents;
    }

    @Override
    public void notifyEventChanges(int eventID) {
        System.out.println("Notification: Event " + eventID + " has been rescheduled.");
    }

    @Override
    public int getVenueCapacity(int hallID) {
        return venueCapacity.getOrDefault(hallID, 0);
    }

    @Override
    public List<Show> getListOfAllShows() {
        return showList;
    }

    @Override
    public List<ShowTime> getShowTimeList() {
        List<ShowTime> showTimes = new ArrayList<>();
        for (Show show : showList) {
            showTimes.add(new ShowTime(show.getShowStartTime()));
        }
        return showTimes;
    }

    @Override
    public LocalDateTime getShowStartTime(int eventID) {
        return events.getOrDefault(eventID, new Event(0, "Unknown", LocalDateTime.now(), "Unknown")).getEventDateTime();
    }

    @Override
    public int getShowDuration(int eventID) {
        for (Show show : showList) {
            if (show.getShowID() == eventID) {
                return show.getDurationMinutes();
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        BoxOfficeSystem system = new BoxOfficeSystem();

        System.out.println("Updated Ticket Price: " + system.getUpdatedTicketPrice(101));
        System.out.println("Seating Availability: " + system.getSeatingAvailability(101));
        System.out.println("Event Schedule: " + system.getEventSchedule());
        System.out.println("Search by Date: " + system.searchEventScheduleByDate("2025-02-18"));
        System.out.println("Search by Type: " + system.searchEventScheduleByType("Music", 101));

        system.notifyEventChanges(101);

        System.out.println("Venue Capacity: " + system.getVenueCapacity(1));
        System.out.println("Show List: " + system.getListOfAllShows());
        System.out.println("Show Time List: " + system.getShowTimeList());
        System.out.println("Show Start Time: " + system.getShowStartTime(101));
        System.out.println("Show Duration: " + system.getShowDuration(101));
    }
}
