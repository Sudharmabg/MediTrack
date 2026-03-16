package com.airtribe.meditrack.service;

import com.airtribe.meditrack.dto.PatientRequestDTO;
import com.airtribe.meditrack.dto.PatientResponseDTO;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.exception.ResourceNotFoundException;
import com.airtribe.meditrack.repository.PatientRepository;
import com.airtribe.meditrack.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Patient Service
 * 
 * Demonstrates:
 * - Service layer pattern
 * - Business logic encapsulation
 * - Transaction management
 * - Java 8+ Streams and Lambdas
 * - Singleton pattern (IdGenerator)
 * 
 * @author Sudharma
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;
    private final IdGenerator idGenerator;

    /**
     * Register a new patient
     * 
     * @param requestDTO Patient request DTO
     * @return Created patient response DTO
     */
    public PatientResponseDTO registerPatient(PatientRequestDTO requestDTO) {
        log.info("Registering new patient: {} {}", requestDTO.getFirstName(), requestDTO.getLastName());

        // Validate unique constraints
        if (patientRepository.existsByEmail(requestDTO.getEmail())) {
            throw new InvalidDataException("email", "Email already exists");
        }
        if (patientRepository.existsByPhoneNumber(requestDTO.getPhoneNumber())) {
            throw new InvalidDataException("phoneNumber", "Phone number already exists");
        }

        // Map DTO to Entity
        Patient patient = modelMapper.map(requestDTO, Patient.class);

        // Generate unique patient ID using Singleton pattern
        patient.setPatientId(idGenerator.generatePatientId());

        // Save patient
        Patient savedPatient = patientRepository.save(patient);
        log.info("Patient registered successfully with ID: {} (Patient ID: {})",
                savedPatient.getId(), savedPatient.getPatientId());

        return mapToResponseDTO(savedPatient);
    }

    /**
     * Get patient by ID
     * 
     * @param id Patient ID
     * @return Patient response DTO
     */
    @Transactional(readOnly = true)
    public PatientResponseDTO getPatientById(Long id) {
        log.info("Fetching patient with ID: {}", id);
        Patient patient = findPatientById(id);
        return mapToResponseDTO(patient);
    }

    /**
     * Get patient by patient ID
     * 
     * @param patientId Patient ID (e.g., PAT-001)
     * @return Patient response DTO
     */
    @Transactional(readOnly = true)
    public PatientResponseDTO getPatientByPatientId(String patientId) {
        log.info("Fetching patient with Patient ID: {}", patientId);
        Patient patient = patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", patientId));
        return mapToResponseDTO(patient);
    }

    /**
     * Get all patients
     * 
     * Demonstrates: Java 8 Streams
     * 
     * @return List of patient response DTOs
     */
    @Transactional(readOnly = true)
    public List<PatientResponseDTO> getAllPatients() {
        log.info("Fetching all patients");
        return patientRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get active patients
     * 
     * Demonstrates: Stream filtering
     * 
     * @return List of active patients
     */
    @Transactional(readOnly = true)
    public List<PatientResponseDTO> getActivePatients() {
        log.info("Fetching active patients");
        return patientRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Search patients by keyword
     * 
     * @param keyword Search keyword
     * @return List of matching patients
     */
    @Transactional(readOnly = true)
    public List<PatientResponseDTO> searchPatients(String keyword) {
        log.info("Searching patients with keyword: {}", keyword);
        return patientRepository.searchByName(keyword)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get patients by blood group
     * 
     * @param bloodGroup Blood group
     * @return List of patients
     */
    @Transactional(readOnly = true)
    public List<PatientResponseDTO> getPatientsByBloodGroup(String bloodGroup) {
        log.info("Fetching patients with blood group: {}", bloodGroup);
        return patientRepository.findByBloodGroup(bloodGroup)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get patients with allergies
     * 
     * Demonstrates: Stream filtering with predicates
     * 
     * @return List of patients with allergies
     */
    @Transactional(readOnly = true)
    public List<PatientResponseDTO> getPatientsWithAllergies() {
        log.info("Fetching patients with allergies");
        return patientRepository.findPatientsWithAllergies()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get patients by age range
     * 
     * Demonstrates: Stream filtering with custom predicates
     * 
     * @param minAge Minimum age
     * @param maxAge Maximum age
     * @return List of patients in age range
     */
    @Transactional(readOnly = true)
    public List<PatientResponseDTO> getPatientsByAgeRange(int minAge, int maxAge) {
        log.info("Fetching patients with age between {} and {}", minAge, maxAge);
        return patientRepository.findAll()
                .stream()
                .filter(patient -> patient.matchesAgeRange(minAge, maxAge))
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update patient
     * 
     * @param id         Patient ID
     * @param requestDTO Updated patient data
     * @return Updated patient response DTO
     */
    public PatientResponseDTO updatePatient(Long id, PatientRequestDTO requestDTO) {
        log.info("Updating patient with ID: {}", id);
        Patient patient = findPatientById(id);

        // Update fields
        patient.setFirstName(requestDTO.getFirstName());
        patient.setLastName(requestDTO.getLastName());
        patient.setPhoneNumber(requestDTO.getPhoneNumber());
        patient.setEmail(requestDTO.getEmail());
        patient.setGender(requestDTO.getGender());
        patient.setDateOfBirth(requestDTO.getDateOfBirth());
        patient.setAddress(requestDTO.getAddress());
        patient.setBloodGroup(requestDTO.getBloodGroup());
        patient.setMedicalHistory(requestDTO.getMedicalHistory());
        patient.setAllergies(requestDTO.getAllergies());
        patient.setEmergencyContact(requestDTO.getEmergencyContact());
        patient.setActive(requestDTO.getActive());

        Patient updatedPatient = patientRepository.save(patient);
        log.info("Patient updated successfully");

        return mapToResponseDTO(updatedPatient);
    }

    /**
     * Delete patient
     * 
     * @param id Patient ID
     */
    public void deletePatient(Long id) {
        log.info("Deleting patient with ID: {}", id);
        Patient patient = findPatientById(id);
        patientRepository.delete(patient);
        log.info("Patient deleted successfully");
    }

    /**
     * Deactivate patient (soft delete)
     * 
     * @param id Patient ID
     * @return Updated patient
     */
    public PatientResponseDTO deactivatePatient(Long id) {
        log.info("Deactivating patient with ID: {}", id);
        Patient patient = findPatientById(id);
        patient.setActive(false);
        Patient updatedPatient = patientRepository.save(patient);
        return mapToResponseDTO(updatedPatient);
    }

    /**
     * Activate patient
     * 
     * @param id Patient ID
     * @return Updated patient
     */
    public PatientResponseDTO activatePatient(Long id) {
        log.info("Activating patient with ID: {}", id);
        Patient patient = findPatientById(id);
        patient.setActive(true);
        Patient updatedPatient = patientRepository.save(patient);
        return mapToResponseDTO(updatedPatient);
    }

    /**
     * Clone patient record (demonstrates deep copy)
     * 
     * @param id Patient ID to clone
     * @return Cloned patient
     */
    public PatientResponseDTO clonePatient(Long id) {
        log.info("Cloning patient with ID: {}", id);
        Patient original = findPatientById(id);

        // Deep clone
        Patient cloned = original.clone();
        cloned.setId(null); // New ID will be generated
        cloned.setPatientId(idGenerator.generatePatientId()); // New patient ID
        cloned.setEmail(original.getEmail() + ".copy"); // Unique email

        Patient savedClone = patientRepository.save(cloned);
        log.info("Patient cloned successfully with new ID: {}", savedClone.getId());

        return mapToResponseDTO(savedClone);
    }

    /**
     * Get patient statistics
     * 
     * Demonstrates: Stream aggregate operations
     * 
     * @return Statistics map
     */
    @Transactional(readOnly = true)
    public PatientStatistics getPatientStatistics() {
        log.info("Calculating patient statistics");

        List<Patient> allPatients = patientRepository.findAll();

        long totalPatients = allPatients.size();
        long activePatients = patientRepository.countByActiveTrue();
        long patientsWithAllergies = allPatients.stream()
                .filter(Patient::hasAllergies)
                .count();

        double averageAge = allPatients.stream()
                .mapToInt(Patient::getAge)
                .average()
                .orElse(0.0);

        return new PatientStatistics(totalPatients, activePatients,
                patientsWithAllergies, averageAge);
    }

    // Helper methods

    private Patient findPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", id));
    }

    private PatientResponseDTO mapToResponseDTO(Patient patient) {
        PatientResponseDTO dto = modelMapper.map(patient, PatientResponseDTO.class);
        dto.setFullName(patient.getFullName());
        dto.setAge(patient.getAge());
        dto.setTotalAppointments(patient.getTotalAppointments());
        return dto;
    }

    /**
     * Inner class for patient statistics
     */
    public static class PatientStatistics {
        private final long totalPatients;
        private final long activePatients;
        private final long patientsWithAllergies;
        private final double averageAge;

        public PatientStatistics(long totalPatients, long activePatients,
                long patientsWithAllergies, double averageAge) {
            this.totalPatients = totalPatients;
            this.activePatients = activePatients;
            this.patientsWithAllergies = patientsWithAllergies;
            this.averageAge = averageAge;
        }

        // Getters
        public long getTotalPatients() {
            return totalPatients;
        }

        public long getActivePatients() {
            return activePatients;
        }

        public long getPatientsWithAllergies() {
            return patientsWithAllergies;
        }

        public double getAverageAge() {
            return averageAge;
        }
    }
}
