package com.example.codeblooded;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;

public class DailySheetView {
    private Stage stage;
    private LocalDate date;
    private Scene backScene;

    public DailySheetView(Stage stage, LocalDate date, Scene backScene) {
        this.stage = stage;
        this.date = date;
        this.backScene = backScene;
    }

    public Scene getView() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #333333;");

        Label title = new Label("Daily Sheet for " + date.toString());
        title.getStyleClass().add("daily-sheet-title");

        Label usageTitle = new Label("Daily Usage Snapshot");
        usageTitle.getStyleClass().add("daily-sheet-section-title");
        Label usageText = new Label("Sample usage data for " + date);
        usageText.getStyleClass().add("daily-sheet-text");

        Label bookingTitle = new Label("Booking Details");
        bookingTitle.getStyleClass().add("daily-sheet-section-title");
        Label bookingText = new Label("Sample booking details for " + date);
        bookingText.getStyleClass().add("daily-sheet-text");

        Label configTitle = new Label("Configuration Requirements");
        configTitle.getStyleClass().add("daily-sheet-section-title");
        Label configText = new Label("Sample configuration requirements for " + date);
        configText.getStyleClass().add("daily-sheet-text");

        Label notesTitle = new Label("Staff Notes");
        notesTitle.getStyleClass().add("daily-sheet-section-title");
        Label notesText = new Label("Sample staff notes for " + date);
        notesText.getStyleClass().add("daily-sheet-text");

        Button backButton = new Button("Back to Timeline");
        backButton.setOnAction(e -> stage.setScene(backScene));

        root.getChildren().addAll(
                title,
                usageTitle, usageText,
                bookingTitle, bookingText,
                configTitle, configText,
                notesTitle, notesText,
                backButton
        );

        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/com/example/codeblooded/styles.css").toExternalForm());
        return scene;
    }
}