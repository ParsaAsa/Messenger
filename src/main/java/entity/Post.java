package entity;

import java.time.LocalDateTime;

public class Post {
    private Long id;
    private String content;
    private LocalDateTime timestamp;
    private byte[] photo; // Binary data for the photo (optional)
    private User user; // Many-to-One relationship with User

    // Default constructor (required by JPA and ORM frameworks)
    public Post() {}

    // Constructor for creating a new post (without ID)
    public Post(String content, LocalDateTime timestamp, byte[] photo, User user) {
        this.content = content;
        this.timestamp = timestamp;
        this.photo = photo;
        this.user = user;
    }

    // Constructor for fetching a post from the database (with ID)
    public Post(Long id, String content, LocalDateTime timestamp, byte[] photo, User user) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
        this.photo = photo;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public byte[] getPhoto() { return photo; }
    public void setPhoto(byte[] photo) { this.photo = photo; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}