package com.personnel.personnelservice.core.ports.services;

import com.personnel.personnelservice.core.models.dtos.DoctorPatientDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for managing doctor-patient relationships
 */
public interface DoctorPatientService {

    /**
     * Create a new doctor-patient relationship
     * @param doctorPatientDto doctor-patient relationship data
     * @return the created doctor-patient relationship
     */
    DoctorPatientDto createDoctorPatient(DoctorPatientDto doctorPatientDto);

    /**
     * Get a doctor-patient relationship by id
     * @param id doctor-patient relationship id
     * @return the doctor-patient relationship
     */
    DoctorPatientDto getDoctorPatientById(UUID id);

    /**
     * Update a doctor-patient relationship
     * @param doctorPatientDto doctor-patient relationship data
     * @return the updated doctor-patient relationship
     */
    DoctorPatientDto updateDoctorPatient(DoctorPatientDto doctorPatientDto);

    /**
     * Delete a doctor-patient relationship
     * @param id doctor-patient relationship id
     */
    void deleteDoctorPatient(UUID id);

    /**
     * Get all doctor-patient relationships
     * @return list of all doctor-patient relationships
     */
    List<DoctorPatientDto> getAllDoctorPatients();

    /**
     * Get paginated doctor-patient relationships
     * @param pageable pagination information
     * @return paginated list of doctor-patient relationships
     */
    Page<DoctorPatientDto> getAllDoctorPatients(Pageable pageable);

    /**
     * Get all doctor-patient relationships by doctor id
     * @param doctorId doctor id
     * @return list of doctor-patient relationships
     */
    List<DoctorPatientDto> getDoctorPatientsByDoctorId(UUID doctorId);

    /**
     * Get paginated doctor-patient relationships by doctor id
     * @param doctorId doctor id
     * @param pageable pagination information
     * @return paginated list of doctor-patient relationships
     */
    Page<DoctorPatientDto> getDoctorPatientsByDoctorId(UUID doctorId, Pageable pageable);

    /**
     * Get all doctor-patient relationships by patient id
     * @param patientId patient id
     * @return list of doctor-patient relationships
     */
    List<DoctorPatientDto> getDoctorPatientsByPatientId(UUID patientId);

    /**
     * Get paginated doctor-patient relationships by patient id
     * @param patientId patient id
     * @param pageable pagination information
     * @return paginated list of doctor-patient relationships
     */
    Page<DoctorPatientDto> getDoctorPatientsByPatientId(UUID patientId, Pageable pageable);

    /**
     * Get a specific doctor-patient relationship by doctor id and patient id
     * @param doctorId doctor id
     * @param patientId patient id
     * @return the doctor-patient relationship if found
     */
    Optional<DoctorPatientDto> getDoctorPatientByDoctorIdAndPatientId(UUID doctorId, UUID patientId);

    /**
     * Delete a doctor-patient relationship by doctor id and patient id
     * @param doctorId doctor id
     * @param patientId patient id
     */
    void deleteDoctorPatientByDoctorIdAndPatientId(UUID doctorId, UUID patientId);

    /**
     * Check if a doctor-patient relationship exists
     * @param doctorId doctor id
     * @param patientId patient id
     * @return true if the relationship exists, false otherwise
     */
    boolean existsDoctorPatientByDoctorIdAndPatientId(UUID doctorId, UUID patientId);
}