package com.airtribe.meditrack.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gender Enum
 * 
 * @author Sudharma
 */
public enum Gender {
    MALE("Male", "M"),
    FEMALE("Female", "F"),
    OTHER("Other", "O");

    private final String displayName;
    private final String code;

    Gender(String displayName, String code) {
        this.displayName = displayName;
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCode() {
        return code;
    }

    @JsonValue
    @Override
    public String toString() {
        return displayName;
    }

    @JsonCreator
    public static Gender fromString(String value) {
        if (value == null) {
            return null;
        }
        
        String normalizedValue = value.trim().toUpperCase();
        
        // Handle both enum name and display name
        switch (normalizedValue) {
            case "MALE":
            case "M":
                return MALE;
            case "FEMALE":
            case "F":
                return FEMALE;
            case "OTHER":
            case "O":
                return OTHER;
            default:
                throw new IllegalArgumentException("Invalid gender value: " + value + 
                    ". Valid values are: MALE, FEMALE, OTHER (case insensitive)");
        }
    }
}
