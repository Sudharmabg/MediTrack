package com.airtribe.meditrack.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Doctor Specialization Enum
 * 
 * Demonstrates:
 * - Type-safe enumeration (better than String constants)
 * - Enum with fields and methods
 * - Encapsulation in enums
 * 
 * @author Sudharma
 */
public enum Specialization {
    GENERAL_PHYSICIAN("General Physician", "GP"),
    CARDIOLOGIST("Cardiologist", "CARDIO"),
    DERMATOLOGIST("Dermatologist", "DERM"),
    PEDIATRICIAN("Pediatrician", "PED"),
    ORTHOPEDIC("Orthopedic Surgeon", "ORTHO"),
    NEUROLOGIST("Neurologist", "NEURO"),
    GYNECOLOGIST("Gynecologist", "GYNO"),
    PSYCHIATRIST("Psychiatrist", "PSYCH"),
    OPHTHALMOLOGIST("Ophthalmologist", "OPHTHAL"),
    ENT_SPECIALIST("ENT Specialist", "ENT"),
    DENTIST("Dentist", "DENT"),
    RADIOLOGIST("Radiologist", "RADIO");

    private final String displayName;
    private final String code;

    /**
     * Constructor for Specialization enum
     * 
     * @param displayName Human-readable name
     * @param code        Short code for the specialization
     */
    Specialization(String displayName, String code) {
        this.displayName = displayName;
        this.code = code;
    }

    /**
     * Get display name
     * 
     * @return Display name of the specialization
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get specialization code
     * 
     * @return Short code
     */
    public String getCode() {
        return code;
    }

    /**
     * Get specialization by code
     * 
     * @param code Specialization code
     * @return Specialization enum or null
     */
    public static Specialization fromCode(String code) {
        for (Specialization spec : values()) {
            if (spec.code.equalsIgnoreCase(code)) {
                return spec;
            }
        }
        return null;
    }

    @JsonValue
    @Override
    public String toString() {
        return displayName;
    }

    @JsonCreator
    public static Specialization fromString(String value) {
        if (value == null) {
            return null;
        }
        
        String normalizedValue = value.trim().toUpperCase().replace(" ", "_");
        
        // Try to match by enum name first
        try {
            return Specialization.valueOf(normalizedValue);
        } catch (IllegalArgumentException e) {
            // If that fails, try to match by display name or code
            for (Specialization spec : values()) {
                if (spec.displayName.equalsIgnoreCase(value.trim()) || 
                    spec.code.equalsIgnoreCase(value.trim())) {
                    return spec;
                }
            }
            throw new IllegalArgumentException("Invalid specialization value: " + value);
        }
    }
}
