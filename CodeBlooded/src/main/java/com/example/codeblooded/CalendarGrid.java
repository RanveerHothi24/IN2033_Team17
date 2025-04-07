package com.example.codeblooded;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

import javafx.scene.control.TextField;

public class CalendarGrid extends Application {

    private YearMonth currentYearMonth;
    private YearMonth displayedYearMonth;
    private Label monthLabel;
    private GridPane grid;
    private StackPane[][] dayCells;
    private Label[][] dayLabels;
    private static final double CELL_SPACING = 5;

    private List<String> selectedRooms = new ArrayList<>();
    private String selectedCompany = null;

    @Override
    public void start(Stage primaryStage) {
        // Create and set the login scene as the initial view
        Scene loginScene = createLoginScene(primaryStage);
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Login");
        primaryStage.show();
    }

    /** Creates the login scene with username and password fields */
    private Scene createLoginScene(Stage primaryStage) {
        // UI elements
        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-text-fill: white;");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-text-fill: white;");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        // Layout for login form
        GridPane loginForm = new GridPane();
        loginForm.setHgap(10);
        loginForm.setVgap(10);
        loginForm.add(usernameLabel, 0, 0);
        loginForm.add(usernameField, 1, 0);
        loginForm.add(passwordLabel, 0, 1);
        loginForm.add(passwordField, 1, 1);
        loginForm.add(loginButton, 1, 2);
        loginForm.add(errorLabel, 0, 3, 2, 1);  // Error spans 2 columns

