package com.airtribe.meditrack.util.strategy;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Bill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Premium Billing Strategy - Concrete Strategy
 * 
 * Demonstrates:
 * - Another concrete implementation of Strategy pattern
 * - Premium billing with additional charges
 * 
 * @author Sudharma
 */
@Component
public class PremiumBillingStrategy implements BillingStrategy {

    @Value("${meditrack.tax-rate:0.18}")
    private Double taxRate;

    private static final Double PREMIUM_MULTIPLIER = 1.5;
    private static final Double PREMIUM_SERVICE_CHARGE = 500.0;

    @Override
    public Bill calculateBill(Appointment appointment) {
        Bill bill = new Bill();
        bill.setAppointment(appointment);

        // Premium consultation fee (1.5x standard)
        Double premiumFee = appointment.getDoctor().getConsultationFee() * PREMIUM_MULTIPLIER;
        bill.setConsultationFee(premiumFee);
        bill.setMedicineCharges(0.0);
        bill.setTestCharges(0.0);
        bill.setOtherCharges(PREMIUM_SERVICE_CHARGE); // Premium service charge
        bill.setTaxRate(taxRate);
        bill.setDiscount(0.0);
        bill.setPaid(false);

        return bill;
    }

    @Override
    public String getStrategyName() {
        return "Premium Billing";
    }
}
