package com.airtribe.meditrack.util;

import com.airtribe.meditrack.constants.Constants;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Date Utility Class
 * 
 * Demonstrates:
 * - Date/time utility methods
 * - Date formatting
 * - Date calculations
 * 
 * @author Sudharma
 */
@Component
public class DateUtil {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT);

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(Constants.TIME_FORMAT);

    /**
     * Format date to string
     * 
     * @param date Date to format
     * @return Formatted date string
     */
    public String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : null;
    }

    /**
     * Format date time to string
     * 
     * @param dateTime Date time to format
     * @return Formatted date time string
     */
    public String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATETIME_FORMATTER) : null;
    }

    /**
     * Format time to string
     * 
     * @param dateTime Date time to extract time from
     * @return Formatted time string
     */
    public String formatTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(TIME_FORMATTER) : null;
    }

    /**
     * Parse date from string
     * 
     * @param dateString Date string
     * @return Parsed LocalDate
     */
    public LocalDate parseDate(String dateString) {
        return dateString != null ? LocalDate.parse(dateString, DATE_FORMATTER) : null;
    }

    /**
     * Parse date time from string
     * 
     * @param dateTimeString Date time string
     * @return Parsed LocalDateTime
     */
    public LocalDateTime parseDateTime(String dateTimeString) {
        return dateTimeString != null ? LocalDateTime.parse(dateTimeString, DATETIME_FORMATTER) : null;
    }

    /**
     * Calculate age from date of birth
     * 
     * @param dateOfBirth Date of birth
     * @return Age in years
     */
    public int calculateAge(LocalDate dateOfBirth) {
        return dateOfBirth != null ? (int) ChronoUnit.YEARS.between(dateOfBirth, LocalDate.now()) : 0;
    }

    /**
     * Check if date is in the past
     * 
     * @param date Date to check
     * @return true if date is in the past
     */
    public boolean isPast(LocalDate date) {
        return date != null && date.isBefore(LocalDate.now());
    }

    /**
     * Check if date time is in the past
     * 
     * @param dateTime Date time to check
     * @return true if date time is in the past
     */
    public boolean isPast(LocalDateTime dateTime) {
        return dateTime != null && dateTime.isBefore(LocalDateTime.now());
    }

    /**
     * Check if date is in the future
     * 
     * @param date Date to check
     * @return true if date is in the future
     */
    public boolean isFuture(LocalDate date) {
        return date != null && date.isAfter(LocalDate.now());
    }

    /**
     * Check if date time is in the future
     * 
     * @param dateTime Date time to check
     * @return true if date time is in the future
     */
    public boolean isFuture(LocalDateTime dateTime) {
        return dateTime != null && dateTime.isAfter(LocalDateTime.now());
    }

    /**
     * Check if date is today
     * 
     * @param date Date to check
     * @return true if date is today
     */
    public boolean isToday(LocalDate date) {
        return date != null && date.equals(LocalDate.now());
    }

    /**
     * Check if date time is today
     * 
     * @param dateTime Date time to check
     * @return true if date time is today
     */
    public boolean isToday(LocalDateTime dateTime) {
        return dateTime != null && dateTime.toLocalDate().equals(LocalDate.now());
    }

    /**
     * Add days to date
     * 
     * @param date Base date
     * @param days Number of days to add
     * @return New date
     */
    public LocalDate addDays(LocalDate date, long days) {
        return date != null ? date.plusDays(days) : null;
    }

    /**
     * Add hours to date time
     * 
     * @param dateTime Base date time
     * @param hours    Number of hours to add
     * @return New date time
     */
    public LocalDateTime addHours(LocalDateTime dateTime, long hours) {
        return dateTime != null ? dateTime.plusHours(hours) : null;
    }

    /**
     * Get days between two dates
     * 
     * @param startDate Start date
     * @param endDate   End date
     * @return Number of days between dates
     */
    public long daysBetween(LocalDate startDate, LocalDate endDate) {
        return startDate != null && endDate != null ? ChronoUnit.DAYS.between(startDate, endDate) : 0;
    }

    /**
     * Get hours between two date times
     * 
     * @param startDateTime Start date time
     * @param endDateTime   End date time
     * @return Number of hours between date times
     */
    public long hoursBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return startDateTime != null && endDateTime != null ? ChronoUnit.HOURS.between(startDateTime, endDateTime) : 0;
    }

    /**
     * Get current date time
     * 
     * @return Current LocalDateTime
     */
    public LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * Get current date
     * 
     * @return Current LocalDate
     */
    public LocalDate today() {
        return LocalDate.now();
    }
}
