package com.example.codeblooded;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import java.util.*;

public class DayViewController {

    @FXML
    private FlowPane legendPane;

    @FXML
    private GridPane timeslotGrid;

    private Map<String, Color> companyColors = new HashMap<>();
    private List<Booking> bookings;

    // Method to set bookings (called after room and day selection)
    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
        initializeView();
    }

    private void initializeView() {
        if (bookings == null) return;

        // Step 1: Identify unique companies and assign colors
        Set<String> companies = new HashSet<>();
        for (Booking booking : bookings) {
            companies.add(booking.getCompany());
        }
        List<String> companyList = new ArrayList<>(companies);
        Collections.sort(companyList); // Sort for consistency

        List<Color> colors = Arrays.asList(
                Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE
        ); // Add more colors if needed
        for (int i = 0; i < companyList.size(); i++) {
            companyColors.put(companyList.get(i), colors.get(i % colors.size()));
        }

        // Step 2: Populate the legend
        for (String company : companyList) {
            Color color = companyColors.get(company);
            Rectangle rect = new Rectangle(20, 20, color);
            Label label = new Label(company);
            label.setPadding(new Insets(0, 0, 0, 5));
            HBox item = new HBox(5, rect, label);
            legendPane.getChildren().add(item);
        }

        // Step 3: Set up the grid structure
        timeslotGrid.getColumnConstraints().clear();
        for (int i = 0; i < 13; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100.0 / 13);
            timeslotGrid.getColumnConstraints().add(col);
        }
        timeslotGrid.getRowConstraints().clear();
        timeslotGrid.getRowConstraints().add(new RowConstraints()); // Header row
        timeslotGrid.getRowConstraints().add(new RowConstraints()); // Booking row

        // Add hour labels (11 to 23)
        for (int h = 11; h <= 23; h++) {
            Text hourLabel = new Text(String.valueOf(h));
            GridPane.setHalignment(hourLabel, javafx.geometry.HPos.CENTER);
            timeslotGrid.add(hourLabel, h - 11, 0);
        }

        // Step 4: Determine booked hours
        List<String> bookedCompanies = new ArrayList<>(Collections.nCopies(13, null));
        for (Booking booking : bookings) {
            for (int h = booking.getStartHour(); h < booking.getEndHour(); h++) {
                int index = h - 11;
                if (index >= 0 && index < 13) {
                    bookedCompanies.set(index, booking.getCompany());
                }
            }
        }

        // Step 5: Populate the grid with cells
        for (int i = 0; i < 13; i++) {
            StackPane cell = new StackPane();
            cell.setPrefHeight(50); // Adjust height as needed
            String company = bookedCompanies.get(i);
            if (company != null) {
                Color color = companyColors.get(company);
                cell.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
                String finalCompany = company; // For lambda
                cell.setOnMouseClicked(event -> {
                    System.out.println("Booked by: " + finalCompany);
                    // Add more display logic here later (e.g., label or dialog)
                });
            } else {
                cell.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
            }
            timeslotGrid.add(cell, i, 1);
        }
    }
}