package com.personnel.personnelservice.adapters.persistances.repositories;

import com.personnel.personnelservice.adapters.persistances.entities.DoctorAssistant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaDoctorAssistantRepository extends JpaBaseRepository<DoctorAssistant> {

    /**
     * Find all doctor-assistant relationships by doctor
     * @param doctorId the doctor id
     * @return list of doctor-assistant relationships
     */
    List<DoctorAssistant> findByDoctorId(UUID doctorId);

    /**
     * Find all doctor-assistant relationships by assistant
     * @param assistantId the assistant id
     * @return list of doctor-assistant relationships
     */
    List<DoctorAssistant> findByAssistantId(UUID assistantId);

    /**
     * Find a specific doctor-assistant relationship
     * @param doctorId the doctor id
     * @param assistantId the assistant id
     * @return the doctor-assistant relationship if found
     */
    Optional<DoctorAssistant> findByDoctorIdAndAssistantId(UUID doctorId, UUID assistantId);

    /**
     * Find paginated doctor-assistant relationships by doctor
     * @param doctorId the doctor id
     * @param pageable pagination information
     * @return paginated list of doctor-assistant relationships
     */
    Page<DoctorAssistant> findByDoctorId(UUID doctorId, Pageable pageable);

    /**
     * Find paginated doctor-assistant relationships by assistant
     * @param assistantId the assistant id
     * @param pageable pagination information
     * @return paginated list of doctor-assistant relationships
     */
    Page<DoctorAssistant> findByAssistantId(UUID assistantId, Pageable pageable);

    /**
     * Delete a specific doctor-assistant relationship
     * @param doctorId the doctor id
     * @param assistantId the assistant id
     */
    void deleteByDoctorIdAndAssistantId(UUID doctorId, UUID assistantId);

    /**
     * Check if a specific doctor-assistant relationship exists
     * @param doctorId the doctor id
     * @param assistantId the assistant id
     * @return true if the relationship exists, false otherwise
     */
    boolean existsByDoctorIdAndAssistantId(UUID doctorId, UUID assistantId);
}