package com.airtribe.meditrack.exception;

/**
 * Exception thrown when data validation fails
 * 
 * Demonstrates:
 * - Validation exception
 * - Custom error messages
 * 
 * @author Sudharma
 */
public class InvalidDataException extends MediTrackException {

    public InvalidDataException(String fieldName, String reason) {
        super(String.format("Invalid data for field '%s': %s", fieldName, reason),
                "INVALID_DATA");
    }

    public InvalidDataException(String message) {
        super(message, "INVALID_DATA");
    }

    public InvalidDataException(String message, Throwable cause) {
        super(message, "INVALID_DATA", cause);
    }
}
