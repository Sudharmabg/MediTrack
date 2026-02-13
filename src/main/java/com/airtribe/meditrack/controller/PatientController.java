package com.airtribe.meditrack.controller;

import com.airtribe.meditrack.dto.PatientRequestDTO;
import com.airtribe.meditrack.dto.PatientResponseDTO;
import com.airtribe.meditrack.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Patient REST Controller
 * 
 * @author Sudharma
 */
@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Patient Management", description = "APIs for managing patients")
@ApiResponses(value = {
    @ApiResponse(responseCode = "400", description = "Bad request - Invalid input"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
})
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    @Operation(summary = "Register a new patient", description = "Creates a new patient in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Patient created successfully"),
        @ApiResponse(responseCode = "409", description = "Patient with email already exists")
    })
    public ResponseEntity<PatientResponseDTO> registerPatient(@Valid @RequestBody PatientRequestDTO requestDTO) {
        log.info("REST API: Register patient - {}", requestDTO.getEmail());
        PatientResponseDTO response = patientService.registerPatient(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get patient by ID", description = "Retrieves a patient by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Patient found successfully"),
        @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable Long id) {
        log.info("REST API: Get patient by ID - {}", id);
        PatientResponseDTO response = patientService.getPatientById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patient-id/{patientId}")
    @Operation(summary = "Get patient by patient ID", description = "Retrieves a patient by their patient ID (e.g., PAT-001)")
    public ResponseEntity<PatientResponseDTO> getPatientByPatientId(@PathVariable String patientId) {
        log.info("REST API: Get patient by patient ID - {}", patientId);
        PatientResponseDTO response = patientService.getPatientByPatientId(patientId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all patients", description = "Retrieves all patients")
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {
        log.info("REST API: Get all patients");
        List<PatientResponseDTO> response = patientService.getAllPatients();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    @Operation(summary = "Get active patients", description = "Retrieves all active patients")
    public ResponseEntity<List<PatientResponseDTO>> getActivePatients() {
        log.info("REST API: Get active patients");
        List<PatientResponseDTO> response = patientService.getActivePatients();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Search patients", description = "Search patients by keyword")
    public ResponseEntity<List<PatientResponseDTO>> searchPatients(@RequestParam String keyword) {
        log.info("REST API: Search patients - {}", keyword);
        List<PatientResponseDTO> response = patientService.searchPatients(keyword);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/blood-group/{bloodGroup}")
    @Operation(summary = "Get patients by blood group", description = "Retrieves patients by blood group")
    public ResponseEntity<List<PatientResponseDTO>> getPatientsByBloodGroup(@PathVariable String bloodGroup) {
        log.info("REST API: Get patients by blood group - {}", bloodGroup);
        List<PatientResponseDTO> response = patientService.getPatientsByBloodGroup(bloodGroup);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/with-allergies")
    @Operation(summary = "Get patients with allergies", description = "Retrieves patients who have allergies")
    public ResponseEntity<List<PatientResponseDTO>> getPatientsWithAllergies() {
        log.info("REST API: Get patients with allergies");
        List<PatientResponseDTO> response = patientService.getPatientsWithAllergies();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/age-range")
    @Operation(summary = "Get patients by age range", description = "Retrieves patients within age range")
    public ResponseEntity<List<PatientResponseDTO>> getPatientsByAgeRange(
            @RequestParam int minAge, @RequestParam int maxAge) {
        log.info("REST API: Get patients by age range - {} to {}", minAge, maxAge);
        List<PatientResponseDTO> response = patientService.getPatientsByAgeRange(minAge, maxAge);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get patient statistics", description = "Retrieves statistics about patients")
    public ResponseEntity<PatientService.PatientStatistics> getPatientStatistics() {
        log.info("REST API: Get patient statistics");
        PatientService.PatientStatistics response = patientService.getPatientStatistics();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update patient", description = "Updates an existing patient")
    public ResponseEntity<PatientResponseDTO> updatePatient(
            @PathVariable Long id, @Valid @RequestBody PatientRequestDTO requestDTO) {
        log.info("REST API: Update patient - {}", id);
        PatientResponseDTO response = patientService.updatePatient(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate patient", description = "Deactivates a patient (soft delete)")
    public ResponseEntity<PatientResponseDTO> deactivatePatient(@PathVariable Long id) {
        log.info("REST API: Deactivate patient - {}", id);
        PatientResponseDTO response = patientService.deactivatePatient(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate patient", description = "Activates a patient")
    public ResponseEntity<PatientResponseDTO> activatePatient(@PathVariable Long id) {
        log.info("REST API: Activate patient - {}", id);
        PatientResponseDTO response = patientService.activatePatient(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/clone")
    @Operation(summary = "Clone patient", description = "Creates a deep copy of patient record")
    public ResponseEntity<PatientResponseDTO> clonePatient(@PathVariable Long id) {
        log.info("REST API: Clone patient - {}", id);
        PatientResponseDTO response = patientService.clonePatient(id);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete patient", description = "Deletes a patient from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Patient deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Patient not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete patient with active appointments")
    })
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        log.info("REST API: Delete patient - {}", id);
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
