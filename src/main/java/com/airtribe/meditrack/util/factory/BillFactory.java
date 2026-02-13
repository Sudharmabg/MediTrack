package com.airtribe.meditrack.util.factory;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Bill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Bill Factory - Factory Pattern
 * 
 * Demonstrates:
 * - Factory pattern for object creation
 * - Encapsulation of creation logic
 * - Different bill types
 * 
 * @author Sudharma
 */
@Component
public class BillFactory {

    @Value("${meditrack.tax-rate:0.18}")
    private Double defaultTaxRate;

    /**
     * Create a standard bill
     * 
     * @param appointment     Appointment
     * @param consultationFee Consultation fee
     * @param medicineCharges Medicine charges
     * @param testCharges     Test charges
     * @param otherCharges    Other charges
     * @param taxRate         Tax rate
     * @param discount        Discount
     * @return Created bill
     */
    public Bill createBill(Appointment appointment, Double consultationFee,
            Double medicineCharges, Double testCharges,
            Double otherCharges, Double taxRate, Double discount) {

        Bill bill = new Bill();
        bill.setAppointment(appointment);
        bill.setConsultationFee(consultationFee);
        bill.setMedicineCharges(medicineCharges != null ? medicineCharges : 0.0);
        bill.setTestCharges(testCharges != null ? testCharges : 0.0);
        bill.setOtherCharges(otherCharges != null ? otherCharges : 0.0);
        bill.setTaxRate(taxRate != null ? taxRate : defaultTaxRate);
        bill.setDiscount(discount != null ? discount : 0.0);
        bill.setPaid(false);

        return bill;
    }

    /**
     * Create a simple consultation bill
     * 
     * @param appointment     Appointment
     * @param consultationFee Consultation fee
     * @return Created bill
     */
    public Bill createConsultationBill(Appointment appointment, Double consultationFee) {
        return createBill(appointment, consultationFee, 0.0, 0.0, 0.0, defaultTaxRate, 0.0);
    }

    /**
     * Create a comprehensive bill (with tests and medicines)
     * 
     * @param appointment     Appointment
     * @param consultationFee Consultation fee
     * @param medicineCharges Medicine charges
     * @param testCharges     Test charges
     * @return Created bill
     */
    public Bill createComprehensiveBill(Appointment appointment, Double consultationFee,
            Double medicineCharges, Double testCharges) {
        return createBill(appointment, consultationFee, medicineCharges,
                testCharges, 0.0, defaultTaxRate, 0.0);
    }

    /**
     * Create an emergency bill (higher charges, no discount)
     * 
     * @param appointment     Appointment
     * @param consultationFee Base consultation fee
     * @return Created bill with emergency surcharge
     */
    public Bill createEmergencyBill(Appointment appointment, Double consultationFee) {
        // Emergency surcharge: 50% extra
        Double emergencyFee = consultationFee * 1.5;
        return createBill(appointment, emergencyFee, 0.0, 0.0, 0.0, defaultTaxRate, 0.0);
    }

    /**
     * Create a discounted bill (for senior citizens, staff, etc.)
     * 
     * @param appointment        Appointment
     * @param consultationFee    Consultation fee
     * @param discountPercentage Discount percentage
     * @return Created bill with discount
     */
    public Bill createDiscountedBill(Appointment appointment, Double consultationFee,
            Double discountPercentage) {
        Bill bill = createBill(appointment, consultationFee, 0.0, 0.0, 0.0, defaultTaxRate, 0.0);
        bill.applyPercentageDiscount(discountPercentage);
        return bill;
    }

    /**
     * Create a package bill (fixed price for consultation + tests)
     * 
     * @param appointment Appointment
     * @param packageType Package type (BASIC, STANDARD, PREMIUM)
     * @return Created bill
     */
    public Bill createPackageBill(Appointment appointment, PackageType packageType) {
        switch (packageType) {
            case BASIC:
                return createBill(appointment, 500.0, 0.0, 200.0, 0.0, defaultTaxRate, 0.0);
            case STANDARD:
                return createBill(appointment, 1000.0, 500.0, 500.0, 0.0, defaultTaxRate, 0.0);
            case PREMIUM:
                return createBill(appointment, 2000.0, 1000.0, 1000.0, 500.0, defaultTaxRate, 0.0);
            default:
                return createConsultationBill(appointment, 500.0);
        }
    }

    /**
     * Package types enum
     */
    public enum PackageType {
        BASIC,
        STANDARD,
        PREMIUM
    }
}
