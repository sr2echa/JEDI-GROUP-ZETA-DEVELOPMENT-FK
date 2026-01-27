package com.flipfit.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Properties props = new Properties();
                try (FileInputStream fis = new FileInputStream(".env")) {
                    props.load(fis);
                }

                String url = props.getProperty("DB_URL");
                String user = props.getProperty("DB_USER");
                String pass = props.getProperty("DB_PASSWORD");

                if (url == null || url.isEmpty()
                        || user == null || user.isEmpty()
                        || pass == null || pass.isEmpty()) {
                    throw new RuntimeException("Missing required database configuration properties: DB_URL, DB_USER, DB_PASSWORD");
                }

                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, user, pass);
            } catch (IOException | ClassNotFoundException | SQLException e) {
                throw new RuntimeException("Failed to create database connection", e);
            }
        }
        return connection;
    }
}
