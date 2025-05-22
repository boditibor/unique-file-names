package com.tiborbodi.uniquefilenames.exception;

/**
 * Exception thrown for file operation errors.
 */
public class FileOperationException extends RuntimeException {
    /**
     * Constructs a new FileOperationException with the specified detail message.
     * @param message the detail message
     */
    public FileOperationException(String message) {
        super(message);
    }
}

