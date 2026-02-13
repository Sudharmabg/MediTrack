package com.airtribe.meditrack.repository;

import com.airtribe.meditrack.entity.Bill;
import com.airtribe.meditrack.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Bill Repository
 * 
 * Demonstrates:
 * - Spring Data JPA Repository
 * - Aggregate queries
 * 
 * @author Sudharma
 */
@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    /**
     * Find bill by appointment
     */
    Optional<Bill> findByAppointment(Appointment appointment);

    /**
     * Find unpaid bills
     */
    List<Bill> findByPaidFalse();

    /**
     * Find paid bills
     */
    List<Bill> findByPaidTrue();

    /**
     * Find bills by payment method
     */
    List<Bill> findByPaymentMethod(String paymentMethod);

    /**
     * Find bills by date range
     */
    @Query("SELECT b FROM Bill b WHERE " +
            "b.createdAt >= :startDate AND b.createdAt <= :endDate")
    List<Bill> findByDateRange(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * Calculate total revenue
     */
    @Query("SELECT SUM(b.consultationFee + b.medicineCharges + b.testCharges + " +
            "b.otherCharges + (b.consultationFee + b.medicineCharges + b.testCharges + " +
            "b.otherCharges) * b.taxRate - b.discount) FROM Bill b WHERE b.paid = true")
    Double calculateTotalRevenue();

    /**
     * Calculate revenue for date range
     */
    @Query("SELECT SUM(b.consultationFee + b.medicineCharges + b.testCharges + " +
            "b.otherCharges + (b.consultationFee + b.medicineCharges + b.testCharges + " +
            "b.otherCharges) * b.taxRate - b.discount) FROM Bill b " +
            "WHERE b.paid = true AND b.paidAt >= :startDate AND b.paidAt <= :endDate")
    Double calculateRevenueByDateRange(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * Count unpaid bills
     */
    long countByPaidFalse();

    /**
     * Count paid bills
     */
    long countByPaidTrue();
}
