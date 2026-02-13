package com.airtribe.meditrack.enums;

/**
 * Appointment Status Enum
 * 
 * Demonstrates:
 * - Enum for state management
 * - Type safety over String constants
 * - Business logic encapsulation
 * 
 * @author Sudharma
 */
public enum AppointmentStatus {
    SCHEDULED("Scheduled", "Appointment is scheduled"),
    CONFIRMED("Confirmed", "Appointment confirmed by patient"),
    IN_PROGRESS("In Progress", "Appointment is currently ongoing"),
    COMPLETED("Completed", "Appointment completed successfully"),
    CANCELLED("Cancelled", "Appointment cancelled"),
    NO_SHOW("No Show", "Patient did not show up"),
    RESCHEDULED("Rescheduled", "Appointment rescheduled to another time");

    private final String displayName;
    private final String description;

    /**
     * Constructor
     * 
     * @param displayName Human-readable status name
     * @param description Status description
     */
    AppointmentStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Check if appointment can be cancelled
     * 
     * @return true if cancellable
     */
    public boolean isCancellable() {
        return this == SCHEDULED || this == CONFIRMED;
    }

    /**
     * Check if appointment is active
     * 
     * @return true if active
     */
    public boolean isActive() {
        return this == SCHEDULED || this == CONFIRMED || this == IN_PROGRESS;
    }

    /**
     * Check if appointment is final (completed or cancelled)
     * 
     * @return true if final
     */
    public boolean isFinal() {
        return this == COMPLETED || this == CANCELLED || this == NO_SHOW;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
