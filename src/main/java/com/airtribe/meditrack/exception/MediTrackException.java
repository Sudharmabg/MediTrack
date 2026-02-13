package com.airtribe.meditrack.exception;

/**
 * Base Exception for MediTrack Application
 * 
 * Demonstrates:
 * - Custom exception hierarchy
 * - Exception chaining
 * - Proper exception design
 * 
 * @author Sudharma
 */
public class MediTrackException extends RuntimeException {

    private final String errorCode;

    public MediTrackException(String message) {
        super(message);
        this.errorCode = "MEDITRACK_ERROR";
    }

    public MediTrackException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public MediTrackException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "MEDITRACK_ERROR";
    }

    public MediTrackException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
