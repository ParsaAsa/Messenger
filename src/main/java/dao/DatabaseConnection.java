package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://dpg-cvb2csvnoe9s73fh1a40-a.frankfurt-postgres.render.com:5432/renderfirst";
    private static final String USER = "parsa";
    private static final String PASSWORD = "bTj3ZAOKaQzqcVzQpzDw8q8283M4bngZ";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}