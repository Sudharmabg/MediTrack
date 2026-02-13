package com.airtribe.meditrack.dto;

import com.airtribe.meditrack.enums.AppointmentStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Appointment Response DTO
 * 
 * @author Sudharma
 */
@Data
public class AppointmentResponseDTO {

    private Long id;
    private Long doctorId;
    private String doctorName;
    private String doctorSpecialization;
    private Long patientId;
    private String patientName;
    private LocalDateTime appointmentDateTime;
    private Integer durationMinutes;
    private AppointmentStatus status;
    private String statusDisplayName;
    private String symptoms;
    private String diagnosis;
    private String prescription;
    private String notes;
    private Boolean isPast;
    private Boolean isUpcoming;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
