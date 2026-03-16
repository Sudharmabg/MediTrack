package com.airtribe.meditrack.repository;

import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.enums.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Doctor Repository
 * 
 * Demonstrates:
 * - Spring Data JPA Repository
 * - Custom query methods
 * - JPQL queries
 * 
 * @author Sudharma
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

        /**
         * Find doctor by license number
         */
        Optional<Doctor> findByLicenseNumber(String licenseNumber);

        /**
         * Find doctors by specialization
         */
        List<Doctor> findBySpecialization(Specialization specialization);

        /**
         * Find available doctors
         */
        List<Doctor> findByAvailableTrue();

        /**
         * Find available doctors by specialization
         */
        List<Doctor> findBySpecializationAndAvailableTrue(Specialization specialization);

        /**
         * Find doctors by experience years (greater than or equal)
         */
        List<Doctor> findByExperienceYearsGreaterThanEqual(Integer years);

        /**
         * Find doctors by consultation fee range
         */
        List<Doctor> findByConsultationFeeBetween(Double minFee, Double maxFee);

        /**
         * Search doctors by name (case-insensitive)
         */
        @Query("SELECT d FROM Doctor d WHERE " +
                        "LOWER(d.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(d.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
        List<Doctor> searchByName(@Param("keyword") String keyword);

        /**
         * Find top doctors by experience
         */
        @Query("SELECT d FROM Doctor d WHERE d.available = true " +
                        "ORDER BY d.experienceYears DESC")
        List<Doctor> findTopDoctorsByExperience();

        /**
         * Count doctors by specialization
         */
        long countBySpecialization(Specialization specialization);

        /**
         * Check if doctor exists by email
         */
        boolean existsByEmail(String email);

        /**
         * Check if doctor exists by license number
         */
        boolean existsByLicenseNumber(String licenseNumber);

        /**
         * Count available doctors
         */
        long countByAvailableTrue();
}
