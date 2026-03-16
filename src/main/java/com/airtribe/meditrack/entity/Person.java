package com.airtribe.meditrack.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.airtribe.meditrack.enums.Gender;
import java.time.LocalDate;

/**
 * Abstract Base Class - Person
 * 
 * Demonstrates:
 * - Abstraction (abstract class)
 * - Inheritance (base class for Doctor and Patient)
 * - Encapsulation (private fields with getters/setters via Lombok)
 * - JPA Inheritance Strategy (JOINED table strategy)
 * 
 * @author Sudharma
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "persons")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 10)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(length = 500)
    private String address;

    @Column(updatable = false)
    private LocalDate createdAt;

    @Column
    private LocalDate updatedAt;

    /**
     * Pre-persist callback - set creation timestamp
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
        updatedAt = LocalDate.now();
    }

    /**
     * Pre-update callback - update timestamp
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }

    /**
     * Get full name
     * 
     * @return Full name (firstName + lastName)
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Calculate age from date of birth
     * 
     * @return Age in years
     */
    public int getAge() {
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }

    /**
     * Abstract method to be implemented by subclasses
     * Demonstrates polymorphism
     * 
     * @return Person type description
     */
    public abstract String getPersonType();

    /**
     * Deep copy implementation
     * Demonstrates Cloneable interface
     * 
     * @return Cloned Person object
     */
    @Override
    public Person clone() {
        try {
            return (Person) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone not supported", e);
        }
    }

    @Override
    public String toString() {
        return String.format("%s [ID: %d, Name: %s, Phone: %s]",
                getPersonType(), id, getFullName(), phoneNumber);
    }
}
