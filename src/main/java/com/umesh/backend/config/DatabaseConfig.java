package com.umesh.backend.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static Connection connnection;
    public static Connection getConnection() throws SQLException {

        try{
            if(connnection==null) {
                connnection = DriverManager.getConnection(
                        "jdbc:mysql://127.0.0.1:3306/learnProcedures",
                        "root",
                        "Ume$h2896"
                );
            }
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return connnection;
    }


}
