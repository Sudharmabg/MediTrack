package com.airtribe.meditrack.dto;

import com.airtribe.meditrack.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * Patient Request DTO
 * 
 * @author Sudharma
 */
@Data
public class PatientRequestDTO {

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

    @Size(max = 20, message = "Blood group must not exceed 20 characters")
    private String bloodGroup;

    @Size(max = 2000, message = "Medical history must not exceed 2000 characters")
    private String medicalHistory;

    @Size(max = 1000, message = "Allergies must not exceed 1000 characters")
    private String allergies;

    @Size(max = 500, message = "Emergency contact must not exceed 500 characters")
    private String emergencyContact;

    private Boolean active = true;
}
