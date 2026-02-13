package com.airtribe.meditrack.util;

import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.enums.Gender;
import com.airtribe.meditrack.enums.Specialization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV Utility Class
 * 
 * Demonstrates:
 * - File I/O operations
 * - CSV parsing and generation
 * - Try-with-resources
 * - Exception handling
 * - Serialization/Deserialization
 * 
 * @author Sudharma
 */
@Component
@Slf4j
public class CSVUtil {

    private static final String CSV_SEPARATOR = ",";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Export doctors to CSV file
     * 
     * Demonstrates: File writing, try-with-resources
     * 
     * @param doctors  List of doctors
     * @param filePath File path
     * @throws IOException if file operation fails
     */
    public void exportDoctorsToCSV(List<Doctor> doctors, String filePath) throws IOException {
        log.info("Exporting {} doctors to CSV: {}", doctors.size(), filePath);

        // Try-with-resources ensures file is closed automatically
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            // Write header
            writer.write(
                    "ID,FirstName,LastName,Email,PhoneNumber,Gender,DateOfBirth,LicenseNumber,Specialization,ConsultationFee,ExperienceYears,Available");
            writer.newLine();

            // Write data
            for (Doctor doctor : doctors) {
                writer.write(doctorToCSV(doctor));
                writer.newLine();
            }

            log.info("Successfully exported doctors to CSV");
        } catch (IOException e) {
            log.error("Error exporting doctors to CSV: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Import doctors from CSV file
     * 
     * Demonstrates: File reading, CSV parsing, exception handling
     * 
     * @param filePath File path
     * @return List of doctors
     * @throws IOException if file operation fails
     */
    public List<Doctor> importDoctorsFromCSV(String filePath) throws IOException {
        log.info("Importing doctors from CSV: {}", filePath);
        List<Doctor> doctors = new ArrayList<>();

        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            // Skip header
            String header = reader.readLine();
            if (header == null) {
                throw new IOException("CSV file is empty");
            }

            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    Doctor doctor = csvToDoctor(line);
                    doctors.add(doctor);
                } catch (Exception e) {
                    log.warn("Error parsing line {}: {} - {}", lineNumber, line, e.getMessage());
                    // Continue processing other lines
                }
            }

            log.info("Successfully imported {} doctors from CSV", doctors.size());
        } catch (IOException e) {
            log.error("Error importing doctors from CSV: {}", e.getMessage());
            throw e;
        }

