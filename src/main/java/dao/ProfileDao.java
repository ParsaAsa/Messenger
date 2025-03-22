package dao;

import entity.Profile;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProfileDao implements DAO<Profile> {
    private Connection connection;

    public ProfileDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Profile profile) {
        String sql = "INSERT INTO profiles (first_name, last_name, bio, profile_picture_url, user_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, profile.getFirstName());
            statement.setString(2, profile.getLastName());
            statement.setString(3, profile.getBio());
            statement.setString(4, profile.getProfilePictureUrl());
            statement.setLong(5, profile.getUser().getId());
            statement.executeUpdate();

            // Retrieve the generated ID
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    profile.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save profile", e);
        }
    }

    @Override
    public Optional<Profile> findById(Long id) {
        String sql = "SELECT * FROM profiles WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new Profile(
                            resultSet.getLong("id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("bio"),
                            resultSet.getString("profile_picture_url"),
                            null // User is not fetched here
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find profile by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Profile> findAll() {
        List<Profile> profiles = new ArrayList<>();
        String sql = "SELECT * FROM profiles";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                profiles.add(new Profile(
                        resultSet.getLong("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("bio"),
                        resultSet.getString("profile_picture_url"),
                        null // User is not fetched here
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all profiles", e);
        }
        return profiles;
    }

    @Override
    public void update(Profile profile) {
        String sql = "UPDATE profiles SET first_name = ?, last_name = ?, bio = ?, profile_picture_url = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, profile.getFirstName());
            statement.setString(2, profile.getLastName());
            statement.setString(3, profile.getBio());
            statement.setString(4, profile.getProfilePictureUrl());
            statement.setLong(5, profile.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update profile", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM profiles WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete profile", e);
        }
    }
}