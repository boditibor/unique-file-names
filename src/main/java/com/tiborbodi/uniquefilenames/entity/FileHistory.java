package com.tiborbodi.uniquefilenames.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entity representing the file operation history.
 */
@Entity
@Table(name = "file_history")
public class FileHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private LocalDateTime timestamp;
    private String requestedPath;

    /**
     * Constructs a FileHistory entity.
     *
     * @param userName      the user who performed the operation
     * @param timestamp     the time of the operation
     * @param requestedPath the path requested
     */
    public FileHistory(String userName, LocalDateTime timestamp, String requestedPath) {
        this.userName = userName;
        this.timestamp = timestamp;
        this.requestedPath = requestedPath;
    }

    /**
     * Default constructor for JPA.
     */
    public FileHistory() {

    }

    /**
     * Gets the ID of the history record.
     *
     * @return the ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the user's name.
     *
     * @return the user's name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Gets the timestamp of the operation.
     *
     * @return the timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the requested path.
     *
     * @return the requested path
     */
    public String getRequestedPath() {
        return requestedPath;
    }

}

