package com.airtribe.meditrack.util;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.exception.InvalidDataException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * Validator Utility Class
 * 
 * Demonstrates:
 * - Centralized validation logic
 * - Regular expressions
 * - Business rule validation
 * 
 * @author Sudharma
 */
@Component
public class Validator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(Constants.EMAIL_REGEX);
    private static final Pattern PHONE_PATTERN = Pattern.compile(Constants.PHONE_REGEX);

    /**
     * Validate email format
     * 
     * @param email Email to validate
     * @throws InvalidDataException if email is invalid
     */
    public void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidDataException("email", "Email cannot be empty");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidDataException("email", "Invalid email format");
        }
    }

    /**
     * Validate phone number (Indian mobile number)
     * 
     * @param phoneNumber Phone number to validate
     * @throws InvalidDataException if phone number is invalid
     */
    public void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new InvalidDataException("phoneNumber", "Phone number cannot be empty");
        }

        if (!PHONE_PATTERN.matcher(phoneNumber).matches()) {
            throw new InvalidDataException("phoneNumber",
                    "Invalid Indian mobile number. Must start with 6-9 and be 10 digits");
        }
    }

    /**
     * Validate name
     * 
     * @param name      Name to validate
     * @param fieldName Field name for error message
     * @throws InvalidDataException if name is invalid
     */
    public void validateName(String name, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidDataException(fieldName, fieldName + " cannot be empty");
        }

        if (name.length() < Constants.MIN_NAME_LENGTH || name.length() > Constants.MAX_NAME_LENGTH) {
            throw new InvalidDataException(fieldName,
                    fieldName + " must be between " + Constants.MIN_NAME_LENGTH +
                            " and " + Constants.MAX_NAME_LENGTH + " characters");
        }
    }

    /**
     * Validate age
     * 
     * @param age Age to validate
     * @throws InvalidDataException if age is invalid
     */
    public void validateAge(int age) {
        if (age < Constants.MIN_AGE || age > Constants.MAX_AGE) {
            throw new InvalidDataException("age",
                    "Age must be between " + Constants.MIN_AGE + " and " + Constants.MAX_AGE);
        }
    }

    /**
     * Validate date of birth
     * 
     * @param dateOfBirth Date of birth to validate
     * @throws InvalidDataException if date of birth is invalid
     */
    public void validateDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            throw new InvalidDataException("dateOfBirth", "Date of birth cannot be null");
        }

        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new InvalidDataException("dateOfBirth", "Date of birth cannot be in the future");
        }

        int age = LocalDate.now().getYear() - dateOfBirth.getYear();
        validateAge(age);
    }

    /**
     * Validate appointment date time
     * 
     * @param appointmentDateTime Appointment date time to validate
     * @throws InvalidDataException if date time is invalid
     */
    public void validateAppointmentDateTime(LocalDateTime appointmentDateTime) {
        if (appointmentDateTime == null) {
            throw new InvalidDataException("appointmentDateTime", "Appointment date time cannot be null");
        }

        if (appointmentDateTime.isBefore(LocalDateTime.now())) {
            throw new InvalidDataException("appointmentDateTime", "Appointment must be in the future");
        }

        // Validate clinic hours
        int hour = appointmentDateTime.getHour();
        if (hour < Constants.CLINIC_OPEN_HOUR || hour >= Constants.CLINIC_CLOSE_HOUR) {
            throw new InvalidDataException("appointmentDateTime",
                    "Appointments must be between " + Constants.CLINIC_OPEN_HOUR +
                            ":00 and " + Constants.CLINIC_CLOSE_HOUR + ":00");
        }
    }

    /**
     * Validate consultation fee
     * 
     * @param fee Consultation fee to validate
     * @throws InvalidDataException if fee is invalid
     */
    public void validateConsultationFee(Double fee) {
        if (fee == null) {
            throw new InvalidDataException("consultationFee", "Consultation fee cannot be null");
        }

        if (fee < Constants.MIN_CONSULTATION_FEE || fee > Constants.MAX_CONSULTATION_FEE) {
            throw new InvalidDataException("consultationFee",
                    "Consultation fee must be between ₹" + Constants.MIN_CONSULTATION_FEE +
                            " and ₹" + Constants.MAX_CONSULTATION_FEE);
        }
    }

    /**
     * Validate appointment duration
     * 
     * @param durationMinutes Duration in minutes
     * @throws InvalidDataException if duration is invalid
     */
    public void validateAppointmentDuration(Integer durationMinutes) {
        if (durationMinutes == null) {
            throw new InvalidDataException("durationMinutes", "Duration cannot be null");
        }

        if (durationMinutes < Constants.MIN_APPOINTMENT_DURATION_MINUTES ||
                durationMinutes > Constants.MAX_APPOINTMENT_DURATION_MINUTES) {
            throw new InvalidDataException("durationMinutes",
                    "Appointment duration must be between " + Constants.MIN_APPOINTMENT_DURATION_MINUTES +
                            " and " + Constants.MAX_APPOINTMENT_DURATION_MINUTES + " minutes");
        }
    }

    /**
     * Validate not null
     * 
     * @param value     Value to validate
     * @param fieldName Field name for error message
     * @throws InvalidDataException if value is null
     */
    public void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new InvalidDataException(fieldName, fieldName + " cannot be null");
        }
    }

    /**
     * Validate not empty string
     * 
     * @param value     String to validate
     * @param fieldName Field name for error message
     * @throws InvalidDataException if string is empty
     */
    public void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidDataException(fieldName, fieldName + " cannot be empty");
        }
    }

    /**
     * Validate positive number
     * 
     * @param value     Number to validate
     * @param fieldName Field name for error message
     * @throws InvalidDataException if number is not positive
     */
    public void validatePositive(Double value, String fieldName) {
        if (value == null || value < 0) {
            throw new InvalidDataException(fieldName, fieldName + " must be positive");
        }
    }
}
