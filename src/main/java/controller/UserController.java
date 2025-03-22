package controller;

import dao.UserDao;
import entity.User;
import java.util.List;
import java.util.Optional;

public class UserController {
    private UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    // Create a new user
    public String createUser(String username, String email, String passwordHash) {
        User user = new User(username, email, passwordHash);
        userDao.save(user);
        return "User created: " + user;
    }

    // Get user by ID
    public String getUserById(Long id) {
        Optional<User> user = userDao.findById(id);
        return user.map(u -> "Fetched user: " + u)
                .orElse("User not found!");
    }

    // Get all users
    public String getAllUsers() {
        List<User> users = userDao.findAll();
        StringBuilder response = new StringBuilder("All users:\n");
        for (User user : users) {
            response.append(user).append("\n");
        }
        return response.toString();
    }

    // Update user
    public String updateUser(Long id, String username, String email, String passwordHash) {
        Optional<User> userOptional = userDao.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(username);
            user.setEmail(email);
            user.setPasswordHash(passwordHash);
            userDao.update(user);
            return "Updated user: " + user;
        } else {
            return "User not found!";
        }
    }

    // Delete user by ID
    public String deleteUser(Long id) {
        Optional<User> userOptional = userDao.findById(id);
        if (userOptional.isPresent()) {
            userDao.deleteById(id);
            return "Deleted user with ID: " + id;
        } else {
            return "User not found!";
        }
    }
}