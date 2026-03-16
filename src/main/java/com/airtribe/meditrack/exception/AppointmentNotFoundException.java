package com.airtribe.meditrack.exception;

/**
 * Exception thrown when an appointment is not found
 * 
 * Demonstrates:
 * - Specific exception for domain logic
 * - Inheritance from base exception
 * 
 * @author Sudharma
 */
public class AppointmentNotFoundException extends MediTrackException {

    public AppointmentNotFoundException(Long appointmentId) {
        super(String.format("Appointment not found with ID: %d", appointmentId),
                "APPOINTMENT_NOT_FOUND");
    }

    public AppointmentNotFoundException(String message) {
        super(message, "APPOINTMENT_NOT_FOUND");
    }

    public AppointmentNotFoundException(String message, Throwable cause) {
        super(message, "APPOINTMENT_NOT_FOUND", cause);
    }
}