        return doctors;
    }

    /**
     * Export patients to CSV file
     * 
     * @param patients List of patients
     * @param filePath File path
     * @throws IOException if file operation fails
     */
    public void exportPatientsToCSV(List<Patient> patients, String filePath) throws IOException {
        log.info("Exporting {} patients to CSV: {}", patients.size(), filePath);

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            // Write header
            writer.write(
                    "ID,PatientID,FirstName,LastName,Email,PhoneNumber,Gender,DateOfBirth,Address,BloodGroup,MedicalHistory,Allergies,EmergencyContact,Active");
            writer.newLine();

            // Write data
            for (Patient patient : patients) {
                writer.write(patientToCSV(patient));
                writer.newLine();
            }

            log.info("Successfully exported patients to CSV");
        } catch (IOException e) {
            log.error("Error exporting patients to CSV: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Import patients from CSV file
     * 
     * @param filePath File path
     * @return List of patients
     * @throws IOException if file operation fails
     */
    public List<Patient> importPatientsFromCSV(String filePath) throws IOException {
        log.info("Importing patients from CSV: {}", filePath);
        List<Patient> patients = new ArrayList<>();

        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            // Skip header
            String header = reader.readLine();
            if (header == null) {
                throw new IOException("CSV file is empty");
            }

            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    Patient patient = csvToPatient(line);
                    patients.add(patient);
                } catch (Exception e) {
                    log.warn("Error parsing line {}: {} - {}", lineNumber, line, e.getMessage());
                }
            }

            log.info("Successfully imported {} patients from CSV", patients.size());
        } catch (IOException e) {
            log.error("Error importing patients from CSV: {}", e.getMessage());
            throw e;
        }

        return patients;
    }

    // Helper methods for CSV conversion

    private String doctorToCSV(Doctor doctor) {
        return String.join(CSV_SEPARATOR,
                String.valueOf(doctor.getId() != null ? doctor.getId() : ""),
                escapeCSV(doctor.getFirstName()),
                escapeCSV(doctor.getLastName()),
                escapeCSV(doctor.getEmail()),
                escapeCSV(doctor.getPhoneNumber()),
                doctor.getGender() != null ? doctor.getGender().name() : "",
                doctor.getDateOfBirth() != null ? doctor.getDateOfBirth().format(DATE_FORMATTER) : "",
                escapeCSV(doctor.getLicenseNumber()),
                doctor.getSpecialization() != null ? doctor.getSpecialization().name() : "",
                String.valueOf(doctor.getConsultationFee()),
                String.valueOf(doctor.getExperienceYears()),
                String.valueOf(doctor.getAvailable()));
    }

    private Doctor csvToDoctor(String csvLine) {
        String[] fields = parseCSVLine(csvLine);

        Doctor doctor = new Doctor();
        int i = 0;

        // Skip ID (will be auto-generated)
        i++;

        doctor.setFirstName(fields[i++]);
        doctor.setLastName(fields[i++]);
        doctor.setEmail(fields[i++]);
        doctor.setPhoneNumber(fields[i++]);
        doctor.setGender(Gender.valueOf(fields[i++]));
        doctor.setDateOfBirth(LocalDate.parse(fields[i++], DATE_FORMATTER));
        doctor.setLicenseNumber(fields[i++]);
        doctor.setSpecialization(Specialization.valueOf(fields[i++]));
        doctor.setConsultationFee(Double.parseDouble(fields[i++]));
        doctor.setExperienceYears(Integer.parseInt(fields[i++]));
        doctor.setAvailable(Boolean.parseBoolean(fields[i++]));

        return doctor;
    }

    private String patientToCSV(Patient patient) {
        return String.join(CSV_SEPARATOR,
                String.valueOf(patient.getId() != null ? patient.getId() : ""),
                escapeCSV(patient.getPatientId()),
                escapeCSV(patient.getFirstName()),
                escapeCSV(patient.getLastName()),
                escapeCSV(patient.getEmail()),
                escapeCSV(patient.getPhoneNumber()),
                patient.getGender() != null ? patient.getGender().name() : "",
                patient.getDateOfBirth() != null ? patient.getDateOfBirth().format(DATE_FORMATTER) : "",
                escapeCSV(patient.getAddress()),
                escapeCSV(patient.getBloodGroup()),
                escapeCSV(patient.getMedicalHistory()),
                escapeCSV(patient.getAllergies()),
                escapeCSV(patient.getEmergencyContact()),
                String.valueOf(patient.getActive()));
    }

    private Patient csvToPatient(String csvLine) {
        String[] fields = parseCSVLine(csvLine);

        Patient patient = new Patient();
        int i = 0;

        // Skip ID (will be auto-generated)
        i++;

        patient.setPatientId(fields[i++]);
        patient.setFirstName(fields[i++]);
        patient.setLastName(fields[i++]);
        patient.setEmail(fields[i++]);
        patient.setPhoneNumber(fields[i++]);
        patient.setGender(Gender.valueOf(fields[i++]));
        patient.setDateOfBirth(LocalDate.parse(fields[i++], DATE_FORMATTER));
        patient.setAddress(fields[i++]);
        patient.setBloodGroup(fields[i++]);
        patient.setMedicalHistory(fields[i++]);
        patient.setAllergies(fields[i++]);
        patient.setEmergencyContact(fields[i++]);
        patient.setActive(Boolean.parseBoolean(fields[i++]));

        return patient;
    }

    /**
     * Escape CSV field (handle commas and quotes)
     * 
     * @param field Field to escape
     * @return Escaped field
     */
    private String escapeCSV(String field) {
        if (field == null) {
            return "";
        }

        // If field contains comma or quote, wrap in quotes and escape quotes
        if (field.contains(",") || field.contains("\"")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }

        return field;
    }

    /**
     * Parse CSV line (handle quoted fields)
     * 
     * @param line CSV line
     * @return Array of fields
     */
    private String[] parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    // Escaped quote
                    currentField.append('"');
                    i++; // Skip next quote
                } else {
                    // Toggle quote mode
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                // Field separator
                fields.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }

        // Add last field
        fields.add(currentField.toString());

        return fields.toArray(new String[0]);
    }

    /**
     * Check if file exists
     * 
     * @param filePath File path
     * @return true if file exists
     */
    public boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    /**
     * Create directory if not exists
     * 
     * @param dirPath Directory path
     * @throws IOException if directory creation fails
     */
    public void createDirectoryIfNotExists(String dirPath) throws IOException {
        Path path = Paths.get(dirPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
            log.info("Created directory: {}", dirPath);
        }
    }
}
