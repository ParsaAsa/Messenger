package dao;

import entity.Post;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostDao implements DAO<Post> {
    private Connection connection;

    public PostDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Post post) {
        String sql = "INSERT INTO posts (content, timestamp, photo, user_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, post.getContent());
            statement.setTimestamp(2, Timestamp.valueOf(post.getTimestamp()));
            statement.setBytes(3, post.getPhoto());
            statement.setLong(4, post.getUser().getId());
            statement.executeUpdate();

            // Retrieve the generated ID
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    post.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save post", e);
        }
    }

    @Override
    public Optional<Post> findById(Long id) {
        String sql = "SELECT * FROM posts WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new Post(
                            resultSet.getLong("id"),
                            resultSet.getString("content"),
                            resultSet.getTimestamp("timestamp").toLocalDateTime(),
                            resultSet.getBytes("photo"),
                            null // User is not fetched here
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find post by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM posts";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                posts.add(new Post(
                        resultSet.getLong("id"),
                        resultSet.getString("content"),
                        resultSet.getTimestamp("timestamp").toLocalDateTime(),
                        resultSet.getBytes("photo"),
                        null // User is not fetched here
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all posts", e);
        }
        return posts;
    }

    @Override
    public void update(Post post) {
        String sql = "UPDATE posts SET content = ?, timestamp = ?, photo = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, post.getContent());
            statement.setTimestamp(2, Timestamp.valueOf(post.getTimestamp()));
            statement.setBytes(3, post.getPhoto());
            statement.setLong(4, post.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update post", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM posts WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete post", e);
        }
    }

    // Custom method to fetch posts by user ID
    public List<Post> findByUserId(Long userId) {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM posts WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    posts.add(new Post(
                            resultSet.getLong("id"),
                            resultSet.getString("content"),
                            resultSet.getTimestamp("timestamp").toLocalDateTime(),
                            resultSet.getBytes("photo"),
                            null // User is not fetched here
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch posts by user ID", e);
        }
        return posts;
    }
}