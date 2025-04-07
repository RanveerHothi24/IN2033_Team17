package com.example.codeblooded;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.*;

public class TimelineView {
    private final String[] rooms = {"Main Hall", "Small Hall", "Rehearsal Space"};
    private final int[] hours = {11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};
    private Stage stage;
    private Scene calendarScene;
    private LocalDate selectedDate;
    private List<Booking> bookings;
    private Map<String, Color> companyColors = new HashMap<>();
    private VBox contractDetailsSection;
    private BorderPane root;

    public TimelineView(Stage stage, Scene calendarScene, LocalDate selectedDate) {
        this.stage = stage;
        this.calendarScene = calendarScene;
        this.selectedDate = selectedDate;
        this.bookings = new BookingDAO().getBookingsForDate(selectedDate);
    }

    public Scene createScene() {
        assignCompanyColors();
        root = new BorderPane();
        root.getStyleClass().add("root");

        Button backButton = new Button("Back to Calendar");
        backButton.setOnAction(e -> stage.setScene(calendarScene));
        HBox topBar = new HBox(backButton);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10));
        root.setTop(topBar);

        GridPane timelineGrid = createTimelineGrid();
        root.setCenter(timelineGrid);
        BorderPane.setAlignment(timelineGrid, Pos.CENTER);
        BorderPane.setMargin(timelineGrid, new Insets(20));

        HBox bottomSection = createBottomSection();
        root.setBottom(bottomSection);
        BorderPane.setMargin(bottomSection, new Insets(20));

        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/com/example/codeblooded/styles.css").toExternalForm());
        return scene;
    }

    private GridPane createTimelineGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(0);
        grid.setVgap(1);
        grid.setAlignment(Pos.CENTER);

        // Add hour labels
        for (int i = 0; i < hours.length; i++) {
            Label hourLabel = new Label(hours[i] + ":00");
            hourLabel.getStyleClass().add("timeline-hour-label");
            GridPane.setHalignment(hourLabel, HPos.CENTER);
            grid.add(hourLabel, i + 1, 0);
        }

        // Add room labels
        for (int row = 0; row < rooms.length; row++) {
            Label roomLabel = new Label(rooms[row]);
            roomLabel.getStyleClass().add("timeline-room-label");
            grid.add(roomLabel, 0, row + 1);
        }

        // Populate grid with bookings
        for (Booking booking : bookings) {
            int startColumn = booking.getStartHour() - 11;
            int endColumn = booking.getEndHour() - 11;
            int roomIndex = Arrays.asList(rooms).indexOf(booking.getRoom());

            if (roomIndex != -1 && startColumn >= 0 && startColumn < 13 && endColumn > startColumn) {
                Color color = companyColors.get(booking.getCompany());
                for (int col = startColumn; col < endColumn; col++) {
                    StackPane bookingBlock = new StackPane();
                    bookingBlock.getStyleClass().add("booking-block");
                    bookingBlock.setStyle("-fx-background-color: " + toHex(color) + ";");
                    bookingBlock.setOnMouseClicked(e -> {
                        ContractDAO contractDAO = new ContractDAO();
                        Client client = contractDAO.getContractDetails(booking.getCompany());
                        if (client != null) {
                            updateContractDetailsSection(client);
                        } else {
                            contractDetailsSection.getChildren().clear();
                            Label title = new Label("Contract Details");
                            title.getStyleClass().add("section-title");
                            Label message = new Label("No contract details found for " + booking.getCompany());
                            message.getStyleClass().add("section-text");
                            contractDetailsSection.getChildren().addAll(title, message);
                        }
                    });
                    grid.add(bookingBlock, col + 1, roomIndex + 1, 1, 1);
                }
            }
        }

        // Fill empty cells
        for (int row = 0; row < rooms.length; row++) {
            for (int col = 0; col < hours.length; col++) {
                int finalRow = row;
                int finalCol = col;
                if (grid.getChildren().stream().noneMatch(node ->
                        GridPane.getRowIndex(node) == finalRow + 1 && GridPane.getColumnIndex(node) == finalCol + 1)) {
                    StackPane cell = new StackPane();
                    cell.getStyleClass().add("timeline-cell");
                    grid.add(cell, col + 1, row + 1);
                }
            }
        }

        return grid;
    }

    private HBox createBottomSection() {
        HBox bottom = new HBox(20);
        bottom.setAlignment(Pos.CENTER);
        bottom.getStyleClass().add("bottom-section");

        // Company list section
        ContractDisplay.clearCompanyList();
        for (Booking booking : bookings) {
            Color color = companyColors.get(booking.getCompany());
            String timeSlot = booking.getStartHour() + ":00 - " + booking.getEndHour() + ":00";
            ContractDisplay.addCompanyToList(booking.getCompany(), timeSlot, color);
        }
        VBox companyListSection = ContractDisplay.getCompanyListBox();
        companyListSection.setAlignment(Pos.TOP_CENTER);
        companyListSection.getStyleClass().add("section-box");
        Label companyListTitle = new Label("Companies Booked");
        companyListTitle.getStyleClass().add("section-title");
        companyListSection.getChildren().add(0, companyListTitle);

        // Contract details section
        contractDetailsSection = new VBox(10);
        contractDetailsSection.setAlignment(Pos.TOP_CENTER);
        contractDetailsSection.getStyleClass().add("section-box");
        Label contractTitle = new Label("Contract Details");
        contractTitle.getStyleClass().add("section-title");
        Label selectMessage = new Label("Select a booking to view contract details");
        selectMessage.getStyleClass().add("section-text");
        contractDetailsSection.getChildren().addAll(contractTitle, selectMessage);

        // Booking slot
        VBox bookingSlot = createBookingSlot();

        bottom.getChildren().addAll(companyListSection, contractDetailsSection, bookingSlot);
        return bottom;
    }

    private void updateContractDetailsSection(Client client) {
        contractDetailsSection.getChildren().clear();
        Label title = new Label("Contract Details");
        title.getStyleClass().add("section-title");
        Label company = new Label("Company: " + client.getCompanyName());
        Label contact = new Label("Contact: " + client.getContactName());
        Label email = new Label("Email: " + client.getContactEmail());
        Label phone = new Label("Phone: " + client.getPhoneNumber());
        Label address = new Label("Address: " + client.getStreetAddress());
        Label city = new Label("City: " + client.getCity());
        Label postcode = new Label("Postcode: " + client.getPostcode());
        Label billingName = new Label("Billing Name: " + (client.getBillingName() != null ? client.getBillingName() : "N/A"));
        Label billingEmail = new Label("Billing Email: " + (client.getBillingEmail() != null ? client.getBillingEmail() : "N/A"));

        for (Label label : new Label[]{company, contact, email, phone, address, city, postcode, billingName, billingEmail}) {
            label.getStyleClass().add("section-text");
        }

        contractDetailsSection.getChildren().addAll(title, company, contact, email, phone, address, city, postcode, billingName, billingEmail);
    }

    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    private void assignCompanyColors() {
        Set<String> uniqueCompanies = new HashSet<>();
        for (Booking booking : bookings) {
            uniqueCompanies.add(booking.getCompany());
        }
        Random random = new Random();
        for (String company : uniqueCompanies) {
            if (!companyColors.containsKey(company)) {
                Color color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                companyColors.put(company, color);
            }
        }
    }

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

            if (room != null && date != null && start != null && end != null && !company.isEmpty() && start < end) {
                BookingDAO bookingDAO = new BookingDAO();
                bookingDAO.insertBooking(room, date, start, end, company);

                // Refresh the timeline if the booking is for the current date
                if (date.equals(selectedDate)) {
                    bookings = bookingDAO.getBookingsForDate(selectedDate);
                    root.setCenter(createTimelineGrid());
                }

                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Booking Success");
                alert.setHeaderText(null);
                alert.setContentText("Booking created successfully!");
                alert.showAndWait();
            } else {
                // Show error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Booking Error");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all fields correctly. Ensure the start time is before the end time.");
                alert.showAndWait();
            }
        });

        bookingSlot.getChildren().addAll(title, roomCombo, datePicker, startTimeCombo, endTimeCombo, companyField, bookButton);
        return bookingSlot;
    }
}