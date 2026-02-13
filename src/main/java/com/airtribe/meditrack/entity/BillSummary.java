package com.airtribe.meditrack.entity;

import java.time.LocalDateTime;

/**
 * BillSummary - Immutable Class
 * 
 * Demonstrates:
 * - Immutability (final class, final fields, no setters)
 * - Thread-safety through immutability
 * - Defensive copying
 * - Value object pattern
 * 
 * This class is NOT a JPA entity - it's a pure immutable value object
 * used for read-only bill summaries and reports.
 * 
 * @author Sudharma
 */
public final class BillSummary {

    private final Long billId;
    private final String patientName;
    private final String doctorName;
    private final Double subtotal;
    private final Double tax;
    private final Double discount;
    private final Double total;
    private final Boolean paid;
    private final LocalDateTime createdAt;

    /**
     * Constructor - all fields must be provided at creation
     * 
     * @param billId      Bill ID
     * @param patientName Patient name
     * @param doctorName  Doctor name
     * @param subtotal    Subtotal amount
     * @param tax         Tax amount
     * @param discount    Discount amount
     * @param total       Total amount
     * @param paid        Payment status
     * @param createdAt   Creation timestamp
     */
    public BillSummary(Long billId, String patientName, String doctorName,
            Double subtotal, Double tax, Double discount,
            Double total, Boolean paid, LocalDateTime createdAt) {
        // Defensive copying for mutable objects
        this.billId = billId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.subtotal = subtotal;
        this.tax = tax;
        this.discount = discount;
        this.total = total;
        this.paid = paid;
        this.createdAt = createdAt != null ? LocalDateTime.from(createdAt) : null;
    }

    // Only getters - no setters (immutability)

    public Long getBillId() {
        return billId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public Double getTax() {
        return tax;
    }

    public Double getDiscount() {
        return discount;
    }

    public Double getTotal() {
        return total;
    }

    public Boolean isPaid() {
        return paid;
    }

    /**
     * Defensive copy for mutable field
     */
    public LocalDateTime getCreatedAt() {
        return createdAt != null ? LocalDateTime.from(createdAt) : null;
    }

    /**
     * Calculate tax percentage
     */
    public Double getTaxPercentage() {
        return subtotal > 0 ? (tax / subtotal) * 100 : 0.0;
    }

    /**
     * Calculate discount percentage
     */
    public Double getDiscountPercentage() {
        return subtotal > 0 ? (discount / subtotal) * 100 : 0.0;
    }

    /**
     * Get formatted summary
     */
    public String getFormattedSummary() {
        return String.format(
                "Bill Summary:\n" +
                        "Bill ID: %d\n" +
                        "Patient: %s\n" +
                        "Doctor: %s\n" +
                        "Subtotal: ₹%.2f\n" +
                        "Tax (%.1f%%): ₹%.2f\n" +
                        "Discount: ₹%.2f\n" +
                        "Total: ₹%.2f\n" +
                        "Status: %s\n" +
                        "Created: %s",
                billId, patientName, doctorName,
                subtotal, getTaxPercentage(), tax,
                discount, total,
                paid ? "PAID" : "UNPAID",
                createdAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        BillSummary that = (BillSummary) o;
        return billId != null && billId.equals(that.billId);
    }

    @Override
    public int hashCode() {
        return billId != null ? billId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format("BillSummary[ID=%d, Patient=%s, Total=₹%.2f, Paid=%s]",
                billId, patientName, total, paid ? "Yes" : "No");
    }
}
