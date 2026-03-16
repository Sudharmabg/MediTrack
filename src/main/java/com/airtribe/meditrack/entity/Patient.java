package com.airtribe.meditrack.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.airtribe.meditrack.interfaces.Searchable;

import java.util.ArrayList;
import java.util.List;

/**
 * Patient Entity - extends Person
 * 
 * Demonstrates:
 * - Inheritance (extends Person)
 * - Polymorphism (overrides abstract methods)
 * - Interface implementation (Searchable)
 * - Deep cloning (Cloneable)
 * - One-to-Many relationship with Appointments
 * 
 * @author Sudharma
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patients")
public class Patient extends Person implements Searchable {

    @Column(nullable = false, unique = true, length = 20)
    private String patientId;

    @Column(length = 20)
    private String bloodGroup;

    @Column(length = 2000)
    private String medicalHistory;

    @Column(length = 1000)
    private String allergies;

    @Column(length = 500)
    private String emergencyContact;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments = new ArrayList<>();

    /**
     * Override abstract method from Person
     * Demonstrates polymorphism
     */
    @Override
    public String getPersonType() {
        return "Patient";
    }

    /**
     * Implement Searchable interface
     * Demonstrates interface implementation and method overloading
     */
    @Override
    public boolean matchesSearchCriteria(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return false;
        }

        String lowerKeyword = keyword.toLowerCase();
        return getFullName().toLowerCase().contains(lowerKeyword) ||
                patientId.toLowerCase().contains(lowerKeyword) ||
                (bloodGroup != null && bloodGroup.toLowerCase().contains(lowerKeyword)) ||
                getPhoneNumber().contains(keyword);
    }

    /**
     * Method overloading - search by age
     * Demonstrates polymorphism (compile-time)
     */
    public boolean matchesAge(int age) {
        return getAge() == age;
    }

    /**
     * Method overloading - search by age range
     * Demonstrates polymorphism (compile-time)
     */
    public boolean matchesAgeRange(int minAge, int maxAge) {
        int age = getAge();
        return age >= minAge && age <= maxAge;
    }

    /**
     * Deep clone implementation
     * Demonstrates deep vs shallow copy
     */
    @Override
    public Patient clone() {
        Patient cloned = (Patient) super.clone();
        // Deep copy of mutable collections
        if (this.appointments != null) {
            cloned.appointments = new ArrayList<>(this.appointments);
        }
        return cloned;
    }

    /**
     * Business method - check if patient has any allergies
     */
    public boolean hasAllergies() {
        return allergies != null && !allergies.trim().isEmpty();
    }

    /**
     * Get total appointments count
     */
    public int getTotalAppointments() {
        return appointments != null ? appointments.size() : 0;
    }

    @Override
    public String toString() {
        return String.format("Patient %s [ID: %s, Blood Group: %s, Age: %d]",
                getFullName(), patientId, bloodGroup, getAge());
    }
}
