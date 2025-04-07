package com.example.codeblooded;

import java.time.LocalDate;

public class Booking {
    private int bookingId;
    private LocalDate date;
    private String room;
    private int startHour;
    private int endHour;
    private String companyName;

    public Booking(int bookingId, LocalDate date, String room, int startHour, int endHour, String companyName) {
        this.bookingId = bookingId;
        this.date = date;
        this.room = room;
        this.startHour = startHour;
        this.endHour = endHour;
        this.companyName = companyName;
    }

    public int getBookingId() { return bookingId; }
    public LocalDate getDate() { return date; }
    public String getRoom() { return room; }
    public int getStartHour() { return startHour; }
    public int getEndHour() { return endHour; }
    public String getCompany() { return companyName; }
}