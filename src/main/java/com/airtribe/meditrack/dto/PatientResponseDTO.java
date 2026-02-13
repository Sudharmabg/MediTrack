package com.airtribe.meditrack.dto;

import com.airtribe.meditrack.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

/**
 * Patient Response DTO
 * 
 * @author Sudharma
 */
@Data
public class PatientResponseDTO {

    private Long id;
    private String patientId;
    private String firstName;
    private String lastName;
    private String fullName;
    private String phoneNumber;
    private String email;
    private Gender gender;
    private LocalDate dateOfBirth;
    private Integer age;
    private String address;
    private String bloodGroup;
    private String medicalHistory;
    private String allergies;
    private String emergencyContact;
    private Boolean active;
    private Integer totalAppointments;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
