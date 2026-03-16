package com.airtribe.meditrack.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Bill Response DTO
 * 
 * @author Sudharma
 */
@Data
public class BillResponseDTO {

    private Long id;
    private Long appointmentId;
    private String patientName;
    private String doctorName;
    private Double consultationFee;
    private Double medicineCharges;
    private Double testCharges;
    private Double otherCharges;
    private Double subtotal;
    private Double taxRate;
    private Double taxAmount;
    private Double discount;
    private Double total;
    private Boolean paid;
    private String paymentMethod;
    private String transactionId;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
}
