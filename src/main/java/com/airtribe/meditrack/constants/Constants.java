package com.airtribe.meditrack.constants;

/**
 * Application-wide constants
 * 
 * Demonstrates:
 * - Static final fields for immutable constants
 * - Centralized configuration
 * - Best practices for constant naming (UPPER_SNAKE_CASE)
 * 
 * @author Sudharma
 */
public final class Constants {

    // Prevent instantiation
    private Constants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }

    // Tax and Currency
    public static final double DEFAULT_TAX_RATE = 0.18; // 18% GST
    public static final String DEFAULT_CURRENCY = "INR";

    // Appointment Configuration
    public static final int DEFAULT_APPOINTMENT_DURATION_MINUTES = 30;
    public static final int MIN_APPOINTMENT_DURATION_MINUTES = 15;
    public static final int MAX_APPOINTMENT_DURATION_MINUTES = 120;

    // Working Hours
    public static final int CLINIC_OPEN_HOUR = 9; // 9 AM
    public static final int CLINIC_CLOSE_HOUR = 18; // 6 PM

    // Validation Constraints
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 100;
    public static final int MIN_AGE = 0;
    public static final int MAX_AGE = 150;
    public static final String PHONE_REGEX = "^[6-9]\\d{9}$"; // Indian mobile number
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    // File Paths
    public static final String DATA_EXPORT_PATH = "data/exports/";
    public static final String DATA_IMPORT_PATH = "data/imports/";
    public static final String REPORTS_PATH = "data/reports/";

    // Date Formats
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_FORMAT = "HH:mm";

    // API Response Messages
    public static final String SUCCESS_MESSAGE = "Operation completed successfully";
    public static final String CREATED_MESSAGE = "Resource created successfully";
    public static final String UPDATED_MESSAGE = "Resource updated successfully";
    public static final String DELETED_MESSAGE = "Resource deleted successfully";
    public static final String NOT_FOUND_MESSAGE = "Resource not found";
    public static final String VALIDATION_ERROR_MESSAGE = "Validation failed";
    public static final String INTERNAL_ERROR_MESSAGE = "Internal server error occurred";

    // Pagination
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 100;

    // Billing
    public static final double MIN_CONSULTATION_FEE = 100.0;
    public static final double MAX_CONSULTATION_FEE = 10000.0;
}
