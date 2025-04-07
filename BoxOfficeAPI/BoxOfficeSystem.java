import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BoxOfficeOperationsImpl implements BoxOfficeOperations {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/in2033t17";
    private static final String DB_USER = "in2033t17_d";
    private static final String DB_PASSWORD = "qKvalvobOa0";

    @Override
    public List<EventSchedule> getEventSchedule() {
        List<EventSchedule> schedules = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT c.event_id, c.start_date_time, r.name AS venue_name " +
                 "FROM calendar c " +
                 "JOIN rooms r ON c.room_id = r.room_id " +
                 "ORDER BY c.start_date_time")) {

            while (rs.next()) {
                schedules.add(new EventSchedule(
                    rs.getInt("event_id"),
                    rs.getTimestamp("start_date_time").toLocalDateTime(),
                    rs.getString("venue_name")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving event schedule: " + e.getMessage());
        }
        return schedules;
    }

    @Override
    public List<Event> searchEventScheduleByDate(String startDate, String endDate) {
        List<Event> events = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT c.event_id, c.show_name, c.start_date_time, co.category " +
                 "FROM calendar c " +
                 "JOIN contracts co ON c.show_name = co.show_name " +
                 "WHERE c.start_date_time BETWEEN ? AND ? " +
                 "ORDER BY c.start_date_time")) {

            pstmt.setString(1, startDate + " 00:00:00");
            pstmt.setString(2, endDate + " 23:59:59");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                events.add(new Event(
                    rs.getInt("event_id"),
                    rs.getString("show_name"),
                    rs.getTimestamp("start_date_time").toLocalDateTime(),
                    rs.getString("category")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error searching event schedule by date: " + e.getMessage());
        }
        return events;
    }

    @Override
    public List<Event> searchEventScheduleByType(String eventType, int eventID) {
        List<Event> events = new ArrayList<>();
        String query = "SELECT c.event_id, c.show_name, c.start_date_time, co.category " +
                      "FROM calendar c " +
                      "JOIN contracts co ON c.show_name = co.show_name " +
                      "WHERE co.category = ?" +
                      (eventID != -1 ? " AND c.event_id = ?" : "") +
                      " ORDER BY c.start_date_time";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, eventType);
            if (eventID != -1) {
                pstmt.setInt(2, eventID);
            }
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                events.add(new Event(
                    rs.getInt("event_id"),
                    rs.getString("show_name"),
                    rs.getTimestamp("start_date_time").toLocalDateTime(),
                    rs.getString("category")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error searching event schedule by type: " + e.getMessage());
        }
        return events;
    }

    @Override
    public void notifyEventChanges(int eventID) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT show_name, room_id, start_date_time, end_date_time, status " +
                 "FROM calendar WHERE event_id = ?")) {

            pstmt.setInt(1, eventID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.printf(
                    "Event Change Notification - Event ID: %d, Show: %s, Room: %s, Start: %s, End: %s, Status: %s%n",
                    eventID,
                    rs.getString("show_name"),
                    rs.getString("room_id"),
                    rs.getTimestamp("start_date_time").toString(),
                    rs.getTimestamp("end_date_time").toString(),
                    rs.getString("status")
                );
            } else {
                System.out.println("Event ID " + eventID + " not found.");
            }
        } catch (SQLException e) {
            System.err.println("Error notifying event changes: " + e.getMessage());
        }
    }

    @Override
    public int getVenueCapacity(String hallID) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT capacity FROM rooms WHERE room_id = ?")) {

            pstmt.setString(1, hallID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("capacity");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving venue capacity: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public List<Show> getListOfAllShows() {
        List<Show> shows = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT show_id, show_name, start_day, " +
                 "TIMESTAMPDIFF(MINUTE, start_day, end_day) AS duration_minutes " +
                 "FROM contracts ORDER BY show_name")) {

            while (rs.next()) {
                shows.add(new Show(
                    rs.getInt("show_id"),
                    rs.getString("show_name"),
                    rs.getDate("start_day").toLocalDate().atStartOfDay(),
                    rs.getInt("duration_minutes")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving list of shows: " + e.getMessage());
        }
        return shows;
    }

    @Override
    public List<Show> getShowTimeList() {
        List<Show> shows = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                 "SELECT c.event_id, c.show_name, c.start_date_time, " +
                 "TIMESTAMPDIFF(MINUTE, c.start_date_time, c.end_date_time) AS duration_minutes " +
                 "FROM calendar c " +
                 "ORDER BY c.start_date_time")) {

            while (rs.next()) {
                shows.add(new Show(
                    rs.getInt("event_id"),
                    rs.getString("show_name"),
                    rs.getTimestamp("start_date_time").toLocalDateTime(),
                    rs.getInt("duration_minutes")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving show time list: " + e.getMessage());
        }
        return shows;
    }

    @Override
    public LocalDateTime getShowStartTime(int eventID) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT start_date_time FROM calendar WHERE event_id = ?")) {

            pstmt.setInt(1, eventID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getTimestamp("start_date_time").toLocalDateTime();
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving show start time: " + e.getMessage());
        }
        return null; // Indicate not found
    }

    @Override
    public int getShowDuration(int eventID) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT start_date_time, end_date_time FROM calendar WHERE event_id = ?")) {

            pstmt.setInt(1, eventID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                LocalDateTime start = rs.getTimestamp("start_date_time").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("end_date_time").toLocalDateTime();
                Duration duration = Duration.between(start, end);
                return (int) duration.toMinutes();
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving show duration: " + e.getMessage());
        }
        return -1;
    }
}
