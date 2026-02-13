package com.airtribe.meditrack.repository;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Appointment Repository
 * 
 * Demonstrates:
 * - Spring Data JPA Repository
 * - Complex query methods
 * - Date/time queries
 * 
 * @author Sudharma
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

        /**
         * Find appointments by doctor
         */
        List<Appointment> findByDoctor(Doctor doctor);

        /**
         * Find appointments by patient
         */
        List<Appointment> findByPatient(Patient patient);

        /**
         * Find appointments by status
         */
        List<Appointment> findByStatus(AppointmentStatus status);

        /**
         * Find appointments by doctor and status
         */
        List<Appointment> findByDoctorAndStatus(Doctor doctor, AppointmentStatus status);

        /**
         * Find appointments by patient and status
         */
        List<Appointment> findByPatientAndStatus(Patient patient, AppointmentStatus status);

        /**
         * Find appointments by date range
         */
        @Query("SELECT a FROM Appointment a WHERE " +
                        "a.appointmentDateTime >= :startDate AND a.appointmentDateTime <= :endDate")
        List<Appointment> findByDateRange(@Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        /**
         * Find upcoming appointments for a doctor
         */
        @Query("SELECT a FROM Appointment a WHERE a.doctor = :doctor " +
                        "AND a.appointmentDateTime > :now " +
                        "AND a.status IN ('SCHEDULED', 'CONFIRMED') " +
                        "ORDER BY a.appointmentDateTime ASC")
        List<Appointment> findUpcomingAppointmentsByDoctor(@Param("doctor") Doctor doctor,
                        @Param("now") LocalDateTime now);

        /**
         * Find upcoming appointments for a patient
         */
        @Query("SELECT a FROM Appointment a WHERE a.patient = :patient " +
                        "AND a.appointmentDateTime > :now " +
                        "AND a.status IN ('SCHEDULED', 'CONFIRMED') " +
                        "ORDER BY a.appointmentDateTime ASC")
        List<Appointment> findUpcomingAppointmentsByPatient(@Param("patient") Patient patient,
                        @Param("now") LocalDateTime now);

        /**
         * Find today's appointments
         */
        @Query("SELECT a FROM Appointment a WHERE " +
                        "CAST(a.appointmentDateTime AS date) = CURRENT_DATE " +
                        "ORDER BY a.appointmentDateTime ASC")
        List<Appointment> findTodaysAppointments();

        /**
         * Count appointments by doctor and status
         */
        long countByDoctorAndStatus(Doctor doctor, AppointmentStatus status);

        /**
         * Count appointments by patient
         */
        long countByPatient(Patient patient);

        /**
         * Check for conflicting appointments
         */
        @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.doctor = :doctor " +
                        "AND a.appointmentDateTime = :dateTime " +
                        "AND a.status IN ('SCHEDULED', 'CONFIRMED', 'IN_PROGRESS')")
        boolean existsConflictingAppointment(@Param("doctor") Doctor doctor,
                        @Param("dateTime") LocalDateTime dateTime);
}
