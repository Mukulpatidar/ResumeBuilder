package com.resume.builder.servlets.auth;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.resume.builder.db.DatabaseConnection;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Retrieve form data
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // 2. Hash the password for security
        String hashedPassword = hashPassword(password);
        
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // 3. Get a database connection
            conn = DatabaseConnection.getConnection();
            
            // 4. Create a SQL INSERT statement using PreparedStatement to prevent SQL injection
            String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            
            // 5. Set the parameter values
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, hashedPassword);
            
            // 6. Execute the statement
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // 7. If successful, redirect to the login page
                response.sendRedirect("login.jsp");
            } else {
                // Handle registration failure
                response.getWriter().println("Registration failed. Please try again.");
            }

        } catch (SQLException e) {
            // Handle potential duplicate username/email errors
            e.printStackTrace();
            response.getWriter().println("An error occurred. The username or email may already be in use.");
        } finally {
            // 8. Close resources to prevent leaks
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Hashes a password using SHA-256.
     * NOTE: For a real-world application, use a stronger, salted hashing algorithm like BCrypt.
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}