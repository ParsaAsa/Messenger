package handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import controller.UserController;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class UserHandler implements HttpHandler {
    private UserController userController;

    public UserHandler(UserController userController) {
        this.userController = userController;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        String response = "";
        int statusCode = 200;

        try {
            switch (method) {
                case "GET":
                    if (path.equals("/users")) {
                        // Get all users
                        response = userController.getAllUsers();
                    } else if (path.startsWith("/users/")) {
                        // Get user by ID
                        Long id = Long.parseLong(path.split("/")[2]);
                        response = userController.getUserById(id);
                    }
                    break;

                case "POST":
                    if (path.equals("/users")) {
                        // Create a new user
                        Map<String, String> requestBody = parseRequestBody(exchange);
                        String username = requestBody.get("username");
                        String email = requestBody.get("email");
                        String passwordHash = requestBody.get("passwordHash");
                        response = userController.createUser(username, email, passwordHash);
                    }
                    break;

                case "PUT":
                    if (path.startsWith("/users/")) {
                        // Update user by ID
                        Long id = Long.parseLong(path.split("/")[2]);
                        Map<String, String> requestBody = parseRequestBody(exchange);
                        String username = requestBody.get("username");
                        String email = requestBody.get("email");
                        String passwordHash = requestBody.get("passwordHash");
                        response = userController.updateUser(id, username, email, passwordHash);
                    }
                    break;

                case "DELETE":
                    if (path.startsWith("/users/")) {
                        // Delete user by ID
                        Long id = Long.parseLong(path.split("/")[2]);
                        response = userController.deleteUser(id);
                    }
                    break;

                default:
                    response = "Unsupported HTTP method: " + method;
                    statusCode = 405; // Method Not Allowed
                    break;
            }
        } catch (Exception e) {
            response = "Error: " + e.getMessage();
            statusCode = 500; // Internal Server Error
        }

        // Send the response
        exchange.sendResponseHeaders(statusCode, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    // Helper method to parse the request body
    private Map<String, String> parseRequestBody(HttpExchange exchange) throws IOException {
        String requestBody = new String(exchange.getRequestBody().readAllBytes());
        Map<String, String> bodyMap = new HashMap<>();
        for (String pair : requestBody.split("&")) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                bodyMap.put(keyValue[0], keyValue[1]);
            }
        }
        return bodyMap;
    }
}