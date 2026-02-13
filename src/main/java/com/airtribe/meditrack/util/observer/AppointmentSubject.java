package com.airtribe.meditrack.util.observer;

import com.airtribe.meditrack.entity.Appointment;

/**
 * Subject Interface - Observer Pattern
 * 
 * Demonstrates:
 * - Subject/Observable in Observer pattern
 * - Interface for managing observers
 * 
 * @author Sudharma
 */
public interface AppointmentSubject {

    /**
     * Register an observer
     * 
     * @param observer Observer to register
     */
    void registerObserver(AppointmentObserver observer);

    /**
     * Remove an observer
     * 
     * @param observer Observer to remove
     */
    void removeObserver(AppointmentObserver observer);

    /**
     * Notify all observers of a change
     * 
     * @param appointment The appointment that changed
     * @param action      The action performed
     */
    void notifyObservers(Appointment appointment, String action);
}
