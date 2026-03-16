package com.airtribe.meditrack.interfaces;

/**
 * Payable Interface
 * 
 * Demonstrates:
 * - Interface for payment processing
 * - Contract for billable entities
 * - Business logic abstraction
 * 
 * @author Sudharma
 */
public interface Payable {

    /**
     * Calculate subtotal (before tax and discount)
     * 
     * @return Subtotal amount
     */
    Double calculateSubtotal();

    /**
     * Calculate tax amount
     * 
     * @return Tax amount
     */
    Double calculateTax();

    /**
     * Calculate total amount (subtotal + tax - discount)
     * 
     * @return Total amount
     */
    Double calculateTotal();

    /**
     * Process payment
     * 
     * @param paymentMethod Payment method (Cash, Card, UPI, etc.)
     * @param transactionId Transaction ID
     */
    void processPayment(String paymentMethod, String transactionId);

    /**
     * Default method - check if paid
     * 
     * @return true if payment is processed
     */
    default boolean isPaid() {
        return false;
    }

    /**
     * Default method - get formatted total
     * 
     * @return Formatted total with currency
     */
    default String getFormattedTotal() {
        return String.format("₹%.2f", calculateTotal());
    }
}
