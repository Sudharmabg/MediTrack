package com.airtribe.meditrack.util.strategy;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Bill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Standard Billing Strategy - Concrete Strategy
 * 
 * Demonstrates:
 * - Concrete implementation of Strategy pattern
 * - Standard billing calculation
 * 
 * @author Sudharma
 */
@Component
public class StandardBillingStrategy implements BillingStrategy {

    @Value("${meditrack.tax-rate:0.18}")
    private Double taxRate;

    @Override
    public Bill calculateBill(Appointment appointment) {
        Bill bill = new Bill();
        bill.setAppointment(appointment);

        // Standard consultation fee from doctor
        bill.setConsultationFee(appointment.getDoctor().getConsultationFee());
        bill.setMedicineCharges(0.0);
        bill.setTestCharges(0.0);
        bill.setOtherCharges(0.0);
        bill.setTaxRate(taxRate);
        bill.setDiscount(0.0);
        bill.setPaid(false);

        return bill;
    }

    @Override
    public String getStrategyName() {
        return "Standard Billing";
    }
}
