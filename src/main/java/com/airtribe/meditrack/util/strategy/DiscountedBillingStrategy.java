package com.airtribe.meditrack.util.strategy;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Bill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Discounted Billing Strategy - Concrete Strategy
 * 
 * Demonstrates:
 * - Strategy pattern with discount logic
 * - Billing for senior citizens, staff, etc.
 * 
 * @author Sudharma
 */
@Component
public class DiscountedBillingStrategy implements BillingStrategy {

    @Value("${meditrack.tax-rate:0.18}")
    private Double taxRate;

    private static final Double DISCOUNT_PERCENTAGE = 20.0; // 20% discount

    @Override
    public Bill calculateBill(Appointment appointment) {
        Bill bill = new Bill();
        bill.setAppointment(appointment);

        // Standard consultation fee
        Double consultationFee = appointment.getDoctor().getConsultationFee();
        bill.setConsultationFee(consultationFee);
        bill.setMedicineCharges(0.0);
        bill.setTestCharges(0.0);
        bill.setOtherCharges(0.0);
        bill.setTaxRate(taxRate);

        // Apply discount
        Double discountAmount = consultationFee * (DISCOUNT_PERCENTAGE / 100);
        bill.setDiscount(discountAmount);
        bill.setPaid(false);

        return bill;
    }

    @Override
    public String getStrategyName() {
        return "Discounted Billing (20% off)";
    }
}
