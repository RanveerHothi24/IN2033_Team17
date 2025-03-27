package api;

import java.util.Date;

public class Booking {
    private int bookingId;
    private int roomId;
    private int eventId;
    private String status;
    private Date bookingDate;
    private Date confirmationDeadline;

    public Booking(int bookingId, int roomId, int eventId, String status, Date bookingDate, Date confirmationDeadline) {
        this.bookingId = bookingId;
        this.roomId = roomId;
        this.eventId = eventId;
        this.status = status;
        this.bookingDate = bookingDate;
        this.confirmationDeadline = confirmationDeadline;
    }
    public int getBookingId() {
        return bookingId;
    }
    public int getRoomId() {
        return roomId;
    }
    public int getEventId() {
        return eventId;
    }
    public String getStatus() {
        return status;
    }
    public Date getBookkingDate() {
        return bookingDate;
    }
    public Date getConfirmationDeadline() {
        return confirmationDeadline;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setBookingDate(Date bookkingDate) {
        this.bookingDate = bookkingDate;
    }
    public void setConfirmationDeadline(Date confirmationDeadline) {
        this.confirmationDeadline = confirmationDeadline;
    }
}
