package com.airtribe.meditrack.util.strategy;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Bill;

/**
 * Billing Strategy Interface - Strategy Pattern
 * 
 * Demonstrates:
 * - Strategy pattern for different billing algorithms
 * - Interface for billing calculation
 * 
 * @author Sudharma
 */
public interface BillingStrategy {

    /**
     * Calculate bill for an appointment
     * 
     * @param appointment The appointment to bill
     * @return Calculated bill
     */
    Bill calculateBill(Appointment appointment);

    /**
     * Get strategy name
     * 
     * @return Strategy name
     */
    String getStrategyName();
}
