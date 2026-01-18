package com.umesh.backend.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnectionProvider implements ConnectionProvider {
    private final String url = "jdbc:mysql://127.0.0.1:3306/learnProcedures?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private final String username = "root";
    private final String password = "Ume$h2896";

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public void close() {
        // Connection pooling handled by app server in production
    }
}
