package com.example.codeblooded;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RoomSelectionController {

    @FXML
    private BorderPane mainHallPane, smallHallPane, meetingRoomsPane, rehearsalRoomsPane;

    public void initialize() {
        // Optional initialization
    }

    @FXML
    private void handleClick(MouseEvent event) {
        Object source = event.getSource();
        if (source instanceof BorderPane) {
            BorderPane clickedPane = (BorderPane) source;
            Text text = (Text) clickedPane.getCenter();
            String roomName = text.getText();
            System.out.println("Clicked on: " + roomName);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/codeblooded/DayView.fxml"));
                Parent dayViewRoot = loader.load();

                DayViewController dayViewController = loader.getController();
                // Fetch bookings for the room and a sample date (e.g., today)
                LocalDate sampleDate = LocalDate.now();
                List<Booking> bookings = new BookingDAO().getBookingsForDate(sampleDate)
                        .stream()
                        .filter(b -> b.getRoom().equals(roomName))
                        .collect(Collectors.toList());
                dayViewController.setBookings(bookings);

                Scene dayViewScene = new Scene(dayViewRoot, 1280, 720);
                Stage stage = new Stage();
                stage.setScene(dayViewScene);
                stage.setTitle("Bookings for " + roomName + " on " + sampleDate);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading DayView.fxml");
            }
        }
    }
}