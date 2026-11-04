package com.bank.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static final Properties props = loadProperties();

    private static Properties loadProperties() {
        try (InputStream in = DBConnection.class.getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (in == null) {
                throw new RuntimeException(
                        "db.properties not found. Copy db.properties.example to db.properties and fill in your details.");
            }
            Properties p = new Properties();
            p.load(in);
            return p;

        } catch (IOException e) {
            throw new RuntimeException("Could not read db.properties", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password"));
    }
}