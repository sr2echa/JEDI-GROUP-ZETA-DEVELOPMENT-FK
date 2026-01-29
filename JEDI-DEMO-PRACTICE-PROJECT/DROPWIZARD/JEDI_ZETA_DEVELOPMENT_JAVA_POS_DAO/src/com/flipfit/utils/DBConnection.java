package com.flipfit.utils;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/// Class level Commenting

// TODO: Auto-generated Javadoc
/**
 * The Class DBConnection.
 *
 * @author Zeta
 * @ClassName  "DBConnection"
 */
public class DBConnection {
    
    /** The connection. */
    private static Connection connection = null;

    /**
     * Gets the connection.
     *
     * @return the connection
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Properties props = new Properties();

                // Try multiple locations for .env file
                String[] envPaths = {
                        ".env",
                        "JEDI-DEMO-PRACTICE-PROJECT/JEDI_ZETA_DEVELOPMENT_JAVA_POS_DAO/.env",
                        System.getProperty("user.dir") + "/.env",
                        System.getProperty("user.dir")
                                + "/JEDI-DEMO-PRACTICE-PROJECT/JEDI_ZETA_DEVELOPMENT_JAVA_POS_DAO/.env"
                };

                boolean loaded = false;
                for (String envPath : envPaths) {
                    File envFile = new File(envPath);
                    if (envFile.exists()) {
                        try (FileInputStream fis = new FileInputStream(envFile)) {
                            props.load(fis);
                            loaded = true;
                            break;
                        }
                    }
                }

                if (!loaded) {
                    throw new RuntimeException("Could not find .env file.");
                }

                String url = props.getProperty("DB_URL");
                String user = props.getProperty("DB_USER");
                String pass = props.getProperty("DB_PASSWORD");

                if (url == null || user == null || pass == null) {
                    throw new RuntimeException("Missing DB properties in .env");
                }

                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, user, pass);
            }
        } catch (Exception e) {
            throw new RuntimeException("DB Connection failed", e);
        }
        return connection;
    }
}