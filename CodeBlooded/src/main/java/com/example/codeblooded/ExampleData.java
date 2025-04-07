package com.example.codeblooded;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ExampleData {
    private final static String[] companyNames = {
            "Roberts-Bull Productions",
            "Robinson and Sons Productions",
            "Gordon and Sons Productions",
            "Howard Ltd Productions",
            "Mason, Wright and Green Productions",
            "Houghton-Wright Productions",
            "Taylor-Faulkner Productions",
            "Norton Group Productions",
            "Payne-Lowe Productions",
            "Hughes Ltd Productions",
            "Graham-Smith Productions",
            "Smith Inc Productions",
            "Rose-Archer Productions",
            "Slater-Marshall Productions",
            "Palmer-Pearson Productions",
            "Humphries, Heath and Thompson Productions",
            "Howe-Harris Productions",
            "Bennett, Hughes and John Productions",
            "Clarke Inc Productions",
            "Turner-Stevens Productions",
            "Ward Inc Productions",
            "Bibi Inc Productions",
            "Mitchell-Webb Productions",
            "Turner, Young and Allan Productions",
            "Booth LLC Productions",
            "Bryan Group Productions",
            "Johnson Inc Productions",
            "Johnson, Fletcher and Page Productions",
            "Smith, Barnett and Smith Productions",
            "Evans, Richards and Webb Productions",
            "Chapman, Coles and Howard Productions",
            "James LLC Productions",
            "Burton-Matthews Productions",
            "White-Clark Productions",
            "Smith, Jackson and Richardson Productions",
            "Gibbs and Sons Productions",
            "Taylor, Thomas and Hunter Productions",
            "Atkins-Schofield Productions",
            "George-Whitehead Productions",
            "Smith-Bishop Productions",
            "Marshall and Sons Productions",
            "Duncan-Lewis Productions",
            "Lee, James and Russell Productions",
            "Barker-Conway Productions",
            "Craig, Gardner and Murphy Productions",
            "Wilson, Higgins and Butler Productions",
            "Hamilton Ltd Productions",
            "Perry, Davis and Lloyd Productions",
            "Chapman-Moss Productions",
            "Thomson, Holloway and Davies Productions"
    };
    private final static Random r = new Random();

    private final static String[] rooms = {"Main Hall","Small Hall","Rehearsal Space"};

    public final static List<Booking> bookings = new ArrayList<>();
    private static final LocalDate today = LocalDate.now();
    static {
        for (int i = 0; i < 180; i++) {
            bookings.add(new Booking(i, today.plusDays(i), rooms[r.nextInt(3)], 10, 12, companyNames[r.nextInt(companyNames.length)]));
        }
    }
}
