package com.personnel.personnelservice.adapters.persistances.repositories;

import com.personnel.personnelservice.adapters.persistances.entities.DoctorPatient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaDoctorPatientRepository extends JpaBaseRepository<DoctorPatient> {

    /**
     * Find all doctor-patient relationships by doctor
     * @param doctorId the doctor id
     * @return list of doctor-patient relationships
     */
    List<DoctorPatient> findByDoctorId(UUID doctorId);

    /**
     * Find all doctor-patient relationships by patient
     * @param patientId the patient id
     * @return list of doctor-patient relationships
     */
    List<DoctorPatient> findByPatientId(UUID patientId);

    /**
     * Find a specific doctor-patient relationship
     * @param doctorId the doctor id
     * @param patientId the patient id
     * @return the doctor-patient relationship if found
     */
    Optional<DoctorPatient> findByDoctorIdAndPatientId(UUID doctorId, UUID patientId);

    /**
     * Find paginated doctor-patient relationships by doctor
     * @param doctorId the doctor id
     * @param pageable pagination information
     * @return paginated list of doctor-patient relationships
     */
    Page<DoctorPatient> findByDoctorId(UUID doctorId, Pageable pageable);

    /**
     * Find paginated doctor-patient relationships by patient
     * @param patientId the patient id
     * @param pageable pagination information
     * @return paginated list of doctor-patient relationships
     */
    Page<DoctorPatient> findByPatientId(UUID patientId, Pageable pageable);

    /**
     * Delete a specific doctor-patient relationship
     * @param doctorId the doctor id
     * @param patientId the patient id
     */
    void deleteByDoctorIdAndPatientId(UUID doctorId, UUID patientId);

    /**
     * Check if a specific doctor-patient relationship exists
     * @param doctorId the doctor id
     * @param patientId the patient id
     * @return true if the relationship exists, false otherwise
     */
    boolean existsByDoctorIdAndPatientId(UUID doctorId, UUID patientId);
}