package com.umesh.backend.repository;

import com.umesh.backend.config.ConnectionProvider;
import com.umesh.backend.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL Driver loaded!");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("❌ MySQL Driver missing!", e);
        }
    }

    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String url = "jdbc:mysql://127.0.0.1:3306/learnProcedures?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        try (Connection conn = DriverManager.getConnection(url, "root", "Ume$h2896");
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

}
