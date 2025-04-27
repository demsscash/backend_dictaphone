package com.personnel.personnelservice.core.ports.services;

import com.personnel.personnelservice.core.models.dtos.VoiceDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing voice recordings
 */
public interface VoiceService {

    /**
     * Create a new voice recording
     * @param voiceDto voice recording data
     * @return the created voice recording
     */
    VoiceDto createVoice(VoiceDto voiceDto);

    /**
     * Get a voice recording by id
     * @param id voice recording id
     * @return the voice recording
     */
    VoiceDto getVoiceById(UUID id);

    /**
     * Update a voice recording
     * @param voiceDto voice recording data
     * @return the updated voice recording
     */
    VoiceDto updateVoice(VoiceDto voiceDto);

    /**
     * Delete a voice recording
     * @param id voice recording id
     */
    void deleteVoice(UUID id);

    /**
     * Get all voice recordings
     * @return list of all voice recordings
     */
    List<VoiceDto> getAllVoices();

    /**
     * Get paginated voice recordings
     * @param pageable pagination information
     * @return paginated list of voice recordings
     */
    Page<VoiceDto> getAllVoices(Pageable pageable);

    /**
     * Get all voice recordings by doctor id
     * @param doctorId doctor id
     * @return list of voice recordings
     */
    List<VoiceDto> getVoicesByDoctorId(UUID doctorId);

    /**
     * Get paginated voice recordings by doctor id
     * @param doctorId doctor id
     * @param pageable pagination information
     * @return paginated list of voice recordings
     */
    Page<VoiceDto> getVoicesByDoctorId(UUID doctorId, Pageable pageable);

    /**
     * Get all voice recordings by patient id
     * @param patientId patient id
     * @return list of voice recordings
     */
    List<VoiceDto> getVoicesByPatientId(UUID patientId);

    /**
     * Get paginated voice recordings by patient id
     * @param patientId patient id
     * @param pageable pagination information
     * @return paginated list of voice recordings
     */
    Page<VoiceDto> getVoicesByPatientId(UUID patientId, Pageable pageable);

    /**
     * Get all voice recordings by doctor id and patient id
     * @param doctorId doctor id
     * @param patientId patient id
     * @return list of voice recordings
     */
    List<VoiceDto> getVoicesByDoctorIdAndPatientId(UUID doctorId, UUID patientId);

    /**
     * Get paginated voice recordings by doctor id and patient id
     * @param doctorId doctor id
     * @param patientId patient id
     * @param pageable pagination information
     * @return paginated list of voice recordings
     */
    Page<VoiceDto> getVoicesByDoctorIdAndPatientId(UUID doctorId, UUID patientId, Pageable pageable);

    /**
     * Search voice recordings by title
     * @param searchText search text
     * @param pageable pagination information
     * @return paginated list of voice recordings
     */
    Page<VoiceDto> searchVoicesByTitle(String searchText, Pageable pageable);
}