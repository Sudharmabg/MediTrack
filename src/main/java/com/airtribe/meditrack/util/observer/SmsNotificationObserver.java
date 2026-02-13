package com.airtribe.meditrack.util.observer;

import com.airtribe.meditrack.entity.Appointment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * SMS Notification Observer - Concrete Observer
 * 
 * Demonstrates:
 * - Another concrete implementation of Observer pattern
 * - SMS notification simulation
 * 
 * @author Sudharma
 */
@Component
@Slf4j
public class SmsNotificationObserver implements AppointmentObserver {

    @Override
    public void update(Appointment appointment, String action) {
        log.info("📱 SMS NOTIFICATION");
        log.info("To: {}", appointment.getPatient().getPhoneNumber());
        log.info("Message: Your appointment with Dr. {} has been {}. Date: {}. Status: {}",
                appointment.getDoctor().getFullName(),
                action.toLowerCase(),
                appointment.getAppointmentDateTime(),
                appointment.getStatus());
        log.info("---");

        // In real application, this would send actual SMS
        sendSms(appointment, action);
    }

    private void sendSms(Appointment appointment, String action) {
        // Simulate SMS sending
        log.debug("Simulating SMS send to {}", appointment.getPatient().getPhoneNumber());

        // In production, integrate with SMS service:
        // - Twilio
        // - AWS SNS
        // - MSG91
        // - etc.
    }
}
