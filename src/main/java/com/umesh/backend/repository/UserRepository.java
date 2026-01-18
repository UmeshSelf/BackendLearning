package com.umesh.backend.repository;

import com.umesh.backend.config.ConnectionProvider;
import com.umesh.backend.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    public List<User> findAll(ConnectionProvider provider) throws SQLException {  // Add provider
        List<User> users = new ArrayList<>();
        try (Connection conn = provider.getConnection();  // Use provider instead of hardcoded
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {
            while (rs.next()) {
                users.add(new User(
                        rs.getLong("user_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("city")
                ));
            }
        }
        return users;
    }

    // NEW METHOD: Call stored procedure with ConnectionProvider
    public List<User> callProcedureWithProvider(ConnectionProvider provider) throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection conn = provider.getConnection();  // Uses ConnectionProvider
             CallableStatement stmt = conn.prepareCall("{CALL CreateUserTable()}")) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(new User(
                        (long) rs.getInt("user_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("city")
                ));
            }
        }
        return users;
    }

    // NEW METHOD: Select by last name using CallableStatement
    public List<User> selectByLastName(ConnectionProvider provider, String lastName) throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection conn = provider.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL SelectByLast(?)}")) {

            // Set IN parameter
            stmt.setString(1, lastName);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(new User(
                            (long) rs.getInt("user_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("city")
                    ));
                }
            }
        }
        return users;
    }


    // NEW METHOD: Select by last name using CallableStatement
    public int selectByLastNameTwo(ConnectionProvider provider, String lastName) throws SQLException {
        try (Connection conn = provider.getConnection();
             CallableStatement stmt = conn.prepareCall("{CALL CreateUserCount(?, ?)}")) {  // Fixed procedure name

            // IN parameter (position 1)
            stmt.setString(1, lastName);                    // ✅ Position 1 = IN

            // OUT parameter (position 2) - FIXED INDEX
            stmt.registerOutParameter(2, Types.INTEGER);    // ✅ Position 2 = OUT

            // Execute
            stmt.execute();

            // Get OUT parameter result - FIXED INDEX
            return stmt.getInt(2);                          // ✅ Position 2 = OUT result
        }
    }

    // ADD THIS METHOD to your existing UserRepository class
    public void createUsersTable(ConnectionProvider provider) throws SQLException {
        String createTableSQL = """
        CREATE TABLE IF NOT EXISTS users (
            user_id INT PRIMARY KEY AUTO_INCREMENT,
            first_name VARCHAR(100) NOT NULL,
            last_name VARCHAR(100) NOT NULL,
            city VARCHAR(100) NOT NULL,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        )
        """;

        try (Connection conn = provider.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("✅ Users table created/verified successfully!");
        }
    }


    // ADD THIS METHOD to UserRepository
    public void insertSampleData(ConnectionProvider provider) throws SQLException {
        String insertSQL = """
        INSERT IGNORE INTO users (first_name, last_name, city) VALUES
        ('Durgesh', 'Tiwari', 'Bengaluru'),
        ('John', 'Doe', 'Mumbai'),
        ('Jane', 'Smith', 'Delhi'),
        ('Priya', 'Sharma', 'Pune')
        """;

        try (Connection conn = provider.getConnection();
             Statement stmt = conn.createStatement()) {
            int rowsInserted = stmt.executeUpdate(insertSQL);
            System.out.println("✅ Inserted " + rowsInserted + " sample users");
        }
    }

    // ADD THIS METHOD to see table structure
    public void showTableInfo(ConnectionProvider provider) throws SQLException {
        try (Connection conn = provider.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("DESCRIBE users")) {

            System.out.println("\n📋 USERS TABLE STRUCTURE:");
            while (rs.next()) {
                System.out.printf("Field: %-12s Type: %-15s Null: %s%n",
                        rs.getString("Field"),
                        rs.getString("Type"),
                        rs.getString("Null"));
            }
        }
    }


}
