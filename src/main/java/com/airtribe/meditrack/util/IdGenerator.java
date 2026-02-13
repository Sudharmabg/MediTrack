package com.airtribe.meditrack.util;

import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ID Generator - Singleton Pattern
 * 
 * Demonstrates:
 * - Singleton pattern (Spring-managed bean)
 * - Thread-safe ID generation
 * - AtomicInteger for concurrency
 * - Static initialization block
 * 
 * Note: In Spring, beans are singletons by default.
 * This class demonstrates both eager and lazy initialization concepts.
 * 
 * @author Sudharma
 */
@Component
public class IdGenerator {

    // Static initialization - happens when class is loaded
    private static final String PATIENT_PREFIX = "PAT-";
    private static final String DOCTOR_PREFIX = "DOC-";
    private static final String APPOINTMENT_PREFIX = "APT-";

    // Thread-safe counters using AtomicInteger
    private final AtomicInteger patientCounter;
    private final AtomicInteger doctorCounter;
    private final AtomicInteger appointmentCounter;

    // Static initialization block
    static {
        System.out.println("IdGenerator class loaded - Static initialization block executed");
    }

    /**
     * Constructor - initializes counters
     * Demonstrates instance initialization
     */
    public IdGenerator() {
        this.patientCounter = new AtomicInteger(1000);
        this.doctorCounter = new AtomicInteger(2000);
        this.appointmentCounter = new AtomicInteger(3000);
        System.out.println("IdGenerator instance created");
    }

    /**
     * Generate unique patient ID
     * Thread-safe using AtomicInteger
     * 
     * @return Patient ID (e.g., PAT-1001)
     */
    public String generatePatientId() {
        int id = patientCounter.incrementAndGet();
        return PATIENT_PREFIX + String.format("%04d", id);
    }

    /**
     * Generate unique doctor ID
     * 
     * @return Doctor ID (e.g., DOC-2001)
     */
    public String generateDoctorId() {
        int id = doctorCounter.incrementAndGet();
        return DOCTOR_PREFIX + String.format("%04d", id);
    }

    /**
     * Generate unique appointment ID
     * 
     * @return Appointment ID (e.g., APT-3001)
     */
    public String generateAppointmentId() {
        int id = appointmentCounter.incrementAndGet();
        return APPOINTMENT_PREFIX + String.format("%04d", id);
    }

    /**
     * Reset counters (for testing purposes)
     */
    public void resetCounters() {
        patientCounter.set(1000);
        doctorCounter.set(2000);
        appointmentCounter.set(3000);
        System.out.println("Counters reset to initial values");
    }

    /**
     * Get current patient counter value
     */
    public int getCurrentPatientCount() {
        return patientCounter.get();
    }

    /**
     * Get current doctor counter value
     */
    public int getCurrentDoctorCount() {
        return doctorCounter.get();
    }

    /**
     * Get current appointment counter value
     */
    public int getCurrentAppointmentCount() {
        return appointmentCounter.get();
    }
}

/**
 * Alternative Singleton Implementation (Not used in Spring context)
 * 
 * This demonstrates classic Singleton patterns without Spring.
 * Kept for educational purposes to show different approaches.
 */
class ClassicSingletonExample {

    // Eager Initialization - instance created at class loading
    private static final ClassicSingletonExample EAGER_INSTANCE = new ClassicSingletonExample();

    // Lazy Initialization - instance created when first accessed
    private static ClassicSingletonExample lazyInstance;

    // Private constructor prevents instantiation
    private ClassicSingletonExample() {
        // Prevent reflection attack
        if (EAGER_INSTANCE != null) {
            throw new IllegalStateException("Singleton instance already created");
        }
    }

    /**
     * Eager Singleton - Thread-safe, instance created at class loading
     */
    public static ClassicSingletonExample getEagerInstance() {
        return EAGER_INSTANCE;
    }

    /**
     * Lazy Singleton - Thread-safe using double-checked locking
     */
    public static ClassicSingletonExample getLazyInstance() {
        if (lazyInstance == null) {
            synchronized (ClassicSingletonExample.class) {
                if (lazyInstance == null) {
                    lazyInstance = new ClassicSingletonExample();
                }
            }
        }
        return lazyInstance;
    }

    /**
     * Bill of Pugh Singleton - Thread-safe, lazy initialization
     * Recommended approach for non-Spring applications
     */
    private static class SingletonHelper {
        private static final ClassicSingletonExample INSTANCE = new ClassicSingletonExample();
    }

    public static ClassicSingletonExample getBillPughInstance() {
        return SingletonHelper.INSTANCE;
    }
}
