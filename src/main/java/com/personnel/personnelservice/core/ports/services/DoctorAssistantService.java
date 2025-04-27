package com.personnel.personnelservice.core.ports.services;

import com.personnel.personnelservice.core.models.dtos.DoctorAssistantDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for managing doctor-assistant relationships
 */
public interface DoctorAssistantService {

    /**
     * Create a new doctor-assistant relationship
     * @param doctorAssistantDto doctor-assistant relationship data
     * @return the created doctor-assistant relationship
     */
    DoctorAssistantDto createDoctorAssistant(DoctorAssistantDto doctorAssistantDto);

    /**
     * Get a doctor-assistant relationship by id
     * @param id doctor-assistant relationship id
     * @return the doctor-assistant relationship
     */
    DoctorAssistantDto getDoctorAssistantById(UUID id);

    /**
     * Update a doctor-assistant relationship
     * @param doctorAssistantDto doctor-assistant relationship data
     * @return the updated doctor-assistant relationship
     */
    DoctorAssistantDto updateDoctorAssistant(DoctorAssistantDto doctorAssistantDto);

    /**
     * Delete a doctor-assistant relationship
     * @param id doctor-assistant relationship id
     */
    void deleteDoctorAssistant(UUID id);

    /**
     * Get all doctor-assistant relationships
     * @return list of all doctor-assistant relationships
     */
    List<DoctorAssistantDto> getAllDoctorAssistants();

    /**
     * Get paginated doctor-assistant relationships
     * @param pageable pagination information
     * @return paginated list of doctor-assistant relationships
     */
    Page<DoctorAssistantDto> getAllDoctorAssistants(Pageable pageable);

    /**
     * Get all doctor-assistant relationships by doctor id
     * @param doctorId doctor id
     * @return list of doctor-assistant relationships
     */
    List<DoctorAssistantDto> getDoctorAssistantsByDoctorId(UUID doctorId);

    /**
     * Get paginated doctor-assistant relationships by doctor id
     * @param doctorId doctor id
     * @param pageable pagination information
     * @return paginated list of doctor-assistant relationships
     */
    Page<DoctorAssistantDto> getDoctorAssistantsByDoctorId(UUID doctorId, Pageable pageable);

    /**
     * Get all doctor-assistant relationships by assistant id
     * @param assistantId assistant id
     * @return list of doctor-assistant relationships
     */
    List<DoctorAssistantDto> getDoctorAssistantsByAssistantId(UUID assistantId);

    /**
     * Get paginated doctor-assistant relationships by assistant id
     * @param assistantId assistant id
     * @param pageable pagination information
     * @return paginated list of doctor-assistant relationships
     */
    Page<DoctorAssistantDto> getDoctorAssistantsByAssistantId(UUID assistantId, Pageable pageable);

    /**
     * Get a specific doctor-assistant relationship by doctor id and assistant id
     * @param doctorId doctor id
     * @param assistantId assistant id
     * @return the doctor-assistant relationship if found
     */
    Optional<DoctorAssistantDto> getDoctorAssistantByDoctorIdAndAssistantId(UUID doctorId, UUID assistantId);

    /**
     * Delete a doctor-assistant relationship by doctor id and assistant id
     * @param doctorId doctor id
     * @param assistantId assistant id
     */
    void deleteDoctorAssistantByDoctorIdAndAssistantId(UUID doctorId, UUID assistantId);

    /**
     * Check if a doctor-assistant relationship exists
     * @param doctorId doctor id
     * @param assistantId assistant id
     * @return true if the relationship exists, false otherwise
     */
    boolean existsDoctorAssistantByDoctorIdAndAssistantId(UUID doctorId, UUID assistantId);
}