package com.airtribe.meditrack.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Appointment Request DTO
 * 
 * @author Sudharma
 */
@Data
public class AppointmentRequestDTO {

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Appointment date and time is required")
    @Future(message = "Appointment must be in the future")
    private LocalDateTime appointmentDateTime;

    @NotNull(message = "Duration is required")
    @Min(value = 15, message = "Duration must be at least 15 minutes")
    @Max(value = 120, message = "Duration must not exceed 120 minutes")
    private Integer durationMinutes = 30;

    @Size(max = 1000, message = "Symptoms must not exceed 1000 characters")
    private String symptoms;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
}
