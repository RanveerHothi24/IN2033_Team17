package com.example.codeblooded;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import javafx.scene.control.*;

public class TimelineView {
    private Stage stage;
    private LocalDate date;
    private Scene backScene;
    private final String[] rooms = {"Main Hall", "Small Hall", "Rehearsal Space"};
    private final int[] hours = {11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};

    public TimelineView(Stage stage, LocalDate date, Scene backScene) {
        this.stage = stage;
        this.date = date;
        this.backScene = backScene;
    }

    public Scene getView() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        // Header with date and buttons
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #222222;");

        Label dateLabel = new Label("Timeline for " + date.toString());
        dateLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");

        Button viewDailySheetButton = new Button("View Daily Sheet");
        Button backToCalendarButton = new Button("Back to Calendar");
        backToCalendarButton.setOnAction(e -> stage.setScene(backScene));

        header.getChildren().addAll(dateLabel, viewDailySheetButton, backToCalendarButton);
        root.setTop(header);

        // Timeline Grid (moved to center for better layout)
        GridPane timelineGrid = createTimelineGrid();
        root.setCenter(timelineGrid);
        BorderPane.setAlignment(timelineGrid, Pos.CENTER);
        BorderPane.setMargin(timelineGrid, new Insets(20));

        // Bottom Section
        HBox bottomSection = createBottomSection();
        root.setBottom(bottomSection); // Note: Fix this typo to bottomSection
        BorderPane.setMargin(bottomSection, new Insets(20));

        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/com/example/codeblooded/styles.css").toExternalForm());

        // Set action for View Daily Sheet button
        viewDailySheetButton.setOnAction(e -> {
            DailySheetView dailySheetView = new DailySheetView(stage, date, scene);
            stage.setScene(dailySheetView.getView());
        });

