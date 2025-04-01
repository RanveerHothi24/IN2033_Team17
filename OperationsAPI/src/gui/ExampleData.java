package gui;

import api.Event;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ExampleData {

    private static Random r = new Random();

    // EVENT DATA
    private final static int[] eventIds = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
    public static final String[] eventNames = {"An Event","Some Event","Another Event","idk","Event Name"};
    private static final String[] eventTypes = {"Show","Film","Meeting"};

    private static final LocalDate[] startDates = new LocalDate[28];
    private static final LocalTime[] startTimes = new LocalTime[13];
    private static final LocalDateTime[] startDateTimes = new LocalDateTime[15];

    static {
        for (int i = 0; i < startDates.length; i++) {
            startDates[i] = LocalDate.now().plusDays(i);
        }
        for (int i = 0; i < startTimes.length; i++) {
            startTimes[i] = LocalTime.of(10,0).plusHours(i);
        }
        for (int i = 0; i < startDateTimes.length; i++) {
            startDateTimes[i] = LocalDateTime.of(startDates[r.nextInt(28)], startTimes[r.nextInt(13)]);
        }
    }

    public static String[] companyNames = {"Roberts-Bull Productions","Robinson and Sons Productions","Gordon and Sons Productions,Howard Ltd Productions"
    ,"Mason, Wright and Green Productions"
    ,"Houghton-Wright Productions"
    ,"Taylor-Faulkner Productions"
    ,"Norton Group Productions"
    ,"Payne-Lowe Productions"
    ,"Hughes Ltd Productions"
    ,"Graham-Smith Productions"
    ,"Smith Inc Productions"
    ,"Rose-Archer Productions"
    ,"Slater-Marshall Productions"
    ,"Palmer-Pearson Productions"
    ,"Humphries, Heath and Thompson Productions"
    ,"Howe-Harris Productions"
    ,"Bennett, Hughes and John Productions"
    ,"Clarke Inc Productions"
    ,"Turner-Stevens Productions"
    ,"Ward Inc Productions"
    ,"Bibi Inc Productions"
    ,"Mitchell-Webb Productions"
    ,"Turner, Young and Allan Productions"
    ,"Booth LLC Productions"
    ,"Bryan Group Productions"
    ,"Johnson Inc Productions"
    ,"Johnson, Fletcher and Page Productions"
    ,"Smith, Barnett and Smith Productions"
    ,"Evans, Richards and Webb Productions"
    ,"Chapman, Coles and Howard Productions"
    ,"James LLC Productions"
    ,"Burton-Matthews Productions"
    ,"White-Clark Productions"
    ,"Smith, Jackson and Richardson Productions"
    ,"Gibbs and Sons Productions"
    ,"Taylor, Thomas and Hunter Productions"
    ,"Atkins-Schofield Productions"
    ,"George-Whitehead Productions"
    ,"Smith-Bishop Productions"
    ,"Marshall and Sons Productions"
    ,"Duncan-Lewis Productions"
    ,"Lee, James and Russell Productions"
    ,"Barker-Conway Productions"
    ,"Craig, Gardner and Murphy Productions"
    ,"Wilson, Higgins and Butler Productions"
    ,"Hamilton Ltd Productions"
    ,"Perry, Davis and Lloyd Productions"
    ,"Chapman-Moss Productions"
    ,"Thomson, Holloway and Davies Productions"};


    private final static Duration[] durations = {Duration.ofMinutes(60),Duration.ofMinutes(120)};
    public final static int[] roomIds = {1,2,3,4,5,6,7,8};

    public static Event[] events = new Event[120];
    static {
        for (int i = 0; i < events.length; i++) {
            events[i] = new Event(
                    eventIds[r.nextInt(eventIds.length)],
                    eventNames[r.nextInt(eventNames.length)],
                    eventTypes[r.nextInt(eventTypes.length)],
                    startDateTimes[r.nextInt(startDateTimes.length)],
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
