public class Seat {
    private int seatID;
    private String location;
    private boolean isWheelchairAccessible;
    private boolean isRestrictedView;

    public Seat(int seatID, String location, boolean isWheelchairAccessible, boolean isRestrictedView) {
        this.seatID = seatID;
        this.location = location;
        this.isWheelchairAccessible = isWheelchairAccessible;
        this.isRestrictedView = isRestrictedView;
    }

    // Getters
    public int getSeatID() {
        return seatID;
    }

    public String getLocation() {
        return location;
    }

    public boolean isWheelchairAccessible() {
        return isWheelchairAccessible;
    }

    public boolean isRestrictedView() {
        return isRestrictedView;
    }

    // Setters
    public void setSeatID(int seatID) {
        this.seatID = seatID;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setWheelchairAccessible(boolean wheelchairAccessible) {
        isWheelchairAccessible = wheelchairAccessible;
    }

    public void setRestrictedView(boolean restrictedView) {
        isRestrictedView = restrictedView;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "ID=" + seatID +
                ", Location='" + location + '\'' +
                ", WheelchairAccessible=" + isWheelchairAccessible +
                ", RestrictedView=" + isRestrictedView +
                '}';
    }
}
