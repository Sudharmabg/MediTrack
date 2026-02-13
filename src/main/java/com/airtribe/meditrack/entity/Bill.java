package com.airtribe.meditrack.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.airtribe.meditrack.interfaces.Payable;

import java.time.LocalDateTime;

/**
 * Bill Entity
 * 
 * Demonstrates:
 * - Interface implementation (Payable)
 * - Business logic for tax calculation
 * - One-to-One relationship with Appointment
 * 
 * @author Sudharma
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bills")
public class Bill implements Payable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;

    @Column(nullable = false)
    private Double consultationFee;

    @Column(nullable = false)
    private Double medicineCharges = 0.0;

    @Column(nullable = false)
    private Double testCharges = 0.0;

    @Column(nullable = false)
    private Double otherCharges = 0.0;

    @Column(nullable = false)
    private Double taxRate;

    @Column(nullable = false)
    private Double discount = 0.0;

    @Column(nullable = false)
    private Boolean paid = false;

    @Column
    private String paymentMethod;

    @Column
    private String transactionId;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime paidAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    /**
     * Implement Payable interface - calculate subtotal
     */
    @Override
    public Double calculateSubtotal() {
        return consultationFee + medicineCharges + testCharges + otherCharges;
    }

    /**
     * Implement Payable interface - calculate tax
     */
    @Override
    public Double calculateTax() {
        return calculateSubtotal() * taxRate;
    }

    /**
     * Implement Payable interface - calculate total
     */
    @Override
    public Double calculateTotal() {
        return calculateSubtotal() + calculateTax() - discount;
    }

    /**
     * Implement Payable interface - process payment
     */
    @Override
    public void processPayment(String paymentMethod, String transactionId) {
        if (paid) {
            throw new IllegalStateException("Bill is already paid");
        }

        this.paid = true;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.paidAt = LocalDateTime.now();
    }

    /**
     * Apply discount
     */
    public void applyDiscount(Double discountAmount) {
        if (discountAmount < 0 || discountAmount > calculateSubtotal()) {
            throw new IllegalArgumentException("Invalid discount amount");
        }
        this.discount = discountAmount;
    }

    /**
     * Apply percentage discount
     */
    public void applyPercentageDiscount(Double percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }
        this.discount = calculateSubtotal() * (percentage / 100);
    }

    /**
     * Generate bill summary (immutable)
     */
    public BillSummary generateSummary() {
        return new BillSummary(
                id,
                appointment.getPatient().getFullName(),
                appointment.getDoctor().getFullName(),
                calculateSubtotal(),
                calculateTax(),
                discount,
                calculateTotal(),
                paid,
                createdAt);
    }

    @Override
    public String toString() {
        return String.format("Bill [ID: %d, Total: ₹%.2f, Paid: %s]",
                id, calculateTotal(), paid ? "Yes" : "No");
    }
}
