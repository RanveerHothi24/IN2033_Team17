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
import java.util.Arrays;
import java.util.List;

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

            // Load DayView after room selection
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/codeblooded/DayView.fxml"));
                Parent dayViewRoot = loader.load();

                // Get the DayViewController and set sample bookings
                DayViewController dayViewController = loader.getController();

                // Hardcoded sample bookings for testing
                List<Booking> sampleBookings = Arrays.asList(
                        new Booking(11, 13, "Company A"), // 11:00 to 13:00
                        new Booking(14, 16, "Company B"), // 14:00 to 16:00
                        new Booking(18, 20, "Company C")  // 18:00 to 20:00
                );
                dayViewController.setBookings(sampleBookings);

                // Create a new scene with 1280x720 dimensions
                Scene dayViewScene = new Scene(dayViewRoot, 1280, 720);
                Stage stage = new Stage();
                stage.setScene(dayViewScene);
                stage.setTitle("Bookings for " + roomName + " on [Selected Day]");
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading DayView.fxml");
            }
        }
    }
}