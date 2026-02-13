package com.airtribe.meditrack.service;

import com.airtribe.meditrack.dto.DoctorRequestDTO;
import com.airtribe.meditrack.dto.DoctorResponseDTO;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.enums.Specialization;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.exception.ResourceNotFoundException;
import com.airtribe.meditrack.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Doctor Service
 * 
 * Demonstrates:
 * - Service layer pattern
 * - Business logic encapsulation
 * - Transaction management
 * - DTO mapping
 * - Java 8+ Streams and Lambdas
 * 
 * @author Sudharma
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;

    /**
     * Register a new doctor
     * 
     * @param requestDTO Doctor request DTO
     * @return Created doctor response DTO
     */
    public DoctorResponseDTO registerDoctor(DoctorRequestDTO requestDTO) {
        log.info("Registering new doctor: {} {}", requestDTO.getFirstName(), requestDTO.getLastName());

        // Validate unique constraints
        if (doctorRepository.existsByEmail(requestDTO.getEmail())) {
            throw new InvalidDataException("email", "Email already exists");
        }
        if (doctorRepository.existsByLicenseNumber(requestDTO.getLicenseNumber())) {
            throw new InvalidDataException("licenseNumber", "License number already exists");
        }

        // Map DTO to Entity
        Doctor doctor = modelMapper.map(requestDTO, Doctor.class);

        // Save doctor
        Doctor savedDoctor = doctorRepository.save(doctor);
        log.info("Doctor registered successfully with ID: {}", savedDoctor.getId());

        return mapToResponseDTO(savedDoctor);
    }

    /**
     * Create a new doctor (alias for registerDoctor)
     * 
     * @param requestDTO Doctor request DTO
     * @return Created doctor response DTO
     */
    public DoctorResponseDTO createDoctor(DoctorRequestDTO requestDTO) {
        return registerDoctor(requestDTO);
    }

    /**
     * Get doctor by ID
     * 
     * @param id Doctor ID
     * @return Doctor response DTO
     */
    @Transactional(readOnly = true)
    public DoctorResponseDTO getDoctorById(Long id) {
        log.info("Fetching doctor with ID: {}", id);
        Doctor doctor = findDoctorById(id);
        return mapToResponseDTO(doctor);
    }

    /**
     * Get all doctors
     * 
     * Demonstrates: Java 8 Streams
     * 
     * @return List of doctor response DTOs
     */
    @Transactional(readOnly = true)
    public List<DoctorResponseDTO> getAllDoctors() {
        log.info("Fetching all doctors");
        return doctorRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get available doctors
     * 
     * Demonstrates: Stream filtering
     * 
     * @return List of available doctors
     */
    @Transactional(readOnly = true)
    public List<DoctorResponseDTO> getAvailableDoctors() {
        log.info("Fetching available doctors");
        return doctorRepository.findByAvailableTrue()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get doctors by specialization
     * 
     * Demonstrates: Method overloading (polymorphism)
     * 
     * @param specialization Specialization
     * @return List of doctors
     */
    @Transactional(readOnly = true)
    public List<DoctorResponseDTO> getDoctorsBySpecialization(Specialization specialization) {
        log.info("Fetching doctors with specialization: {}", specialization);
        return doctorRepository.findBySpecialization(specialization)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get available doctors by specialization
     * 
     * Demonstrates: Method overloading (polymorphism)
     * 
     * @param specialization Specialization
     * @return List of available doctors
     */
    @Transactional(readOnly = true)
    public List<DoctorResponseDTO> getAvailableDoctorsBySpecialization(Specialization specialization) {
        log.info("Fetching available doctors with specialization: {}", specialization);
        return doctorRepository.findBySpecializationAndAvailableTrue(specialization)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Search doctors by keyword
     * 
     * @param keyword Search keyword
     * @return List of matching doctors
     */
    @Transactional(readOnly = true)
    public List<DoctorResponseDTO> searchDoctors(String keyword) {
        log.info("Searching doctors with keyword: {}", keyword);
        return doctorRepository.searchByName(keyword)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get doctors by experience (greater than or equal)
     * 
     * Demonstrates: Stream filtering and sorting
     * 
     * @param years Minimum experience years
     * @return List of doctors sorted by experience
     */
    @Transactional(readOnly = true)
    public List<DoctorResponseDTO> getDoctorsByExperience(Integer years) {
        log.info("Fetching doctors with experience >= {} years", years);
        return doctorRepository.findByExperienceYearsGreaterThanEqual(years)
                .stream()
                .sorted((d1, d2) -> d2.getExperienceYears().compareTo(d1.getExperienceYears()))
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get doctors by fee range
     * 
     * Demonstrates: Stream operations
     * 
     * @param minFee Minimum fee
     * @param maxFee Maximum fee
     * @return List of doctors
     */
    @Transactional(readOnly = true)
    public List<DoctorResponseDTO> getDoctorsByFeeRange(Double minFee, Double maxFee) {
        log.info("Fetching doctors with fee between {} and {}", minFee, maxFee);
        return doctorRepository.findByConsultationFeeBetween(minFee, maxFee)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Calculate average consultation fee
     * 
     * Demonstrates: Stream aggregate operations
     * 
     * @return Average fee
     */
    @Transactional(readOnly = true)
    public Double calculateAverageConsultationFee() {
        log.info("Calculating average consultation fee");
        return doctorRepository.findAll()
                .stream()
                .mapToDouble(Doctor::getConsultationFee)
                .average()
                .orElse(0.0);
    }

    /**
     * Update doctor
     * 
     * @param id         Doctor ID
     * @param requestDTO Updated doctor data
     * @return Updated doctor response DTO
     */
    public DoctorResponseDTO updateDoctor(Long id, DoctorRequestDTO requestDTO) {
        log.info("Updating doctor with ID: {}", id);
        Doctor doctor = findDoctorById(id);

        // Update fields
        doctor.setFirstName(requestDTO.getFirstName());
        doctor.setLastName(requestDTO.getLastName());
        doctor.setPhoneNumber(requestDTO.getPhoneNumber());
        doctor.setEmail(requestDTO.getEmail());
        doctor.setGender(requestDTO.getGender());
        doctor.setDateOfBirth(requestDTO.getDateOfBirth());
        doctor.setAddress(requestDTO.getAddress());
        doctor.setSpecialization(requestDTO.getSpecialization());
        doctor.setConsultationFee(requestDTO.getConsultationFee());
        doctor.setExperienceYears(requestDTO.getExperienceYears());
        doctor.setQualifications(requestDTO.getQualifications());
        doctor.setAvailable(requestDTO.getAvailable());

        Doctor updatedDoctor = doctorRepository.save(doctor);
        log.info("Doctor updated successfully");

        return mapToResponseDTO(updatedDoctor);
    }

    /**
     * Delete doctor
     * 
     * @param id Doctor ID
     */
    public void deleteDoctor(Long id) {
        log.info("Deleting doctor with ID: {}", id);
        Doctor doctor = findDoctorById(id);
        doctorRepository.delete(doctor);
        log.info("Doctor deleted successfully");
    }

    /**
     * Toggle doctor availability
     * 
     * @param id Doctor ID
     * @return Updated doctor
     */
    public DoctorResponseDTO toggleAvailability(Long id) {
        log.info("Toggling availability for doctor ID: {}", id);
        Doctor doctor = findDoctorById(id);
        doctor.setAvailable(!doctor.getAvailable());
        Doctor updatedDoctor = doctorRepository.save(doctor);
        return mapToResponseDTO(updatedDoctor);
    }

    /**
     * Get doctors by minimum experience
     * 
     * @param minYears Minimum experience years
     * @return List of doctors
     */
    @Transactional(readOnly = true)
    public List<DoctorResponseDTO> getDoctorsByMinExperience(int minYears) {
        log.info("Fetching doctors with minimum experience: {} years", minYears);
        return getDoctorsByExperience(minYears);
    }

    /**
     * Get doctor statistics
     * 
     * Demonstrates: Stream aggregate operations
     * 
     * @return Statistics
     */
    @Transactional(readOnly = true)
    public DoctorStatistics getDoctorStatistics() {
        log.info("Calculating doctor statistics");

        List<Doctor> allDoctors = doctorRepository.findAll();

        long totalDoctors = allDoctors.size();
        long availableDoctors = doctorRepository.countByAvailableTrue();

        double averageFee = calculateAverageConsultationFee();

        double averageExperience = allDoctors.stream()
                .mapToInt(Doctor::getExperienceYears)
                .average()
                .orElse(0.0);

        return new DoctorStatistics(totalDoctors, availableDoctors, averageFee, averageExperience);
    }

    // Helper methods

    private Doctor findDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", id));
    }

    private DoctorResponseDTO mapToResponseDTO(Doctor doctor) {
        DoctorResponseDTO dto = modelMapper.map(doctor, DoctorResponseDTO.class);
        dto.setFullName(doctor.getFullName());
        dto.setAge(doctor.getAge());
        dto.setSpecializationDisplayName(doctor.getSpecialization().getDisplayName());
        dto.setActiveAppointmentsCount(doctor.getActiveAppointmentsCount());
        return dto;
    }

    /**
     * Inner class for doctor statistics
     */
    public static class DoctorStatistics {
        private final long totalDoctors;
        private final long availableDoctors;
        private final double averageConsultationFee;
        private final double averageExperience;

        public DoctorStatistics(long totalDoctors, long availableDoctors,
                double averageConsultationFee, double averageExperience) {
            this.totalDoctors = totalDoctors;
            this.availableDoctors = availableDoctors;
            this.averageConsultationFee = averageConsultationFee;
            this.averageExperience = averageExperience;
        }

        // Getters
        public long getTotalDoctors() {
            return totalDoctors;
        }

        public long getAvailableDoctors() {
            return availableDoctors;
        }

        public double getAverageConsultationFee() {
            return averageConsultationFee;
        }

        public double getAverageExperience() {
            return averageExperience;
        }
    }
}
