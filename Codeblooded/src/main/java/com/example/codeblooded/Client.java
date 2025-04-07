package com.example.codeblooded;

import java.sql.*;

public class Client {
    private String companyName;
    private String contactName;
    private String contactEmail;
    private String phoneNumber;
    private String streetAddress;
    private String city;
    private String postcode;
    private String billingName;
    private String billingEmail;

    public Client(String companyName, String contactName, String contactEmail, String phoneNumber,
                  String streetAddress, String city, String postcode, String billingName, String billingEmail) {
        this.companyName = companyName;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.phoneNumber = phoneNumber;
        this.streetAddress = streetAddress;
        this.city = city;
        this.postcode = postcode;
        this.billingName = billingName;
        this.billingEmail = billingEmail;
    }

    public String getCompanyName() { return companyName; }
    public String getContactName() { return contactName; }
    public String getContactEmail() { return contactEmail; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getStreetAddress() { return streetAddress; }
    public String getCity() { return city; }
    public String getPostcode() { return postcode; }
    public String getBillingName() { return billingName; }
    public String getBillingEmail() { return billingEmail; }

    public static Client getClientByBookingId(int bookingId) {
        Client client = null;
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://sst-stuproj.city.ac.uk:3306/in2033t17",
                    "in2033t17_a",
                    "3c1lh1l3tY8"
            );
            String sql = "SELECT * FROM contracts WHERE booking_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                client = new Client(
                        rs.getString("company_name"),
                        rs.getString("contact_name"),
                        rs.getString("contact_email"),
                        rs.getString("phone_number"),
                        rs.getString("street_address"),
                        rs.getString("city"),
                        rs.getString("postcode"),
                        rs.getString("billing_name"),
                        rs.getString("billing_email")
                );
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return client;
    }
}