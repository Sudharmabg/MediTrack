package com.airtribe.meditrack.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.airtribe.meditrack.enums.Specialization;
import com.airtribe.meditrack.interfaces.Searchable;

import java.util.ArrayList;
import java.util.List;

/**
 * Doctor Entity - extends Person
 * 
 * Demonstrates:
 * - Inheritance (extends Person)
 * - Polymorphism (overrides abstract methods)
 * - Interface implementation (Searchable)
 * - One-to-Many relationship with Appointments
 * 
 * @author Sudharma
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctors")
public class Doctor extends Person implements Searchable {

    @Column(nullable = false, unique = true, length = 20)
    private String licenseNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Specialization specialization;

    @Column(nullable = false)
    private Double consultationFee;

    @Column(nullable = false)
    private Integer experienceYears;

    @Column(length = 1000)
    private String qualifications;

    @Column(nullable = false)
    private Boolean available = true;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments = new ArrayList<>();

    /**
     * Constructor with essential fields (demonstrates constructor chaining)
     */
    public Doctor(String licenseNumber, Specialization specialization,
            Double consultationFee, Integer experienceYears) {
        super();
        this.licenseNumber = licenseNumber;
        this.specialization = specialization;
        this.consultationFee = consultationFee;
        this.experienceYears = experienceYears;
    }

    /**
     * Override abstract method from Person
     * Demonstrates polymorphism
     */
    @Override
    public String getPersonType() {
        return "Doctor";
    }

    /**
     * Implement Searchable interface
     * Demonstrates interface implementation
     */
    @Override
    public boolean matchesSearchCriteria(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return false;
        }

        String lowerKeyword = keyword.toLowerCase();
        return getFullName().toLowerCase().contains(lowerKeyword) ||
                licenseNumber.toLowerCase().contains(lowerKeyword) ||
                specialization.getDisplayName().toLowerCase().contains(lowerKeyword) ||
                (qualifications != null && qualifications.toLowerCase().contains(lowerKeyword));
    }

    /**
     * Business method - check if doctor can accept new appointments
     */
    public boolean canAcceptAppointments() {
        return available && appointments.stream()
                .filter(apt -> apt.getStatus().isActive())
                .count() < 10; // Max 10 active appointments
    }

    /**
     * Get active appointments count
     */
    public long getActiveAppointmentsCount() {
        return appointments.stream()
                .filter(apt -> apt.getStatus().isActive())
                .count();
    }

    @Override
    public String toString() {
        return String.format("Dr. %s [%s, License: %s, Fee: ₹%.2f]",
                getFullName(), specialization.getDisplayName(),
                licenseNumber, consultationFee);
    }
}
