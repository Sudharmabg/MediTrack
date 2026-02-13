package com.airtribe.meditrack.util.observer;

import com.airtribe.meditrack.entity.Appointment;

/**
 * Observer Interface - Observer Pattern
 * 
 * Demonstrates:
 * - Observer pattern
 * - Interface for notification system
 * 
 * @author Sudharma
 */
public interface AppointmentObserver {

    /**
     * Update method called when appointment state changes
     * 
     * @param appointment The appointment that changed
     * @param action      The action performed (BOOKED, CONFIRMED, CANCELLED, etc.)
     */
    void update(Appointment appointment, String action);
}
