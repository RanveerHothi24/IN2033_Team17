package api;

import java.util.Date;

public class Review {
    private int reviewId;
    private int eventId;
    private int rating;
    private String comment;
    private Date date;

    public Review(int reviewId, int eventId, int rating, String comment, Date date) {
        this.reviewId = reviewId;
        this.eventId = eventId;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
    }
    public int getReviewId() {
        return reviewId;
    }
    public int getEventId() {
        return eventId;
    }
    public int getRating() {
        return rating;
    }
    public String getComment() {
        return comment;
    }
    public Date getDate() {
        return date;
    }
    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public void setDate(Date date) {
        this.date = date;
    }
}
