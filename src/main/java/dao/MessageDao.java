package dao;

import entity.Message;
import entity.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageDao implements DAO<Message> {
    private Connection connection;
    private UserDao userDao; // Add UserDao to fetch sender and receiver

    public MessageDao(Connection connection) {
        this.connection = connection;
        this.userDao = new UserDao(connection); // Initialize UserDao
    }

    @Override
    public void save(Message message) {
        String sql = "INSERT INTO messages (content, timestamp, sender_id, receiver_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, message.getContent());
            statement.setTimestamp(2, Timestamp.valueOf(message.getTimestamp()));
            statement.setLong(3, message.getSender().getId());
            statement.setLong(4, message.getReceiver().getId());
            statement.executeUpdate();

            // Retrieve the generated ID
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    message.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save message", e);
        }
    }

    @Override
    public Optional<Message> findById(Long id) {
        String sql = "SELECT * FROM messages WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Fetch sender and receiver from the database
                    User sender = userDao.findById(resultSet.getLong("sender_id")).orElse(null);
                    User receiver = userDao.findById(resultSet.getLong("receiver_id")).orElse(null);

                    return Optional.of(new Message(
                            resultSet.getLong("id"),
                            resultSet.getString("content"),
                            resultSet.getTimestamp("timestamp").toLocalDateTime(),
                            sender,
                            receiver
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find message by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Message> findAll() {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM messages";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                // Fetch sender and receiver from the database
                User sender = userDao.findById(resultSet.getLong("sender_id")).orElse(null);
                User receiver = userDao.findById(resultSet.getLong("receiver_id")).orElse(null);

                messages.add(new Message(
                        resultSet.getLong("id"),
                        resultSet.getString("content"),
                        resultSet.getTimestamp("timestamp").toLocalDateTime(),
                        sender,
                        receiver
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all messages", e);
        }
        return messages;
    }

    @Override
    public void update(Message message) {
        String sql = "UPDATE messages SET content = ?, timestamp = ?, sender_id = ?, receiver_id = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, message.getContent());
            statement.setTimestamp(2, Timestamp.valueOf(message.getTimestamp()));
            statement.setLong(3, message.getSender().getId());
            statement.setLong(4, message.getReceiver().getId());
            statement.setLong(5, message.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update message", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM messages WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete message", e);
        }
    }

    // Custom method to fetch messages between two users
    public List<Message> findMessagesBetweenUsers(Long senderId, Long receiverId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) ORDER BY timestamp";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, senderId);
            statement.setLong(2, receiverId);
            statement.setLong(3, receiverId);
            statement.setLong(4, senderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    // Fetch sender and receiver from the database
                    User sender = userDao.findById(resultSet.getLong("sender_id")).orElse(null);
                    User receiver = userDao.findById(resultSet.getLong("receiver_id")).orElse(null);

                    messages.add(new Message(
                            resultSet.getLong("id"),
                            resultSet.getString("content"),
                            resultSet.getTimestamp("timestamp").toLocalDateTime(),
                            sender,
                            receiver
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch messages between users", e);
        }
        return messages;
    }
}