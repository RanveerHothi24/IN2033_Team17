package com.example.codeblooded;

import java.sql.*;

public class ContractDAO {
    private static final String URL = "jdbc:mysql://sst-stuproj.city.ac.uk:3306/in2033t17";
    private static final String USER = "in2033t17_a";
    private static final String PASSWORD = "3cL1h1l3tY8";

    public Client getContractDetails(String companyName) {
        Client client = null;
        String query = "SELECT * FROM contracts WHERE company_name = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, companyName);
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
        } catch (SQLException e) {
            System.out.println("Error fetching contract details: " + e.getMessage());
        }
        return client;
    }
}
