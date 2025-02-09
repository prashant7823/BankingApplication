package com.prashant.BankingApplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private int id;
    private String username;
    private String password;
    private double balance;

    public User(int id, String username, String password, double balance) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.balance = balance;
    }

    public static User authenticate(String username, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getDouble("balance"));
                } else {
                    return null;
                }
            }
        }
    }

    public static boolean signUp(String username, String password) throws SQLException {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            return pstmt.executeUpdate() > 0;
        }
    }

    public double getBalance() {
        return balance;
    }

    public int getId() {
        return id;
    }

    public void updateBalance(double amount) throws SQLException {
        String query = "UPDATE users SET balance = ? WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setDouble(1, this.balance + amount);
            pstmt.setInt(2, this.id);
            pstmt.executeUpdate();
            this.balance += amount;
        }
    }
    
    public static User getUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getDouble("balance"));
                } else {
                    return null;
                }
            }
        }
    }
}