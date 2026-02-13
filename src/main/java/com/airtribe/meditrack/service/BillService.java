package com.airtribe.meditrack.service;

import com.airtribe.meditrack.dto.BillRequestDTO;
import com.airtribe.meditrack.dto.BillResponseDTO;
import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Bill;
import com.airtribe.meditrack.entity.BillSummary;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.exception.ResourceNotFoundException;
import com.airtribe.meditrack.repository.AppointmentRepository;
import com.airtribe.meditrack.repository.BillRepository;
import com.airtribe.meditrack.util.factory.BillFactory;
import com.airtribe.meditrack.util.strategy.BillingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Bill Service
 * 
 * Demonstrates:
 * - Service layer pattern
 * - Factory pattern (BillFactory)
 * - Strategy pattern (BillingStrategy)
 * - Business logic for billing
 * - Java 8+ Streams for analytics
 * 
 * @author Sudharma
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BillService {

    private final BillRepository billRepository;
    private final AppointmentRepository appointmentRepository;
    private final BillFactory billFactory;
    private final ModelMapper modelMapper;

    /**
     * Generate bill for an appointment
     * 
     * Demonstrates: Factory pattern
     * 
     * @param requestDTO Bill request DTO
     * @return Created bill response DTO
     */
    public BillResponseDTO generateBill(BillRequestDTO requestDTO) {
        log.info("Generating bill for appointment ID: {}", requestDTO.getAppointmentId());

        // Fetch appointment
        Appointment appointment = appointmentRepository.findById(requestDTO.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", requestDTO.getAppointmentId()));

        // Check if bill already exists
        if (billRepository.findByAppointment(appointment).isPresent()) {
            throw new InvalidDataException("appointment", "Bill already exists for this appointment");
        }

        // Use Factory pattern to create bill
        Bill bill = billFactory.createBill(
                appointment,
                requestDTO.getConsultationFee(),
                requestDTO.getMedicineCharges(),
                requestDTO.getTestCharges(),
                requestDTO.getOtherCharges(),
                requestDTO.getTaxRate(),
                requestDTO.getDiscount());

        Bill savedBill = billRepository.save(bill);
        log.info("Bill generated successfully with ID: {}", savedBill.getId());

        return mapToResponseDTO(savedBill);
    }

    /**
     * Generate bill with billing strategy
     * 
     * Demonstrates: Strategy pattern
     * 
     * @param appointmentId   Appointment ID
     * @param billingStrategy Billing strategy to use
     * @return Created bill
     */
    public BillResponseDTO generateBillWithStrategy(Long appointmentId, BillingStrategy billingStrategy) {
        log.info("Generating bill for appointment ID: {} using strategy: {}",
                appointmentId, billingStrategy.getClass().getSimpleName());

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", appointmentId));

        // Check if bill already exists
        if (billRepository.findByAppointment(appointment).isPresent()) {
            throw new InvalidDataException("appointment", "Bill already exists for this appointment");
        }

        // Use strategy to calculate bill
        Bill bill = billingStrategy.calculateBill(appointment);

        Bill savedBill = billRepository.save(bill);
        log.info("Bill generated successfully with ID: {}", savedBill.getId());

        return mapToResponseDTO(savedBill);
    }

    /**
     * Get bill by ID
     * 
     * @param id Bill ID
     * @return Bill response DTO
     */
    @Transactional(readOnly = true)
    public BillResponseDTO getBillById(Long id) {
        log.info("Fetching bill with ID: {}", id);
        Bill bill = findBillById(id);
        return mapToResponseDTO(bill);
    }

    /**
     * Get bill by appointment
     * 
     * @param appointmentId Appointment ID
     * @return Bill response DTO
     */
    @Transactional(readOnly = true)
    public BillResponseDTO getBillByAppointment(Long appointmentId) {
        log.info("Fetching bill for appointment ID: {}", appointmentId);
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", appointmentId));

        Bill bill = billRepository.findByAppointment(appointment)
                .orElseThrow(() -> new ResourceNotFoundException("Bill for appointment", appointmentId));

        return mapToResponseDTO(bill);
    }

    /**
     * Get all bills
     * 
     * @return List of bill response DTOs
     */
    @Transactional(readOnly = true)
    public List<BillResponseDTO> getAllBills() {
        log.info("Fetching all bills");
        return billRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get unpaid bills
     * 
     * Demonstrates: Stream filtering
     * 
     * @return List of unpaid bills
     */
    @Transactional(readOnly = true)
    public List<BillResponseDTO> getUnpaidBills() {
        log.info("Fetching unpaid bills");
        return billRepository.findByPaidFalse()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get paid bills
     * 
     * @return List of paid bills
     */
    @Transactional(readOnly = true)
    public List<BillResponseDTO> getPaidBills() {
        log.info("Fetching paid bills");
        return billRepository.findByPaidTrue()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Process payment
     * 
     * @param id            Bill ID
     * @param paymentMethod Payment method
     * @param transactionId Transaction ID
     * @return Updated bill
     */
    public BillResponseDTO processPayment(Long id, String paymentMethod, String transactionId) {
        log.info("Processing payment for bill ID: {} via {}", id, paymentMethod);
        Bill bill = findBillById(id);

        bill.processPayment(paymentMethod, transactionId);
        Bill updatedBill = billRepository.save(bill);

        log.info("Payment processed successfully for bill ID: {}", id);
        return mapToResponseDTO(updatedBill);
    }

    /**
     * Apply discount to bill
     * 
     * @param id             Bill ID
     * @param discountAmount Discount amount
     * @return Updated bill
     */
    public BillResponseDTO applyDiscount(Long id, Double discountAmount) {
        log.info("Applying discount of ₹{} to bill ID: {}", discountAmount, id);
        Bill bill = findBillById(id);

        bill.applyDiscount(discountAmount);
        Bill updatedBill = billRepository.save(bill);

        return mapToResponseDTO(updatedBill);
    }

    /**
     * Apply percentage discount
     * 
     * @param id         Bill ID
     * @param percentage Discount percentage
     * @return Updated bill
     */
    public BillResponseDTO applyPercentageDiscount(Long id, Double percentage) {
        log.info("Applying {}% discount to bill ID: {}", percentage, id);
        Bill bill = findBillById(id);

        bill.applyPercentageDiscount(percentage);
        Bill updatedBill = billRepository.save(bill);

        return mapToResponseDTO(updatedBill);
    }

    /**
     * Generate bill summary (immutable)
     * 
     * Demonstrates: Immutable class usage
     * 
     * @param id Bill ID
     * @return Bill summary
     */
    @Transactional(readOnly = true)
    public BillSummary generateBillSummary(Long id) {
        log.info("Generating bill summary for bill ID: {}", id);
        Bill bill = findBillById(id);
        return bill.generateSummary();
    }

    /**
     * Calculate total revenue
     * 
     * Demonstrates: Stream aggregate operations
     * 
     * @return Total revenue
     */
    @Transactional(readOnly = true)
    public Double calculateTotalRevenue() {
        log.info("Calculating total revenue");
        Double revenue = billRepository.calculateTotalRevenue();
        return revenue != null ? revenue : 0.0;
    }

    /**
     * Calculate revenue by date range
     * 
     * @param startDate Start date
     * @param endDate   End date
     * @return Revenue in date range
     */
    @Transactional(readOnly = true)
    public Double calculateRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Calculating revenue from {} to {}", startDate, endDate);
        Double revenue = billRepository.calculateRevenueByDateRange(startDate, endDate);
        return revenue != null ? revenue : 0.0;
    }

    /**
     * Get bills by payment method
     * 
     * @param paymentMethod Payment method
     * @return List of bills
     */
    @Transactional(readOnly = true)
    public List<BillResponseDTO> getBillsByPaymentMethod(String paymentMethod) {
        log.info("Fetching bills paid via {}", paymentMethod);
        return billRepository.findByPaymentMethod(paymentMethod)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get billing statistics
     * 
     * Demonstrates: Stream aggregate operations
     * 
     * @return Billing statistics
     */
    @Transactional(readOnly = true)
    public BillingStatistics getBillingStatistics() {
        log.info("Calculating billing statistics");

        List<Bill> allBills = billRepository.findAll();

        long totalBills = allBills.size();
        long paidBills = billRepository.countByPaidTrue();
        long unpaidBills = billRepository.countByPaidFalse();

        Double totalRevenue = calculateTotalRevenue();

        Double averageBillAmount = allBills.stream()
                .mapToDouble(Bill::calculateTotal)
                .average()
                .orElse(0.0);

        Double totalOutstanding = allBills.stream()
                .filter(bill -> !bill.getPaid())
                .mapToDouble(Bill::calculateTotal)
                .sum();

        return new BillingStatistics(totalBills, paidBills, unpaidBills,
                totalRevenue, averageBillAmount, totalOutstanding);
    }

    /**
     * Get top revenue generating doctors
     * 
     * Demonstrates: Complex stream operations with grouping
     * 
     * @param limit Number of top doctors
     * @return List of doctor names with revenue
     */
    @Transactional(readOnly = true)
    public List<DoctorRevenue> getTopRevenueGeneratingDoctors(int limit) {
        log.info("Fetching top {} revenue generating doctors", limit);

        return billRepository.findByPaidTrue()
                .stream()
                .collect(Collectors.groupingBy(
                        bill -> bill.getAppointment().getDoctor().getFullName(),
                        Collectors.summingDouble(Bill::calculateTotal)))
                .entrySet()
                .stream()
                .map(entry -> new DoctorRevenue(entry.getKey(), entry.getValue()))
                .sorted((a, b) -> Double.compare(b.getRevenue(), a.getRevenue()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Delete bill
     * 
     * @param id Bill ID
     */
    public void deleteBill(Long id) {
        log.info("Deleting bill with ID: {}", id);
        Bill bill = findBillById(id);

        if (bill.getPaid()) {
            throw new InvalidDataException("bill", "Cannot delete a paid bill");
        }

        billRepository.delete(bill);
        log.info("Bill deleted successfully");
    }

    // Helper methods

    private Bill findBillById(Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill", id));
    }

    private BillResponseDTO mapToResponseDTO(Bill bill) {
        BillResponseDTO dto = modelMapper.map(bill, BillResponseDTO.class);
        dto.setPatientName(bill.getAppointment().getPatient().getFullName());
        dto.setDoctorName(bill.getAppointment().getDoctor().getFullName());
        dto.setSubtotal(bill.calculateSubtotal());
        dto.setTaxAmount(bill.calculateTax());
        dto.setTotal(bill.calculateTotal());
        return dto;
    }

    /**
     * Inner class for billing statistics
     */
    public static class BillingStatistics {
        private final long totalBills;
        private final long paidBills;
        private final long unpaidBills;
        private final Double totalRevenue;
        private final Double averageBillAmount;
        private final Double totalOutstanding;

        public BillingStatistics(long totalBills, long paidBills, long unpaidBills,
                Double totalRevenue, Double averageBillAmount, Double totalOutstanding) {
            this.totalBills = totalBills;
            this.paidBills = paidBills;
            this.unpaidBills = unpaidBills;
            this.totalRevenue = totalRevenue;
            this.averageBillAmount = averageBillAmount;
            this.totalOutstanding = totalOutstanding;
        }

        // Getters
        public long getTotalBills() {
            return totalBills;
        }

        public long getPaidBills() {
            return paidBills;
        }

        public long getUnpaidBills() {
            return unpaidBills;
        }

        public Double getTotalRevenue() {
            return totalRevenue;
        }

        public Double getAverageBillAmount() {
            return averageBillAmount;
        }

        public Double getTotalOutstanding() {
            return totalOutstanding;
        }

        // Additional getter for test compatibility
        public Double getPendingRevenue() {
            return totalOutstanding;
        }
    }

    /**
     * Inner class for doctor revenue
     */
    public static class DoctorRevenue {
        private final String doctorName;
        private final Double revenue;

        public DoctorRevenue(String doctorName, Double revenue) {
            this.doctorName = doctorName;
            this.revenue = revenue;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public Double getRevenue() {
            return revenue;
        }
    }
}
