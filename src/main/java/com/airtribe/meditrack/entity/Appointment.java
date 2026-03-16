package com.airtribe.meditrack.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.airtribe.meditrack.enums.AppointmentStatus;

import java.time.LocalDateTime;

/**
 * Appointment Entity
 * 
 * Demonstrates:
 * - Many-to-One relationships (with Doctor and Patient)
 * - Enum usage (AppointmentStatus)
 * - Business logic encapsulation
 * - Cloneable for deep copy
 * 
 * @author Sudharma
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private LocalDateTime appointmentDateTime;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    @Column(length = 1000)
    private String symptoms;

    @Column(length = 2000)
    private String diagnosis;

    @Column(length = 2000)
    private String prescription;

    @Column(length = 500)
    private String notes;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Business method - confirm appointment
     */
    public void confirm() {
        if (status == AppointmentStatus.SCHEDULED) {
            status = AppointmentStatus.CONFIRMED;
        } else {
            throw new IllegalStateException("Only scheduled appointments can be confirmed");
        }
    }

    /**
     * Business method - cancel appointment
     */
    public void cancel() {
        if (status.isCancellable()) {
            status = AppointmentStatus.CANCELLED;
        } else {
            throw new IllegalStateException("Appointment cannot be cancelled in current status: " + status);
        }
    }

    /**
     * Business method - mark as completed
     */
    public void complete(String diagnosis, String prescription) {
        if (status == AppointmentStatus.IN_PROGRESS || status == AppointmentStatus.CONFIRMED) {
            this.diagnosis = diagnosis;
            this.prescription = prescription;
            this.status = AppointmentStatus.COMPLETED;
        } else {
            throw new IllegalStateException("Only in-progress or confirmed appointments can be completed");
        }
    }

    /**
     * Business method - start appointment
     */
    public void start() {
        if (status == AppointmentStatus.CONFIRMED || status == AppointmentStatus.SCHEDULED) {
            status = AppointmentStatus.IN_PROGRESS;
        } else {
            throw new IllegalStateException("Appointment cannot be started in current status: " + status);
        }
    }

    /**
     * Check if appointment is in the past
     */
    public boolean isPast() {
        return appointmentDateTime.isBefore(LocalDateTime.now());
    }

    /**
     * Check if appointment is upcoming
     */
    public boolean isUpcoming() {
        return appointmentDateTime.isAfter(LocalDateTime.now()) && status.isActive();
    }

    /**
     * Deep clone implementation
     * Demonstrates deep vs shallow copy
     */
    @Override
    public Appointment clone() {
        try {
            Appointment cloned = (Appointment) super.clone();
            // Deep copy of mutable fields
            cloned.appointmentDateTime = LocalDateTime.from(this.appointmentDateTime);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone not supported", e);
        }
    }

    @Override
    public String toString() {
        return String.format("Appointment [ID: %d, Doctor: %s, Patient: %s, DateTime: %s, Status: %s]",
                id,
                doctor != null ? doctor.getFullName() : "N/A",
                patient != null ? patient.getFullName() : "N/A",
                appointmentDateTime,
                status);
    }
}
