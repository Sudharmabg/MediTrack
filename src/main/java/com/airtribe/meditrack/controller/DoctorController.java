package com.airtribe.meditrack.controller;

import com.airtribe.meditrack.dto.DoctorRequestDTO;
import com.airtribe.meditrack.dto.DoctorResponseDTO;
import com.airtribe.meditrack.enums.Specialization;
import com.airtribe.meditrack.service.DoctorService;
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
 * Doctor REST Controller
 * 
 * Demonstrates:
 * - REST API design
 * - HTTP methods (GET, POST, PUT, DELETE)
 * - Request/Response DTOs
 * - Validation
 * - Swagger documentation
 * 
 * @author Sudharma
 */
@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Doctor Management", description = "APIs for managing doctors")
@ApiResponses(value = {
    @ApiResponse(responseCode = "400", description = "Bad request - Invalid input"),
    @ApiResponse(responseCode = "500", description = "Internal server error")
})
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    @Operation(summary = "Register a new doctor", description = "Creates a new doctor in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Doctor created successfully"),
        @ApiResponse(responseCode = "409", description = "Doctor with email already exists")
    })
    public ResponseEntity<DoctorResponseDTO> registerDoctor(@Valid @RequestBody DoctorRequestDTO requestDTO) {
        log.info("REST API: Register doctor - {}", requestDTO.getEmail());
        DoctorResponseDTO response = doctorService.registerDoctor(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get doctor by ID", description = "Retrieves a doctor by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Doctor found successfully"),
        @ApiResponse(responseCode = "404", description = "Doctor not found"),
        @ApiResponse(responseCode = "400", description = "Invalid ID format")
    })
    public ResponseEntity<DoctorResponseDTO> getDoctorById(@PathVariable Long id) {
        log.info("REST API: Get doctor by ID - {}", id);
        DoctorResponseDTO response = doctorService.getDoctorById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all doctors", description = "Retrieves all doctors")
    @ApiResponse(responseCode = "200", description = "Doctors retrieved successfully")
    public ResponseEntity<List<DoctorResponseDTO>> getAllDoctors() {
        log.info("REST API: Get all doctors");
        List<DoctorResponseDTO> response = doctorService.getAllDoctors();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/available")
    @Operation(summary = "Get available doctors", description = "Retrieves all available doctors")
    public ResponseEntity<List<DoctorResponseDTO>> getAvailableDoctors() {
        log.info("REST API: Get available doctors");
        List<DoctorResponseDTO> response = doctorService.getAvailableDoctors();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/specialization/{specialization}")
    @Operation(summary = "Get doctors by specialization", description = "Retrieves doctors by their specialization")
    public ResponseEntity<List<DoctorResponseDTO>> getDoctorsBySpecialization(
            @PathVariable Specialization specialization) {
        log.info("REST API: Get doctors by specialization - {}", specialization);
        List<DoctorResponseDTO> response = doctorService.getDoctorsBySpecialization(specialization);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Search doctors", description = "Search doctors by keyword")
    public ResponseEntity<List<DoctorResponseDTO>> searchDoctors(@RequestParam String keyword) {
        log.info("REST API: Search doctors - {}", keyword);
        List<DoctorResponseDTO> response = doctorService.searchDoctors(keyword);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/experience/{minYears}")
    @Operation(summary = "Get doctors by experience", description = "Retrieves doctors with minimum experience years")
    public ResponseEntity<List<DoctorResponseDTO>> getDoctorsByExperience(@PathVariable int minYears) {
        log.info("REST API: Get doctors by experience - {} years", minYears);
        List<DoctorResponseDTO> response = doctorService.getDoctorsByMinExperience(minYears);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/fee-range")
    @Operation(summary = "Get doctors by fee range", description = "Retrieves doctors within consultation fee range")
    public ResponseEntity<List<DoctorResponseDTO>> getDoctorsByFeeRange(
            @RequestParam Double minFee, @RequestParam Double maxFee) {
        log.info("REST API: Get doctors by fee range - ₹{} to ₹{}", minFee, maxFee);
        List<DoctorResponseDTO> response = doctorService.getDoctorsByFeeRange(minFee, maxFee);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get doctor statistics", description = "Retrieves statistics about doctors")
    public ResponseEntity<DoctorService.DoctorStatistics> getDoctorStatistics() {
        log.info("REST API: Get doctor statistics");
        DoctorService.DoctorStatistics response = doctorService.getDoctorStatistics();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update doctor", description = "Updates an existing doctor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Doctor updated successfully"),
        @ApiResponse(responseCode = "404", description = "Doctor not found")
    })
    public ResponseEntity<DoctorResponseDTO> updateDoctor(
            @PathVariable Long id, @Valid @RequestBody DoctorRequestDTO requestDTO) {
        log.info("REST API: Update doctor - {}", id);
        DoctorResponseDTO response = doctorService.updateDoctor(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/availability")
    @Operation(summary = "Toggle doctor availability", description = "Toggles doctor's availability status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Availability toggled successfully"),
        @ApiResponse(responseCode = "404", description = "Doctor not found")
    })
    public ResponseEntity<DoctorResponseDTO> toggleAvailability(@PathVariable Long id) {
        log.info("REST API: Toggle doctor availability - {}", id);
        DoctorResponseDTO response = doctorService.toggleAvailability(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete doctor", description = "Deletes a doctor from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Doctor deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Doctor not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete doctor with active appointments")
    })
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        log.info("REST API: Delete doctor - {}", id);
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }
}
