package com.example.codeblooded;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBCDemo {
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://sst-stuproj.city.ac.uk:3306/in2033t17",
                    "in2033t17_a",
                    "3cL1h1l3tY8"
            );
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM bookings");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("booking_id") + " " +
                        resultSet.getDate("date") + " " +
                        resultSet.getString("room") + " " +
                        resultSet.getInt("start_hour") + " " +
                        resultSet.getInt("end_hour") + " " +
                        resultSet.getString("company_name"));
            }
            connection.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}