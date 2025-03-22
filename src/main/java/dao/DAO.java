package dao;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    // Save an entity to the database
    void save(T entity);

    // Find an entity by its ID
    Optional<T> findById(Long id);

    // Find all entities
    List<T> findAll();

    // Update an entity
    void update(T entity);

    // Delete an entity by its ID
    void deleteById(Long id);
}