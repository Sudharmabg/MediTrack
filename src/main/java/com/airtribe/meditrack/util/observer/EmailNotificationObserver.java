package com.airtribe.meditrack.util.observer;

import com.airtribe.meditrack.entity.Appointment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Email Notification Observer - Concrete Observer
 * 
 * Demonstrates:
 * - Concrete implementation of Observer pattern
 * - Email notification simulation
 * 
 * @author Sudharma
 */
@Component
@Slf4j
public class EmailNotificationObserver implements AppointmentObserver {

    @Override
    public void update(Appointment appointment, String action) {
        log.info("📧 EMAIL NOTIFICATION");
        log.info("To: {}", appointment.getPatient().getEmail());
        log.info("Subject: Appointment {} - {}", action, appointment.getId());
        log.info("Dear {}, your appointment with Dr. {} has been {}.",
                appointment.getPatient().getFullName(),
                appointment.getDoctor().getFullName(),
                action.toLowerCase());
        log.info("Appointment Date: {}", appointment.getAppointmentDateTime());
        log.info("Status: {}", appointment.getStatus());
        log.info("---");

        // In real application, this would send actual email
        sendEmail(appointment, action);
    }

    private void sendEmail(Appointment appointment, String action) {
        // Simulate email sending
        log.debug("Simulating email send to {}", appointment.getPatient().getEmail());

        // In production, integrate with email service:
        // - JavaMail API
        // - SendGrid
        // - AWS SES
        // - etc.
    }
}
