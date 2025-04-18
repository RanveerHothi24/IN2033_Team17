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
import java.util.Map;
import java.util.Set;

public class CalendarGrid extends Application {

    private YearMonth currentYearMonth;
    private YearMonth displayedYearMonth;
    private Label monthLabel;
    private GridPane grid;
    private StackPane[][] dayCells;
    private Label[][] dayLabels;
    private static final double CELL_SPACING = 5;

    private BookingDAO bookingDAO = new BookingDAO(); // Add DAO instance

    private Scene calendarScene;

    @Override

        public void start(Stage primaryStage) {
            Scene loginScene = createLoginScene(primaryStage);
            primaryStage.setScene(loginScene);
            primaryStage.setTitle("Login");
            primaryStage.show();
            currentYearMonth = YearMonth.now();
            displayedYearMonth = currentYearMonth;
            // Remove populateGrid() from here
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
            String username = usernameField.getText().trim(); // Also fixes spacebar issue
            String password = passwordField.getText();
            if (username.equals("") && password.equals("")) {
                this.calendarScene = createCalendarScene();
                primaryStage.setScene(this.calendarScene);
                primaryStage.setTitle("Calendar Grid");
            } else {
                errorLabel.setText("Incorrect username or password");
            }
        });

        return new Scene(loginBox, 1280, 720);  // Compact size for login screen
    }

    /** Creates the calendar scene (moved from start method) */
    private Scene createCalendarScene() {
        currentYearMonth = YearMonth.now();
        displayedYearMonth = currentYearMonth;

        HBox header = createHeader();
        HBox navigation = createNavigation();
        GridPane daysOfWeek = createDaysOfWeek();
        grid = createGrid(); // Initializes dayCells and dayLabels
        populateGrid();      // Populate the grid after initialization

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
                        Stage stage = (Stage) dayCells[r][c].getScene().getWindow();
                        TimelineView timelineView = new TimelineView(stage, this.calendarScene, selectedDate);
                        Scene timelineScene = timelineView.createScene();
                        stage.setScene(timelineScene);
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
        Map<LocalDate, Integer> bookingCounts = bookingDAO.getRoomBookingCountsForMonth(displayedYearMonth);

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 7; col++) {
                int dayNumber = 1 + 7 * row + col - firstColumn;
                if (dayNumber >= 1 && dayNumber <= lengthOfMonth) {
                    LocalDate date = displayedYearMonth.atDay(dayNumber);
                    dayLabels[row][col].setText(String.valueOf(dayNumber)); // Show only day number
                    dayCells[row][col].getStyleClass().add("active-day");
                    dayCells[row][col].getStyleClass().removeAll("past", "today", "future", "fully-booked", "partially-booked", "minimally-booked");

                    // Apply date-based styles
                    if (date.isBefore(today)) {
                        dayCells[row][col].getStyleClass().add("past");
                    } else if (date.isEqual(today)) {
                        dayCells[row][col].getStyleClass().add("today");
                    } else {
                        dayCells[row][col].getStyleClass().add("future");
                    }

                    // Apply booking-based styles
                    if (((Map<?, ?>) bookingCounts).containsKey(date)) {
                        int roomCount = bookingCounts.get(date);
                        if (roomCount == 3) {
                            dayCells[row][col].getStyleClass().add("fully-booked");
                        } else if (roomCount == 2) {
                            dayCells[row][col].getStyleClass().add("partially-booked");
                        } else if (roomCount == 1) {
                            dayCells[row][col].getStyleClass().add("minimally-booked");
                        }
                    }
                } else {
                    dayLabels[row][col].setText("");
                    dayCells[row][col].getStyleClass().removeAll("past", "today", "future", "active-day", "fully-booked", "partially-booked", "minimally-booked");
                }
            }
        }
    }

    // Rest of the class...


    private void updateCalendar() {
        monthLabel.setText(displayedYearMonth.getMonth().toString() + " " + displayedYearMonth.getYear());
        populateGrid();
    }



    private VBox createFilterPanel() {
        VBox filterPanel = new VBox();
        filterPanel.setStyle("-fx-background-color: #444444;");
        filterPanel.setPrefWidth(200);
        Label filterLabel = new Label("Filters");
        filterLabel.setStyle("-fx-text-fill: white;");
        CheckBox filter1 = new CheckBox("MH");
        filter1.setStyle("-fx-text-fill: white;");

        CheckBox filter2 = new CheckBox("SH");
        filter2.setStyle("-fx-text-fill: white;");

        CheckBox filter3 = new CheckBox("F");
        filter3.setStyle("-fx-text-fill: white;");

        CheckBox filter4 = new CheckBox("M");
        filter4.setStyle("-fx-text-fill: white;");

        Button applyButton = new Button("Apply");
        Button resetButton = new Button("Reset");

        resetButton.setOnAction(e -> {
            filter1.setSelected(false);
            filter2.setSelected(false);
            filter3.setSelected(false);
        });

        applyButton.setOnAction(e -> {
            String filters = "";
            if (filter1.isSelected()) filters += "Filter 1, ";
            if (filter2.isSelected()) filters += "Filter 2, ";
            if (filter3.isSelected()) filters += "Filter 3, ";
            if (filter4.isSelected()) filters += "Filter 4, ";

            System.out.println("Applied these filters: " + (filters.isEmpty() ? "None" : filters.substring(0, filters.length() - 2)));
        });

        filterPanel.getChildren().addAll(filterLabel, filter1, filter2, filter3, filter4, applyButton, resetButton);
        filterPanel.setSpacing(10);
        filterPanel.setPadding(new Insets(10));
        return filterPanel;
    }




    public static void main(String[] args) {
        launch(args);
    }
}