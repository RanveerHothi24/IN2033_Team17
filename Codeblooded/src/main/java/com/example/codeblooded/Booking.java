package com.example.codeblooded;

public class Booking {
    private String room;
    private int startHour;
    private int endHour;
    private String company;

    private int bookingId;

    public Booking(int bookingId, String room, int startHour, int endHour, String company) {
          this.bookingId = bookingId;
        this.room = room;
        this.startHour = startHour;
        this.endHour = endHour;
        this.company = company;
    }

    public String getRoom() { return room; }
    public int getStartHour() { return startHour; }
    public int getEndHour() { return endHour; }
    public String getCompany() { return company; }

    public int getBookingId() {
        return bookingId;
    }
}