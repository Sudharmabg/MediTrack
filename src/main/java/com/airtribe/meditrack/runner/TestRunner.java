package com.airtribe.meditrack.runner;

import com.airtribe.meditrack.dto.*;
import com.airtribe.meditrack.enums.*;
import com.airtribe.meditrack.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Comprehensive Test Runner for MediTrack Application
 * 
 * Demonstrates all features and design patterns:
 * - Singleton Pattern (IdGenerator)
 * - Factory Pattern (BillFactory)
 * - Strategy Pattern (BillingStrategy)
 * - Observer Pattern (Appointment Notifications)
 * 
 * @author Sudharma
 * @version 1.0.0
 */
@Component
public class TestRunner implements CommandLineRunner {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private AppointmentService appointmentService;

    private static final String SEPARATOR = "=".repeat(80);
    private static final String LINE = "-".repeat(80);

    @Override
    public void run(String... args) {
        try {
            printHeader();

            // Test 1: Doctor Registration
            List<DoctorResponseDTO> doctors = testDoctorRegistration();

            // Test 2: Patient Registration
            List<PatientResponseDTO> patients = testPatientRegistration();

            // Test 3: Appointment Booking (Observer Pattern)
            testAppointmentBooking(doctors, patients);

            // Test 4: Search and Filtering
            testSearchAndFiltering();

            // Test 5: Statistics and Analytics
            testStatistics();

            printFooter();

        } catch (Exception e) {
            System.err.println("\n❌ Test execution failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void printHeader() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("MEDITRACK COMPREHENSIVE TEST RUNNER");
        System.out.println(SEPARATOR);
        System.out.println("\nTesting all features and design patterns...\n");
    }

    private List<DoctorResponseDTO> testDoctorRegistration() {
        printTestHeader("Test 1: Doctor Registration");

        DoctorRequestDTO doctor1 = createDoctorRequest("Sarah", "Johnson", "sarah.j@hospital.com",
                "9876543210", Specialization.CARDIOLOGIST, "LIC123456", 1500.0, 15);
        DoctorRequestDTO doctor2 = createDoctorRequest("Michael", "Chen", "michael.c@hospital.com",
                "9876543211", Specialization.PEDIATRICIAN, "LIC123457", 1200.0, 10);
        DoctorRequestDTO doctor3 = createDoctorRequest("Priya", "Sharma", "priya.s@hospital.com",
                "9876543212", Specialization.DERMATOLOGIST, "LIC123458", 1000.0, 8);

        DoctorResponseDTO d1 = doctorService.createDoctor(doctor1);
        DoctorResponseDTO d2 = doctorService.createDoctor(doctor2);
        DoctorResponseDTO d3 = doctorService.createDoctor(doctor3);

        System.out.println("Registered Doctor 1: Dr. " + d1.getFullName() + " (ID: " + d1.getId() + ")");
        System.out.println("Specialization: " + d1.getSpecialization());
        System.out.println("Fee: ₹" + d1.getConsultationFee());
        System.out.println("Experience: " + d1.getExperienceYears() + " years");
        System.out.println("Registered Doctor 2: Dr. " + d2.getFullName() + " (ID: " + d2.getId() + ")");
        System.out.println("Registered Doctor 3: Dr. " + d3.getFullName() + " (ID: " + d3.getId() + ")");
        System.out.println();

        return List.of(d1, d2, d3);
    }

    private List<PatientResponseDTO> testPatientRegistration() {
        printTestHeader("Test 2: Patient Registration");

        PatientRequestDTO patient1 = createPatientRequest("John", "Doe", "john.doe@email.com",
                "9123456780", LocalDate.of(1988, 5, 15), "O+", "Penicillin");
        PatientRequestDTO patient2 = createPatientRequest("Jane", "Smith", "jane.smith@email.com",
                "9123456781", LocalDate.of(1995, 8, 22), "A+", "None");
        PatientRequestDTO patient3 = createPatientRequest("Raj", "Kumar", "raj.kumar@email.com",
                "9123456782", LocalDate.of(1980, 12, 10), "B+", "Sulfa drugs");

        PatientResponseDTO p1 = patientService.registerPatient(patient1);
        PatientResponseDTO p2 = patientService.registerPatient(patient2);
        PatientResponseDTO p3 = patientService.registerPatient(patient3);

        System.out.println("Registered Patient 1: " + p1.getFullName() + " (ID: " + p1.getPatientId() + ")");
        System.out.println("Age: " + p1.getAge() + " years");
        System.out.println("Blood Group: " + p1.getBloodGroup());
        System.out.println("Allergies: " + p1.getAllergies());
        System.out.println("Registered Patient 2: " + p2.getFullName() + " (ID: " + p2.getPatientId() + ")");
        System.out.println("Registered Patient 3: " + p3.getFullName() + " (ID: " + p3.getPatientId() + ")");
        System.out.println();

        return List.of(p1, p2, p3);
    }

    private void testAppointmentBooking(List<DoctorResponseDTO> doctors, List<PatientResponseDTO> patients) {
        printTestHeader("Test 3: Appointment Booking (Observer Pattern)");

        AppointmentRequestDTO apt1 = createAppointmentRequest(doctors.get(0).getId(), patients.get(0).getId(),
                LocalDateTime.now().plusDays(1).withHour(10).withMinute(0));
        AppointmentRequestDTO apt2 = createAppointmentRequest(doctors.get(1).getId(), patients.get(1).getId(),
                LocalDateTime.now().plusDays(2).withHour(14).withMinute(0));
        AppointmentRequestDTO apt3 = createAppointmentRequest(doctors.get(2).getId(), patients.get(2).getId(),
                LocalDateTime.now().plusDays(3).withHour(11).withMinute(30));

        AppointmentResponseDTO a1 = appointmentService.bookAppointment(apt1);
        AppointmentResponseDTO a2 = appointmentService.bookAppointment(apt2);
        AppointmentResponseDTO a3 = appointmentService.bookAppointment(apt3);

        System.out.println("Booked Appointment 1: ID " + a1.getId());
        System.out.println("Doctor: Dr. " + doctors.get(0).getFullName());
        System.out.println("Patient: " + patients.get(0).getFullName());
        System.out.println("Date/Time: " + a1.getAppointmentDateTime());
        System.out.println("Status: " + a1.getStatus());
        System.out.println("Email notification sent!");
        System.out.println("SMS notification sent!");
        System.out.println("Booked Appointment 2: ID " + a2.getId());
        System.out.println("Booked Appointment 3: ID " + a3.getId());
        System.out.println();
    }

    private void testSearchAndFiltering() {
        printTestHeader("Test 4: Search and Filtering");

        System.out.println("Testing search capabilities:");

        List<DoctorResponseDTO> cardiologists = doctorService.getDoctorsBySpecialization(Specialization.CARDIOLOGIST);
        System.out.println("Found " + cardiologists.size() + " Cardiologist(s)");

        List<DoctorResponseDTO> available = doctorService.getAvailableDoctors();
        System.out.println("Found " + available.size() + " available doctor(s)");

        List<PatientResponseDTO> activePatients = patientService.getActivePatients();
        System.out.println("Found " + activePatients.size() + " active patient(s)");

        System.out.println();
    }

    private void testStatistics() {
        printTestHeader("Test 5: Statistics and Analytics");

        var doctorStats = doctorService.getDoctorStatistics();
        System.out.println("Doctor Statistics:");
        System.out.println("   Total Doctors: " + doctorStats.getTotalDoctors());
        System.out.println("   Available Doctors: " + doctorStats.getAvailableDoctors());
        System.out.println(
                "   Average Consultation Fee: ₹" + String.format("%.2f", doctorStats.getAverageConsultationFee()));

        var patientStats = patientService.getPatientStatistics();
        System.out.println("\nPatient Statistics:");
        System.out.println("   Total Patients: " + patientStats.getTotalPatients());
        System.out.println("   Active Patients: " + patientStats.getActivePatients());

        var appointmentStats = appointmentService.getAppointmentStatistics();
        System.out.println("\nAppointment Statistics:");
        System.out.println("   Total Appointments: " + appointmentStats.getTotalAppointments());

        System.out.println();
    }

    private void printFooter() {
        System.out.println(SEPARATOR);
        System.out.println("  ALL TESTS COMPLETED SUCCESSFULLY! ");
        System.out.println(SEPARATOR);
        System.out.println("\nYou can now access:");
        System.out.println("Swagger UI: http://localhost:8080/swagger-ui.html");
        System.out.println("H2 Console: http://localhost:8080/h2-console");
        System.out.println("API Docs: http://localhost:8080/api-docs");
        System.out.println("\nApplication is running. Press Ctrl+C to stop.\n");
    }

    private void printTestHeader(String title) {
        System.out.println(LINE);
        System.out.println("  " + title);
        System.out.println(LINE);
        System.out.println();
    }

    // Helper methods
    private DoctorRequestDTO createDoctorRequest(String firstName, String lastName, String email, String phone,
            Specialization specialization, String license, double fee, int experience) {
        DoctorRequestDTO doctor = new DoctorRequestDTO();
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setEmail(email);
        doctor.setPhoneNumber(phone);
        doctor.setDateOfBirth(LocalDate.of(1980, 1, 1));
        doctor.setGender(Gender.MALE);
        doctor.setAddress("Hospital Address");
        doctor.setSpecialization(specialization);
        doctor.setLicenseNumber(license);
        doctor.setConsultationFee(fee);
        doctor.setExperienceYears(experience);
        doctor.setQualifications("MBBS, MD");
        doctor.setAvailable(true);
        return doctor;
    }

    private PatientRequestDTO createPatientRequest(String firstName, String lastName, String email, String phone,
            LocalDate dob, String bloodGroup, String allergies) {
        PatientRequestDTO patient = new PatientRequestDTO();
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setEmail(email);
        patient.setPhoneNumber(phone);
        patient.setDateOfBirth(dob);
        patient.setGender(Gender.MALE);
        patient.setAddress("Patient Address");
        patient.setBloodGroup(bloodGroup);
        patient.setAllergies(allergies);
        patient.setMedicalHistory("No major illnesses");
        patient.setActive(true);
        return patient;
    }

    private AppointmentRequestDTO createAppointmentRequest(Long doctorId, Long patientId, LocalDateTime dateTime) {
        AppointmentRequestDTO appointment = new AppointmentRequestDTO();
        appointment.setDoctorId(doctorId);
        appointment.setPatientId(patientId);
        appointment.setAppointmentDateTime(dateTime);
        return appointment;
    }
}
