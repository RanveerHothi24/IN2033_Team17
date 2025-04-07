package com.example.codeblooded;

import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingDAO {

    private static final String URL = "jdbc:mysql://sst-stuproj.city.ac.uk:3306/in2033t17";
    private static final String USER = "in2033t17_a";
    private static final String PASSWORD = "3cL1h1l3tY8";

    // insert bookings into db

    public void insertBooking(String room, LocalDate date, int startHour, int endHour, String company) {
        String query = "INSERT INTO bookings (room, date, start_hour, end_hour, company_name) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, room);
            stmt.setDate(2, Date.valueOf(date));
            stmt.setInt(3, startHour);
            stmt.setInt(4, endHour);
            stmt.setString(5, company);
            stmt.executeUpdate();
            System.out.println("Booking inserted successfully!");
        } catch (SQLException e) {
            System.out.println("Error inserting booking: " + e.getMessage());
        }
    }
    public Map<LocalDate, Integer> getRoomBookingCountsForMonth(YearMonth yearMonth) {
        Map<LocalDate, Integer> bookingCounts = new HashMap<>();
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();
        String query = "SELECT date, COUNT(DISTINCT room) as room_count " +
                "FROM bookings " +
                "WHERE date BETWEEN ? AND ? " +
                "GROUP BY date";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(start));
            stmt.setDate(2, Date.valueOf(end));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LocalDate date = rs.getDate("date").toLocalDate();
                int roomCount = rs.getInt("room_count");
                bookingCounts.put(date, roomCount);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching booking counts: " + e.getMessage());
        }
        return bookingCounts;
    }

    // New method to fetch bookings for a specific date
    public List<Booking> getBookingsForDate(LocalDate date) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT booking_id, room, start_hour, end_hour, company_name " +
                "FROM bookings " +
                "WHERE date = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int bookingId = rs.getInt("booking_id");
                String room = rs.getString("room");
                int startHour = rs.getInt("start_hour");
                int endHour = rs.getInt("end_hour");
                String company = rs.getString("company_name");
                bookings.add(new Booking(bookingId, room, startHour, endHour, company));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching bookings for date: " + e.getMessage());
        }
        return bookings;
    }
}