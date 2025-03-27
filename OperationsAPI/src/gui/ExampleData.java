package gui;

import api.Event;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Random;

public class ExampleData {

    private static Random r = new Random();

    // EVENT DATA
    private final static int[] eventIds = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
    private static final String[] eventNames = {"An Event","Some Event","Another Event","idk","Event Name"};
    private static final String[] eventTypes = {"Show","Film","Meeting"};

    private static final LocalDate[] startDates = new LocalDate[28];
    private static final LocalTime[] startTimes = new LocalTime[13];
    private static final LocalDateTime[] startDatetimes = new LocalDateTime[15];

    static {
        for (int i = 0; i < startDates.length; i++) {
            startDates[i] = LocalDate.now().plusDays(i);
        }
        for (int i = 0; i < startTimes.length; i++) {
            startTimes[i] = LocalTime.of(10,0).plusHours(i);
        }
        for (int i = 0; i < startDatetimes.length; i++) {
            startDatetimes[i] = LocalDateTime.of(startDates[r.nextInt(28)], startTimes[r.nextInt(13)]);
        }
    }

    private final static Duration[] durations = {Duration.ofMinutes(60),Duration.ofMinutes(120)};
    public final static int[] roomIds = {1,2,3,4,5,6,7,8};

    public static Event[] events = new Event[60];
    static {
        for (int i = 0; i < events.length; i++) {
            events[i] = new Event(
                    eventIds[r.nextInt(eventIds.length)],
                    eventNames[r.nextInt(eventNames.length)],
                    eventTypes[r.nextInt(eventTypes.length)],
                    startDatetimes[r.nextInt(startDatetimes.length)],
                    durations[r.nextInt(durations.length)],
                    roomIds[r.nextInt(roomIds.length)]);
        }
    }

    // BOOKING DATA
    private int[] bookingIds;
    // eventIds
    // roomIds
    private String[] statuses;
    private Date[] bookingDates;
    private Date[] confirmationDeadlines;


    // CLIENT DATA
    private int[] clientIds;
    private String[] clientNames;
    private String[] contactInfos;


    // REVIEW DATA
    private int[] reviewIds;
    // eventIds
    private int[] ratings;
    private String[] comments;
    private Date[] reviewDates;
}
