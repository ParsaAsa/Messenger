package dao;

import com.sun.net.httpserver.HttpServer;
import controller.UserController;
import dao.UserDao;
import handler.UserHandler;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            // Establish a database connection
            connection = DatabaseConnection.getConnection();
            System.out.println("Connected to the database!");

            // Initialize DAOs
            UserDao userDao = new UserDao(connection);

            // Initialize controllers
            UserController userController = new UserController(userDao);

            // Start the HTTP server
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            // Register the UserHandler
            server.createContext("/users", new UserHandler(userController));

            server.start();
            System.out.println("Server started on port 8080");

            // Keep the application running
            System.in.read(); // Wait for user input to stop the server

        } catch (SQLException | IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close the connection when the application exits
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Database connection closed.");
                } catch (SQLException e) {
                    System.err.println("Failed to close the database connection: " + e.getMessage());
                }
            }
        }
    }
}