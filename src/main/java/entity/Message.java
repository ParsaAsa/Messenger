package entity;

import java.time.LocalDateTime;

public class Message {
    private Long id;
    private String content;
    private LocalDateTime timestamp;
    private User sender; // Many-to-One relationship with User (sender)
    private User receiver; // Many-to-One relationship with User (receiver)

    // Default constructor (required by JPA and ORM frameworks)
    public Message() {}

    // Constructor for creating a new message (without ID)
    public Message(String content, LocalDateTime timestamp, User sender, User receiver) {
        this.content = content;
        this.timestamp = timestamp;
        this.sender = sender;
        this.receiver = receiver;
    }

    // Constructor for fetching a message from the database (with ID)
    public Message(Long id, String content, LocalDateTime timestamp, User sender, User receiver) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
        this.sender = sender;
        this.receiver = receiver;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public User getSender() { return sender; }
    public void setSender(User sender) { this.sender = sender; }

    public User getReceiver() { return receiver; }
    public void setReceiver(User receiver) { this.receiver = receiver; }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                ", sender=" + sender.getUsername() +
                ", receiver=" + receiver.getUsername() +
                '}';
    }
}