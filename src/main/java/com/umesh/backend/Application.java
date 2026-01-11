package com.umesh.backend;

import com.umesh.backend.config.ConnectionProvider;
import com.umesh.backend.config.MySQLConnectionProvider;
import com.umesh.backend.model.User;
import com.umesh.backend.repository.UserRepository;
import java.sql.SQLException;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        UserRepository repo = new UserRepository();
        ConnectionProvider provider = new MySQLConnectionProvider();
        try {
            System.out.println("=== 1. ALL USERS (Raw JDBC) ===");
            repo.findAll().forEach(System.out::println);

            System.out.println("\n=== 2. countItems() Stored Proc ===");
            repo.callProcedureWithProvider(provider).forEach(System.out::println);

            System.out.println("\n=== 3. SelectByLast('Tiwari') ===");
            repo.selectByLastName(provider, "Tiwari").forEach(System.out::println);

            System.out.println("\n=== 4. IN+OUT: Users with lastName 'Tiwari' ===");
            int count = repo.selectByLastNameTwo(provider, "Tiwari");
            System.out.println("Users with lastName 'Tiwari': " + count);

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}


