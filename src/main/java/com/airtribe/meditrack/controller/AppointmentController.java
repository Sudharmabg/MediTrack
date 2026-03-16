package com.airtribe.meditrack.controller;

import com.airtribe.meditrack.dto.AppointmentRequestDTO;
import com.airtribe.meditrack.dto.AppointmentResponseDTO;
import com.airtribe.meditrack.enums.AppointmentStatus;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.util.observer.EmailNotificationObserver;
import com.airtribe.meditrack.util.observer.SmsNotificationObserver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
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
 * Appointment REST Controller
 * 
 * Demonstrates:
 * - REST API for appointment management
 * - Observer pattern integration
 * - Complex business logic endpoints
 * 
 * @author Sudharma
 */
@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Appointment Management", description = "APIs for managing appointments")
@ApiResponses(value = {
    @ApiResponse(responseCode = "400", description = "Bad request - Invalid input"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
})
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final EmailNotificationObserver emailObserver;
    private final SmsNotificationObserver smsObserver;

    /**
     * Register observers on initialization
     * Demonstrates: Observer pattern setup
     */
    @PostConstruct
    public void init() {
        appointmentService.registerObserver(emailObserver);
        appointmentService.registerObserver(smsObserver);
        log.info("Appointment observers registered");
    }

    @PostMapping
    @Operation(summary = "Book a new appointment", description = "Creates a new appointment")
    public ResponseEntity<AppointmentResponseDTO> bookAppointment(
            @Valid @RequestBody AppointmentRequestDTO requestDTO) {
        log.info("REST API: Book appointment for patient {} with doctor {}",
                requestDTO.getPatientId(), requestDTO.getDoctorId());
        AppointmentResponseDTO response = appointmentService.bookAppointment(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get appointment by ID", description = "Retrieves an appointment by ID")
    public ResponseEntity<AppointmentResponseDTO> getAppointmentById(@PathVariable Long id) {
        log.info("REST API: Get appointment by ID - {}", id);
        AppointmentResponseDTO response = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all appointments", description = "Retrieves all appointments")
    public ResponseEntity<List<AppointmentResponseDTO>> getAllAppointments() {
        log.info("REST API: Get all appointments");
        List<AppointmentResponseDTO> response = appointmentService.getAllAppointments();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get appointments by doctor", description = "Retrieves all appointments for a doctor")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByDoctor(@PathVariable Long doctorId) {
        log.info("REST API: Get appointments by doctor - {}", doctorId);
        List<AppointmentResponseDTO> response = appointmentService.getAppointmentsByDoctor(doctorId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get appointments by patient", description = "Retrieves all appointments for a patient")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByPatient(@PathVariable Long patientId) {
        log.info("REST API: Get appointments by patient - {}", patientId);
        List<AppointmentResponseDTO> response = appointmentService.getAppointmentsByPatient(patientId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/doctor/{doctorId}/upcoming")
    @Operation(summary = "Get upcoming appointments by doctor", description = "Retrieves upcoming appointments for a doctor")
    public ResponseEntity<List<AppointmentResponseDTO>> getUpcomingAppointmentsByDoctor(@PathVariable Long doctorId) {
        log.info("REST API: Get upcoming appointments by doctor - {}", doctorId);
        List<AppointmentResponseDTO> response = appointmentService.getUpcomingAppointmentsByDoctor(doctorId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patient/{patientId}/upcoming")
    @Operation(summary = "Get upcoming appointments by patient", description = "Retrieves upcoming appointments for a patient")
    public ResponseEntity<List<AppointmentResponseDTO>> getUpcomingAppointmentsByPatient(@PathVariable Long patientId) {
        log.info("REST API: Get upcoming appointments by patient - {}", patientId);
        List<AppointmentResponseDTO> response = appointmentService.getUpcomingAppointmentsByPatient(patientId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/today")
    @Operation(summary = "Get today's appointments", description = "Retrieves all appointments for today")
    public ResponseEntity<List<AppointmentResponseDTO>> getTodaysAppointments() {
        log.info("REST API: Get today's appointments");
        List<AppointmentResponseDTO> response = appointmentService.getTodaysAppointments();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get appointments by status", description = "Retrieves appointments by status")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointmentsByStatus(
            @PathVariable AppointmentStatus status) {
        log.info("REST API: Get appointments by status - {}", status);
        List<AppointmentResponseDTO> response = appointmentService.getAppointmentsByStatus(status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get appointment statistics", description = "Retrieves statistics about appointments")
    public ResponseEntity<AppointmentService.AppointmentStatistics> getAppointmentStatistics() {
        log.info("REST API: Get appointment statistics");
        AppointmentService.AppointmentStatistics response = appointmentService.getAppointmentStatistics();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/confirm")
    @Operation(summary = "Confirm appointment", description = "Confirms an appointment")
    public ResponseEntity<AppointmentResponseDTO> confirmAppointment(@PathVariable Long id) {
        log.info("REST API: Confirm appointment - {}", id);
        AppointmentResponseDTO response = appointmentService.confirmAppointment(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel appointment", description = "Cancels an appointment")
    public ResponseEntity<AppointmentResponseDTO> cancelAppointment(@PathVariable Long id) {
        log.info("REST API: Cancel appointment - {}", id);
        AppointmentResponseDTO response = appointmentService.cancelAppointment(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/start")
    @Operation(summary = "Start appointment", description = "Marks appointment as in progress")
    public ResponseEntity<AppointmentResponseDTO> startAppointment(@PathVariable Long id) {
        log.info("REST API: Start appointment - {}", id);
        AppointmentResponseDTO response = appointmentService.startAppointment(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/complete")
    @Operation(summary = "Complete appointment", description = "Marks appointment as completed")
    public ResponseEntity<AppointmentResponseDTO> completeAppointment(
            @PathVariable Long id,
            @RequestParam String diagnosis,
            @RequestParam String prescription) {
        log.info("REST API: Complete appointment - {}", id);
        AppointmentResponseDTO response = appointmentService.completeAppointment(id, diagnosis, prescription);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/reschedule")
    @Operation(summary = "Reschedule appointment", description = "Reschedules an appointment to a new date/time")
    public ResponseEntity<AppointmentResponseDTO> rescheduleAppointment(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newDateTime) {
        log.info("REST API: Reschedule appointment - {} to {}", id, newDateTime);
        AppointmentResponseDTO response = appointmentService.rescheduleAppointment(id, newDateTime);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete appointment", description = "Deletes an appointment")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        log.info("REST API: Delete appointment - {}", id);
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
