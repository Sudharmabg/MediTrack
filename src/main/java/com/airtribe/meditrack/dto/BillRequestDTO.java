package com.airtribe.meditrack.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * Bill Request DTO
 * 
 * @author Sudharma
 */
@Data
public class BillRequestDTO {

    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;

    @NotNull(message = "Consultation fee is required")
    @DecimalMin(value = "0.0", message = "Consultation fee cannot be negative")
    private Double consultationFee;

    @DecimalMin(value = "0.0", message = "Medicine charges cannot be negative")
    private Double medicineCharges = 0.0;

    @DecimalMin(value = "0.0", message = "Test charges cannot be negative")
    private Double testCharges = 0.0;

    @DecimalMin(value = "0.0", message = "Other charges cannot be negative")
    private Double otherCharges = 0.0;

    @NotNull(message = "Tax rate is required")
    @DecimalMin(value = "0.0", message = "Tax rate cannot be negative")
    @DecimalMax(value = "1.0", message = "Tax rate must not exceed 1.0 (100%)")
    private Double taxRate = 0.18;

    @DecimalMin(value = "0.0", message = "Discount cannot be negative")
    private Double discount = 0.0;
}
