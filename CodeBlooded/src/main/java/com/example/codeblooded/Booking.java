package com.example.codeblooded;

public class Booking {
    private int startHour;  // e.g., 11 for 11:00
    private int endHour;    // e.g., 13 for 13:00 (exclusive end)
    private String company; // Name of the company

    public Booking(int startHour, int endHour, String company) {
        this.startHour = startHour;
        this.endHour = endHour;
        this.company = company;
    }

    public int getStartHour() { return startHour; }
    public int getEndHour() { return endHour; }
    public String getCompany() { return company; }
}