        // Center everything in a VBox without the image
        VBox loginBox = new VBox(20, loginForm);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(20));
        loginBox.setStyle("-fx-background-color: #333333;");  // Dark background

        // Login button action
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (username.equals("") && password.equals("")) {  // Hardcoded credentials
                Scene calendarScene = createCalendarScene();
                primaryStage.setScene(calendarScene);
                primaryStage.setTitle("Calendar Grid");
            } else {
                errorLabel.setText("incorrect username or password");
            }
        });

        return new Scene(loginBox, 400, 300);  // Compact size for login screen
    }

    /** Creates the calendar scene (moved from start method) */
    private Scene createCalendarScene() {
        currentYearMonth = YearMonth.now();
        displayedYearMonth = currentYearMonth;

        HBox header = createHeader();
        HBox navigation = createNavigation();
        GridPane daysOfWeek = createDaysOfWeek();
        grid = createGrid();
        populateGrid();

        VBox calendarBox = new VBox(navigation, daysOfWeek, grid);
        calendarBox.setSpacing(10);
        calendarBox.setPadding(new Insets(10));
        VBox.setVgrow(grid, Priority.ALWAYS);

        VBox filterPanel = createFilterPanel();

        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(calendarBox);
        root.setRight(filterPanel);
        BorderPane.setMargin(calendarBox, new Insets(0, 0, 10, 0));

        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/com/example/codeblooded/styles.css").toExternalForm());
        return scene;
    }

    // Existing methods remain unchanged below this point
    private HBox createHeader() {
        HBox header = new HBox();
        header.getStyleClass().add("header");
        Label titleLabel = new Label("Lancaster Music Hall");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");
        Button dailySheetsButton = new Button("Daily Sheets");
        Button reviewsButton = new Button("Reviews");
        Button bookingsButton = new Button("Bookings");
        Button profileButton = new Button("Profile");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(titleLabel, dailySheetsButton, reviewsButton, bookingsButton, spacer, profileButton);
        header.setSpacing(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10));

        dailySheetsButton.setOnAction(e -> System.out.println("Open Daily Sheets page"));
        reviewsButton.setOnAction(e -> System.out.println("Open Reviews page"));
        bookingsButton.setOnAction(e -> System.out.println("Open Bookings page"));
        profileButton.setOnAction(e -> System.out.println("Open Profile page"));

        return header;
    }

    private HBox createNavigation() {
        Button prevButton = new Button("<");
        Button nextButton = new Button(">");
        monthLabel = new Label(displayedYearMonth.getMonth().toString() + " " + displayedYearMonth.getYear());
        monthLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");
        HBox navigation = new HBox(prevButton, monthLabel, nextButton);
        navigation.setSpacing(10);
        navigation.setAlignment(Pos.CENTER);

        prevButton.setOnAction(e -> {
            displayedYearMonth = displayedYearMonth.minusMonths(1);
            updateCalendar();
        });

        nextButton.setOnAction(e -> {
            YearMonth next = displayedYearMonth.plusMonths(1);
            if (!next.isAfter(currentYearMonth.plusMonths(6))) {
                displayedYearMonth = next;
                updateCalendar();
            }
        });

        return navigation;
    }

    private GridPane createDaysOfWeek() {
        GridPane daysOfWeek = new GridPane();
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        for (int col = 0; col < 7; col++) {
            Label label = new Label(days[col]);
            label.setStyle("-fx-text-fill: white;");
            label.setAlignment(Pos.CENTER);
            label.getStyleClass().add("day-label");
            daysOfWeek.add(label, col, 0);
        }

        for (int i = 0; i < 7; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / 7);
            colConst.setHgrow(Priority.ALWAYS);
            daysOfWeek.getColumnConstraints().add(colConst);
        }

        daysOfWeek.setHgap(CELL_SPACING);
        daysOfWeek.setAlignment(Pos.CENTER);
        return daysOfWeek;
    }

    private GridPane createGrid() {
        GridPane gridPane = new GridPane();
        dayCells = new StackPane[5][7];
        dayLabels = new Label[5][7];

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 7; col++) {
                dayLabels[row][col] = new Label();
                dayLabels[row][col].setAlignment(Pos.CENTER);
                dayCells[row][col] = new StackPane(dayLabels[row][col]);
                dayCells[row][col].getStyleClass().add("day-cell");
                gridPane.add(dayCells[row][col], col, row);

                final int r = row, c = col;
                int finalRow = row;
                int finalCol = col;
                dayCells[row][col].setOnMouseClicked(e -> {
                    String dayText = dayLabels[r][c].getText();
                    if (!dayText.isEmpty()) {
                        int dayNumber = Integer.parseInt(dayText.split("/")[0]);
                        LocalDate selectedDate = displayedYearMonth.atDay(dayNumber);
                        handleDaySelection(selectedDate, (Stage) dayCells[finalRow][finalCol].getScene().getWindow());
                    }
                });
            }
        }

        for (int i = 0; i < 7; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / 7);
            colConst.setHgrow(Priority.ALWAYS);
            gridPane.getColumnConstraints().add(colConst);
        }

        for (int i = 0; i < 5; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / 5);
            rowConst.setVgrow(Priority.ALWAYS);
            gridPane.getRowConstraints().add(rowConst);
        }

        gridPane.setHgap(CELL_SPACING);
        gridPane.setVgap(CELL_SPACING);
        gridPane.setStyle("-fx-border-color: black; -fx-border-width: 0;");
        return gridPane;
    }

    private void populateGrid() {
        LocalDate firstDay = displayedYearMonth.atDay(1);
        int firstColumn = (firstDay.getDayOfWeek().getValue() - 1) % 7;
        int lengthOfMonth = displayedYearMonth.lengthOfMonth();
        LocalDate today = LocalDate.now();

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 7; col++) {
                int dayNumber = 1 + 7 * row + col - firstColumn;
                if (dayNumber >= 1 && dayNumber <= lengthOfMonth) {
                    LocalDate date = displayedYearMonth.atDay(dayNumber);
                    dayLabels[row][col].setText(dayNumber + "/" + displayedYearMonth.getMonthValue());
                    dayCells[row][col].getStyleClass().add("active-day");

                    // Apply past/today/future styles
                    dayCells[row][col].getStyleClass().removeAll("past", "today", "future", "filtered-day");
                    if (date.isBefore(today)) {
                        dayCells[row][col].getStyleClass().add("past");
                    } else if (date.isEqual(today)) {
                        dayCells[row][col].getStyleClass().add("today");
                    } else {
                        dayCells[row][col].getStyleClass().add("future");
                    }

                    // Apply filter highlighting
                    List<Booking> bookings = getBookingsForDate(date);
                    boolean matchesFilter = false;
                    for (Booking booking : bookings) {
                        if ((selectedRooms.isEmpty() || selectedRooms.contains(booking.getRoom())) &&
                                (selectedCompany == null || selectedCompany.equals(booking.getCompany()))) {
                            matchesFilter = true;
                            break;
                        }
                    }
                    if (matchesFilter) {
                        dayCells[row][col].getStyleClass().add("filtered-day");
                    }
                } else {
                    dayLabels[row][col].setText("");
                    dayCells[row][col].getStyleClass().removeAll("past", "today", "future", "active-day", "filtered-day");
                }
            }
        }
    }

    private void updateCalendar() {
        monthLabel.setText(displayedYearMonth.getMonth().toString() + " " + displayedYearMonth.getYear());
        populateGrid();
    }



    private VBox createFilterPanel() {
        VBox filterPanel = new VBox(10);
        filterPanel.setStyle("-fx-background-color: #444444;");
        filterPanel.setPrefWidth(200);
        filterPanel.setPadding(new Insets(10));

        // Top half: Room filters
        Label roomFilterLabel = new Label("Room Filters");
        roomFilterLabel.setStyle("-fx-text-fill: white;");
        CheckBox mainHallCheck = new CheckBox("Main Hall");
        mainHallCheck.setStyle("-fx-text-fill: white;");
        CheckBox smallHallCheck = new CheckBox("Small Hall");
        smallHallCheck.setStyle("-fx-text-fill: white;");
        CheckBox rehearsalCheck = new CheckBox("Rehearsal Space");
        rehearsalCheck.setStyle("-fx-text-fill: white;");

        // Bottom half: Company search
        Label companySearchLabel = new Label("Company Search");
        companySearchLabel.setStyle("-fx-text-fill: white;");
        TextField searchField = new TextField();
        searchField.setPromptText("Search companies...");
        ListView<String> companyListView = new ListView<>();
        companyListView.setPrefHeight(150); // Limit height to ensure scrolling

        // Populate company list with unique company names
        Set<String> companies = new HashSet<>();
        for (Booking booking : ExampleData.bookings) {
            companies.add(booking.getCompany());
        }
        companyListView.getItems().addAll(companies);

        // Filter company list based on search input
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            companyListView.getItems().clear();
            for (String company : companies) {
                if (company.toLowerCase().contains(newValue.toLowerCase())) {
                    companyListView.getItems().add(company);
                }
            }
        });

        // Buttons
        Button applyButton = new Button("Apply");
        Button resetButton = new Button("Reset");

        applyButton.setOnAction(e -> {
            selectedRooms.clear();
            if (mainHallCheck.isSelected()) selectedRooms.add("Main Hall");
            if (smallHallCheck.isSelected()) selectedRooms.add("Small Hall");
            if (rehearsalCheck.isSelected()) selectedRooms.add("Rehearsal Space");
            selectedCompany = companyListView.getSelectionModel().getSelectedItem();
            populateGrid(); // Refresh calendar with filters
        });

        resetButton.setOnAction(e -> {
            mainHallCheck.setSelected(false);
            smallHallCheck.setSelected(false);
            rehearsalCheck.setSelected(false);
            searchField.clear();
            companyListView.getItems().clear();
            companyListView.getItems().addAll(companies); // Reset list
            companyListView.getSelectionModel().clearSelection();
            selectedRooms.clear();
            selectedCompany = null;
            populateGrid(); // Refresh calendar
        });

        // Layout
        VBox topHalf = new VBox(5, roomFilterLabel, mainHallCheck, smallHallCheck, rehearsalCheck);
        VBox bottomHalf = new VBox(5, companySearchLabel, searchField, companyListView);
        HBox buttons = new HBox(10, applyButton, resetButton);
        filterPanel.getChildren().addAll(topHalf, new Separator(), bottomHalf, buttons);

        return filterPanel;
    }

    private void handleDaySelection(LocalDate selectedDate, Stage primaryStage) {
        Scene calendarScene = primaryStage.getScene();
        TimelineView timelineView = new TimelineView(primaryStage, selectedDate, calendarScene);
        primaryStage.setScene(timelineView.getView());
    }
    private List<Booking> getBookingsForDate(LocalDate date) {
        List<Booking> bookings = new ArrayList<>();
        for (Booking booking : ExampleData.bookings) {
            if (booking.getDate().equals(date)) {
                bookings.add(booking);
            }
        }
        return bookings;
    }


    public static void main(String[] args) {
        launch(args);
    }
}