        return scene;
    }

    // Create the timeline grid (Top Half)
    private GridPane createTimelineGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setAlignment(Pos.CENTER);

        for (int i = 0; i < hours.length; i++) {
            Label hourLabel = new Label(hours[i] + ":00");
            hourLabel.getStyleClass().add("timeline-hour-label");
            GridPane.setHalignment(hourLabel, javafx.geometry.HPos.CENTER);
            grid.add(hourLabel, i + 1, 0);
        }

        for (int row = 0; row < rooms.length; row++) {
            Label roomLabel = new Label(rooms[row]);
            roomLabel.getStyleClass().add("timeline-room-label");
            grid.add(roomLabel, 0, row + 1);

            for (int col = 0; col < hours.length; col++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(80, 40);
                cell.getStyleClass().add("timeline-cell");
                grid.add(cell, col + 1, row + 1);
            }
        }
        return grid;
    }

    // Create the bottom section (Contract, Booking, Financial)
    private HBox createBottomSection() {
        HBox bottom = new HBox(20);
        bottom.setAlignment(Pos.CENTER);
        bottom.getStyleClass().add("bottom-section");

        // Contract Tab (Left)
        VBox contractTab = createContractTab();
        HBox.setHgrow(contractTab, Priority.ALWAYS);

        // Booking Slot (Center)
        VBox bookingSlot = createBookingSlot();
        HBox.setHgrow(bookingSlot, Priority.ALWAYS);

        // Financial Report (Right)
        VBox financialReport = createFinancialReport();
        HBox.setHgrow(financialReport, Priority.ALWAYS);

        bottom.getChildren().addAll(contractTab, bookingSlot, financialReport);
        return bottom;
    }

    // Contract Tab (Bottom Left)
    private VBox createContractTab() {
        VBox contractTab = new VBox(10);
        contractTab.setAlignment(Pos.TOP_CENTER);
        contractTab.getStyleClass().add("section-box");

        Label title = new Label("Contract Rates");
        title.getStyleClass().add("section-title");

        Label mainHallLabel = new Label("Main Hall:");
        mainHallLabel.getStyleClass().add("section-subtitle");
        Label mainHallHourly = new Label("  Hourly: £325 + VAT (10:00-17:00, Mon-Fri)");
        mainHallHourly.getStyleClass().add("section-text");
        Label mainHallEvening = new Label("  Evening: £1,850 + VAT (17:00-00:00, Mon-Thu)");
        mainHallEvening.getStyleClass().add("section-text");
        Label mainHallDaily = new Label("  Daily: £3,800 + VAT (10:00-00:00, Mon-Thu)");
        mainHallDaily.getStyleClass().add("section-text");

        Label smallHallLabel = new Label("Small Hall:");
        smallHallLabel.getStyleClass().add("section-subtitle");
        Label smallHallHourly = new Label("  Hourly: £225 + VAT (10:00-17:00, Mon-Fri)");
        smallHallHourly.getStyleClass().add("section-text");
        Label smallHallEvening = new Label("  Evening: £950 + VAT (17:00-00:00, Mon-Thu)");
        smallHallEvening.getStyleClass().add("section-text");
        Label smallHallDaily = new Label("  Daily: £2,200 + VAT (10:00-00:00, Mon-Thu)");
        smallHallDaily.getStyleClass().add("section-text");

        Label rehearsalLabel = new Label("Rehearsal Space:");
        rehearsalLabel.getStyleClass().add("section-subtitle");
        Label rehearsalHourly = new Label("  Hourly: £60 + VAT (10:00-17:00, Mon-Fri)");
        rehearsalHourly.getStyleClass().add("section-text");
        Label rehearsalDaily = new Label("  Daily: £240 + VAT (10:00-17:00, Mon-Fri)");
        rehearsalDaily.getStyleClass().add("section-text");

        contractTab.getChildren().addAll(
                title,
                mainHallLabel, mainHallHourly, mainHallEvening, mainHallDaily,
                smallHallLabel, smallHallHourly, smallHallEvening, smallHallDaily,
                rehearsalLabel, rehearsalHourly, rehearsalDaily
        );

        return contractTab;
    }

    // Booking Slot (Bottom Center)
    private VBox createBookingSlot() {
        VBox bookingSlot = new VBox(15);
        bookingSlot.setAlignment(Pos.CENTER);
        bookingSlot.getStyleClass().add("section-box");

        Label title = new Label("Book a Slot");
        title.getStyleClass().add("section-title");

        ComboBox<String> roomCombo = new ComboBox<>();
        roomCombo.getItems().addAll(rooms);
        roomCombo.setPromptText("Select Room");
        roomCombo.getStyleClass().add("custom-combo");

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select Date");
        datePicker.getStyleClass().add("custom-date-picker");

        ComboBox<Integer> startTimeCombo = new ComboBox<>();
        for (int h = 11; h <= 23; h++) {
            startTimeCombo.getItems().add(h);
        }
        startTimeCombo.setPromptText("Start Time");
        startTimeCombo.getStyleClass().add("custom-combo");

        ComboBox<Integer> endTimeCombo = new ComboBox<>();
        for (int h = 12; h <= 24; h++) {
            endTimeCombo.getItems().add(h);
        }
        endTimeCombo.setPromptText("End Time");
        endTimeCombo.getStyleClass().add("custom-combo");

        TextField companyField = new TextField();
        companyField.setPromptText("Company Name");
        companyField.getStyleClass().add("custom-text-field");

        Button bookButton = new Button("Book");
        bookButton.getStyleClass().add("book-button");
        bookButton.setOnAction(e -> {
            String room = roomCombo.getValue();
            LocalDate date = datePicker.getValue();
            Integer start = startTimeCombo.getValue();
            Integer end = endTimeCombo.getValue();
            String company = companyField.getText();

            if (room != null && date != null && start != null && end != null && !company.isEmpty()) {
                System.out.println("Booking for " + room + " on " + date + " from " + start + ":00 to " + end + ":00 by " + company);
            } else {
                System.out.println("Please fill all fields");
            }
        });

        bookingSlot.getChildren().addAll(title, roomCombo, datePicker, startTimeCombo, endTimeCombo, companyField, bookButton);
        return bookingSlot;
    }

    // Financial Report (Bottom Right)
    private VBox createFinancialReport() {
        VBox financialReport = new VBox(10);
        financialReport.setAlignment(Pos.TOP_CENTER);
        financialReport.getStyleClass().add("section-box");

        Label title = new Label("Financial Report");
        title.getStyleClass().add("section-title");

        Label overviewLabel = new Label("Sample Rates Overview:");
        overviewLabel.getStyleClass().add("section-subtitle");
        Label mainHallRate = new Label("Main Hall Daily (Mon-Thu): £3,800 + VAT");
        mainHallRate.getStyleClass().add("section-text");
        Label smallHallRate = new Label("Small Hall Evening (Mon-Thu): £950 + VAT");
        smallHallRate.getStyleClass().add("section-text");
        Label rehearsalRate = new Label("Rehearsal Space Hourly: £60 + VAT");
        rehearsalRate.getStyleClass().add("section-text");
        Label pendingLabel = new Label("(Detailed report pending booking data)");
        pendingLabel.getStyleClass().add("section-text-italic");

        financialReport.getChildren().addAll(
                title,
                overviewLabel, mainHallRate, smallHallRate, rehearsalRate, pendingLabel
        );

        return financialReport;
    }
}