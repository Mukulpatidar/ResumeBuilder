package com.resume.builder.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

	
	   // --- IMPORTANT: UPDATE THESE VALUES WITH YOUR DATABASE CREDENTIALS ---
    private static final String DB_URL = "jdbc:mysql://localhost:3306/Resume_Builder?useSSL=false";
    private static final String DB_USER = "root"; // e.g., "root"
    private static final String DB_PASSWORD = "Root"; // e.g., "password"
    // --------------------------------------------------------------------

    /**
     * Establishes and returns a connection to the database.
     * @return A Connection object or null if connection fails.
     */
    public static Connection getConnection() {
        Connection connection = null; 
        try {
            // 1. Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. Establish the connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection failed!");
            e.printStackTrace();
        }
        return connection;
    }
}
