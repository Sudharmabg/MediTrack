package com.airtribe.meditrack.controller;

import com.airtribe.meditrack.dto.BillRequestDTO;
import com.airtribe.meditrack.dto.BillResponseDTO;
import com.airtribe.meditrack.entity.BillSummary;
import com.airtribe.meditrack.service.BillService;
import com.airtribe.meditrack.util.strategy.DiscountedBillingStrategy;
import com.airtribe.meditrack.util.strategy.PremiumBillingStrategy;
import com.airtribe.meditrack.util.strategy.StandardBillingStrategy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Bill REST Controller
 * 
 * Demonstrates:
 * - REST API for billing management
 * - Factory and Strategy pattern integration
 * - Payment processing
 * - Analytics endpoints
 * 
 * @author Sudharma
 */
@RestController
@RequestMapping("/api/v1/bills")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Bill Management", description = "APIs for managing bills and payments")
@ApiResponses(value = {
    @ApiResponse(responseCode = "400", description = "Bad request - Invalid input"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
})
public class BillController {

    private final BillService billService;
    private final StandardBillingStrategy standardStrategy;
    private final PremiumBillingStrategy premiumStrategy;
    private final DiscountedBillingStrategy discountedStrategy;

    @PostMapping
    @Operation(summary = "Generate a new bill", description = "Creates a new bill for an appointment")
    public ResponseEntity<BillResponseDTO> generateBill(@Valid @RequestBody BillRequestDTO requestDTO) {
        log.info("REST API: Generate bill for appointment {}", requestDTO.getAppointmentId());
        BillResponseDTO response = billService.generateBill(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/appointment/{appointmentId}/standard")
    @Operation(summary = "Generate bill with standard strategy", description = "Creates a bill using standard billing strategy")
    public ResponseEntity<BillResponseDTO> generateStandardBill(@PathVariable Long appointmentId) {
        log.info("REST API: Generate standard bill for appointment {}", appointmentId);
        BillResponseDTO response = billService.generateBillWithStrategy(appointmentId, standardStrategy);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/appointment/{appointmentId}/premium")
    @Operation(summary = "Generate bill with premium strategy", description = "Creates a bill using premium billing strategy")
    public ResponseEntity<BillResponseDTO> generatePremiumBill(@PathVariable Long appointmentId) {
        log.info("REST API: Generate premium bill for appointment {}", appointmentId);
        BillResponseDTO response = billService.generateBillWithStrategy(appointmentId, premiumStrategy);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/appointment/{appointmentId}/discounted")
    @Operation(summary = "Generate bill with discounted strategy", description = "Creates a bill using discounted billing strategy")
    public ResponseEntity<BillResponseDTO> generateDiscountedBill(@PathVariable Long appointmentId) {
        log.info("REST API: Generate discounted bill for appointment {}", appointmentId);
        BillResponseDTO response = billService.generateBillWithStrategy(appointmentId, discountedStrategy);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get bill by ID", description = "Retrieves a bill by ID")
    public ResponseEntity<BillResponseDTO> getBillById(@PathVariable Long id) {
        log.info("REST API: Get bill by ID - {}", id);
        BillResponseDTO response = billService.getBillById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/appointment/{appointmentId}")
    @Operation(summary = "Get bill by appointment", description = "Retrieves bill for an appointment")
    public ResponseEntity<BillResponseDTO> getBillByAppointment(@PathVariable Long appointmentId) {
        log.info("REST API: Get bill by appointment - {}", appointmentId);
        BillResponseDTO response = billService.getBillByAppointment(appointmentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all bills", description = "Retrieves all bills")
    public ResponseEntity<List<BillResponseDTO>> getAllBills() {
        log.info("REST API: Get all bills");
        List<BillResponseDTO> response = billService.getAllBills();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/unpaid")
    @Operation(summary = "Get unpaid bills", description = "Retrieves all unpaid bills")
    public ResponseEntity<List<BillResponseDTO>> getUnpaidBills() {
        log.info("REST API: Get unpaid bills");
        List<BillResponseDTO> response = billService.getUnpaidBills();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paid")
    @Operation(summary = "Get paid bills", description = "Retrieves all paid bills")
    public ResponseEntity<List<BillResponseDTO>> getPaidBills() {
        log.info("REST API: Get paid bills");
        List<BillResponseDTO> response = billService.getPaidBills();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/payment-method/{paymentMethod}")
    @Operation(summary = "Get bills by payment method", description = "Retrieves bills by payment method")
    public ResponseEntity<List<BillResponseDTO>> getBillsByPaymentMethod(@PathVariable String paymentMethod) {
        log.info("REST API: Get bills by payment method - {}", paymentMethod);
        List<BillResponseDTO> response = billService.getBillsByPaymentMethod(paymentMethod);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get billing statistics", description = "Retrieves billing statistics")
    public ResponseEntity<BillService.BillingStatistics> getBillingStatistics() {
        log.info("REST API: Get billing statistics");
        BillService.BillingStatistics response = billService.getBillingStatistics();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/revenue/total")
    @Operation(summary = "Get total revenue", description = "Calculates total revenue from all paid bills")
    public ResponseEntity<Double> getTotalRevenue() {
        log.info("REST API: Get total revenue");
        Double revenue = billService.calculateTotalRevenue();
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/revenue/date-range")
    @Operation(summary = "Get revenue by date range", description = "Calculates revenue within date range")
    public ResponseEntity<Double> getRevenueByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("REST API: Get revenue from {} to {}", startDate, endDate);
        Double revenue = billService.calculateRevenueByDateRange(startDate, endDate);
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/top-doctors")
    @Operation(summary = "Get top revenue generating doctors", description = "Retrieves top doctors by revenue")
    public ResponseEntity<List<BillService.DoctorRevenue>> getTopDoctors(
            @RequestParam(defaultValue = "10") int limit) {
        log.info("REST API: Get top {} revenue generating doctors", limit);
        List<BillService.DoctorRevenue> response = billService.getTopRevenueGeneratingDoctors(limit);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/summary")
    @Operation(summary = "Get bill summary", description = "Generates immutable bill summary")
    public ResponseEntity<BillSummary> getBillSummary(@PathVariable Long id) {
        log.info("REST API: Get bill summary - {}", id);
        BillSummary summary = billService.generateBillSummary(id);
        return ResponseEntity.ok(summary);
    }

    @PatchMapping("/{id}/pay")
    @Operation(summary = "Process payment", description = "Processes payment for a bill")
    public ResponseEntity<BillResponseDTO> processPayment(
            @PathVariable Long id,
            @RequestParam String paymentMethod,
            @RequestParam String transactionId) {
        log.info("REST API: Process payment for bill {} via {}", id, paymentMethod);
        BillResponseDTO response = billService.processPayment(id, paymentMethod, transactionId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/discount")
    @Operation(summary = "Apply discount", description = "Applies discount amount to a bill")
    public ResponseEntity<BillResponseDTO> applyDiscount(
            @PathVariable Long id,
            @RequestParam Double discountAmount) {
        log.info("REST API: Apply discount of ₹{} to bill {}", discountAmount, id);
        BillResponseDTO response = billService.applyDiscount(id, discountAmount);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/discount-percentage")
    @Operation(summary = "Apply percentage discount", description = "Applies percentage discount to a bill")
    public ResponseEntity<BillResponseDTO> applyPercentageDiscount(
            @PathVariable Long id,
            @RequestParam Double percentage) {
        log.info("REST API: Apply {}% discount to bill {}", percentage, id);
        BillResponseDTO response = billService.applyPercentageDiscount(id, percentage);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete bill", description = "Deletes an unpaid bill")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        log.info("REST API: Delete bill - {}", id);
        billService.deleteBill(id);
        return ResponseEntity.noContent().build();
    }
}
