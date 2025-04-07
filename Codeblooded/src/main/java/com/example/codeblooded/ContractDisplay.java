package com.example.codeblooded;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ContractDisplay {
    private static VBox contractBox = new VBox(10);    // For bottom right
    private static VBox companyListBox = new VBox(10); // For bottom left

    // Create the contract box for display on bottom right
    public static VBox createContractBox(Client client) {
        contractBox.getChildren().clear(); // Clear previous content
        contractBox.setStyle("-fx-background-color: #333333; -fx-padding: 20px;");

        Label title = new Label("Contract");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

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
            label.setStyle("-fx-text-fill: white;");
        }

        contractBox.getChildren().addAll(title, company, contact, email, phone, address, city, postcode, billingName, billingEmail);
        return contractBox;
    }

    // Add a company to the bottom left list with a colored box
    public static void addCompanyToList(String companyName, String timeSlot, Color color) {
        Rectangle colorBox = new Rectangle(10, 10, color);
        Label companyLabel = new Label(companyName + " (" + timeSlot + ")");
        companyLabel.setStyle("-fx-text-fill: white;");
        HBox companyEntry = new HBox(5, colorBox, companyLabel);
        companyListBox.getChildren().add(companyEntry);
    }

    // Clear the company list (used when updating for a new day)
    public static void clearCompanyList() {
        companyListBox.getChildren().clear();
    }

    // Getters for integration with the main layout
    public static VBox getContractBox() {
        return contractBox;
    }

    public static VBox getCompanyListBox() {
        return companyListBox;
    }
}