package com.resume.builder.servlets.auth;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.resume.builder.db.DatabaseConnection;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String hashedPassword = hashPassword(password);
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT id, username, password FROM users WHERE email = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            
            rs = stmt.executeQuery();

            if (rs.next()) {
                // User with the given email exists, check the password
                String storedHashedPassword = rs.getString("password");
                
                if (hashedPassword.equals(storedHashedPassword)) {
                    // Password matches! Create a session.
                    HttpSession session = request.getSession();
                    session.setAttribute("userId", rs.getInt("id"));
                    session.setAttribute("username", rs.getString("username"));
                    
                    // Redirect to the main dashboard
                    response.sendRedirect("dashboard.jsp");
                } else {
                    // Password does not match
                    response.sendRedirect("login.jsp?error=1");
                }
            } else {
                // No user found with that email
                response.sendRedirect("login.jsp?error=1");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("login.jsp?error=1");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // This hashPassword method MUST be identical to the one in RegisterServlet
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
