package com.airtribe.meditrack.repository;

import com.airtribe.meditrack.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Patient Repository
 * 
 * Demonstrates:
 * - Spring Data JPA Repository
 * - Custom finder methods
 * - Query methods
 * 
 * @author Sudharma
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * Find patient by patient ID
     */
    Optional<Patient> findByPatientId(String patientId);

    /**
     * Find active patients
     */
    List<Patient> findByActiveTrue();

    /**
     * Find patients by blood group
     */
    List<Patient> findByBloodGroup(String bloodGroup);

    /**
     * Search patients by name (case-insensitive)
     */
    @Query("SELECT p FROM Patient p WHERE " +
            "LOWER(p.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Patient> searchByName(@Param("keyword") String keyword);

    /**
     * Search patients by phone number
     */
    List<Patient> findByPhoneNumberContaining(String phoneNumber);

    /**
     * Find patients with allergies
     */
    @Query("SELECT p FROM Patient p WHERE p.allergies IS NOT NULL AND p.allergies != ''")
    List<Patient> findPatientsWithAllergies();

    /**
     * Count active patients
     */
    long countByActiveTrue();

    /**
     * Check if patient exists by email
     */
    boolean existsByEmail(String email);

    /**
     * Check if patient exists by patient ID
     */
    boolean existsByPatientId(String patientId);

    /**
     * Check if patient exists by phone number
     */
    boolean existsByPhoneNumber(String phoneNumber);
}
