package com.airtribe.meditrack.dto;

import com.airtribe.meditrack.enums.Gender;
import com.airtribe.meditrack.enums.Specialization;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * Doctor Request DTO
 * 
 * Demonstrates:
 * - DTO pattern for API requests
 * - Bean Validation annotations
 * - Separation of concerns (API layer vs Domain layer)
 * 
 * @author Sudharma
 */
@Data
public class DoctorRequestDTO {

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
    private String lastName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid Indian mobile number")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    @NotBlank(message = "License number is required")
    @Size(max = 20, message = "License number must not exceed 20 characters")
    private String licenseNumber;

    @NotNull(message = "Specialization is required")
    private Specialization specialization;

    @NotNull(message = "Consultation fee is required")
    @DecimalMin(value = "100.0", message = "Consultation fee must be at least ₹100")
    @DecimalMax(value = "10000.0", message = "Consultation fee must not exceed ₹10,000")
    private Double consultationFee;

    @NotNull(message = "Experience years is required")
    @Min(value = 0, message = "Experience years cannot be negative")
    @Max(value = 60, message = "Experience years must not exceed 60")
    private Integer experienceYears;

    @Size(max = 1000, message = "Qualifications must not exceed 1000 characters")
    private String qualifications;

    private Boolean available = true;
}
