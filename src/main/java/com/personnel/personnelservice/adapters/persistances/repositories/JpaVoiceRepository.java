package com.personnel.personnelservice.adapters.persistances.repositories;

import com.personnel.personnelservice.adapters.persistances.entities.Voice;
import com.personnel.personnelservice.adapters.persistances.entities.Patient;
import com.personnel.personnelservice.adapters.persistances.entities.Medecin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface JpaVoiceRepository extends JpaBaseRepository<Voice> {

    /**
     * Find all voices by doctor
     * @param doctorId the doctor id
     * @return list of voices
     */
    List<Voice> findByDoctorId(UUID doctorId);

    /**
     * Find all voices by patient
     * @param patientId the patient id
     * @return list of voices
     */
    List<Voice> findByPatientId(UUID patientId);

    /**
     * Find all voices by doctor and patient
     * @param doctorId the doctor id
     * @param patientId the patient id
     * @return list of voices
     */
    List<Voice> findByDoctorIdAndPatientId(UUID doctorId, UUID patientId);

    /**
     * Find paginated voices by doctor
     * @param doctor the doctor
     * @param pageable pagination information
     * @return paginated list of voices
     */
    Page<Voice> findByDoctor(Medecin doctor, Pageable pageable);

    /**
     * Find paginated voices by patient
     * @param patient the patient
     * @param pageable pagination information
     * @return paginated list of voices
     */
    Page<Voice> findByPatient(Patient patient, Pageable pageable);

    /**
     * Find paginated voices by doctor and patient
     * @param doctor the doctor
     * @param patient the patient
     * @param pageable pagination information
     * @return paginated list of voices
     */
    Page<Voice> findByDoctorAndPatient(Medecin doctor, Patient patient, Pageable pageable);

    /**
     * Search voices by title (containing the search text)
     * @param searchText the search text
     * @param pageable pagination information
     * @return paginated list of voices
     */
    Page<Voice> findByTitleContainingIgnoreCase(String searchText, Pageable pageable);
}