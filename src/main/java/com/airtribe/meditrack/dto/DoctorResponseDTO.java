package com.airtribe.meditrack.dto;

import com.airtribe.meditrack.enums.Gender;
import com.airtribe.meditrack.enums.Specialization;
import lombok.Data;

import java.time.LocalDate;

/**
 * Doctor Response DTO
 * 
 * @author Sudharma
 */
@Data
public class DoctorResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String phoneNumber;
    private String email;
    private Gender gender;
    private LocalDate dateOfBirth;
    private Integer age;
    private String address;
    private String licenseNumber;
    private Specialization specialization;
    private String specializationDisplayName;
    private Double consultationFee;
    private Integer experienceYears;
    private String qualifications;
    private Boolean available;
    private Long activeAppointmentsCount;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
