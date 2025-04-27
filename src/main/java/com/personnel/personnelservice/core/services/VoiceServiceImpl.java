package com.personnel.personnelservice.core.services;

import com.personnel.personnelservice.adapters.persistances.entities.Medecin;
import com.personnel.personnelservice.adapters.persistances.entities.Patient;
import com.personnel.personnelservice.adapters.persistances.entities.Voice;
import com.personnel.personnelservice.adapters.persistances.mappers.VoiceMapper;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaMedecinRepository;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaPatientRepository;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaVoiceRepository;
import com.personnel.personnelservice.core.exceptions.EntityNotFoundException;
import com.personnel.personnelservice.core.models.dtos.VoiceDto;
import com.personnel.personnelservice.core.ports.services.VoiceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of the VoiceService interface
 */
@Service
@RequiredArgsConstructor
public class VoiceServiceImpl implements VoiceService {

    private final JpaVoiceRepository jpaVoiceRepository;
    private final JpaMedecinRepository jpaMedecinRepository;
    private final JpaPatientRepository jpaPatientRepository;
    private final VoiceMapper voiceMapper;

    @Override
    @Transactional
    public VoiceDto createVoice(VoiceDto voiceDto) {
        Voice voice = voiceMapper.toEntity(voiceDto);

        // Set the doctor
        if (voiceDto.getDoctorId() != null) {
            Medecin doctor = jpaMedecinRepository.findById(voiceDto.getDoctorId())
                    .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + voiceDto.getDoctorId()));
            voice.setDoctor(doctor);
        }

        // Set the patient
        if (voiceDto.getPatientId() != null) {
            Patient patient = jpaPatientRepository.findById(voiceDto.getPatientId())
                    .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + voiceDto.getPatientId()));
            voice.setPatient(patient);
        }

        Voice savedVoice = jpaVoiceRepository.save(voice);
        return voiceMapper.toDTO(savedVoice);
    }

    @Override
    public VoiceDto getVoiceById(UUID id) {
        Voice voice = jpaVoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Voice not found with id: " + id));
        return voiceMapper.toDTO(voice);
    }

    @Override
    @Transactional
    public VoiceDto updateVoice(VoiceDto voiceDto) {
        Voice voice = jpaVoiceRepository.findById(voiceDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Voice not found with id: " + voiceDto.getId()));

        voiceMapper.updateEntity(voiceDto, voice);

        // Update the doctor if changed
        if (voiceDto.getDoctorId() != null) {
            Medecin doctor = jpaMedecinRepository.findById(voiceDto.getDoctorId())
                    .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + voiceDto.getDoctorId()));
            voice.setDoctor(doctor);
        }

        // Update the patient if changed
        if (voiceDto.getPatientId() != null) {
            Patient patient = jpaPatientRepository.findById(voiceDto.getPatientId())
                    .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + voiceDto.getPatientId()));
            voice.setPatient(patient);
        }

        Voice updatedVoice = jpaVoiceRepository.save(voice);
        return voiceMapper.toDTO(updatedVoice);
    }

    @Override
    @Transactional
    public void deleteVoice(UUID id) {
        if (!jpaVoiceRepository.existsById(id)) {
            throw new EntityNotFoundException("Voice not found with id: " + id);
        }
        jpaVoiceRepository.deleteById(id);
    }

    @Override
    public List<VoiceDto> getAllVoices() {
        return jpaVoiceRepository.findAll().stream()
                .map(voiceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<VoiceDto> getAllVoices(Pageable pageable) {
        return jpaVoiceRepository.findAll(pageable)
                .map(voiceMapper::toDTO);
    }

    @Override
    public List<VoiceDto> getVoicesByDoctorId(UUID doctorId) {
        return jpaVoiceRepository.findByDoctorId(doctorId).stream()
                .map(voiceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<VoiceDto> getVoicesByDoctorId(UUID doctorId, Pageable pageable) {
        Medecin doctor = jpaMedecinRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + doctorId));

        return jpaVoiceRepository.findByDoctor(doctor, pageable)
                .map(voiceMapper::toDTO);
    }

    @Override
    public List<VoiceDto> getVoicesByPatientId(UUID patientId) {
        return jpaVoiceRepository.findByPatientId(patientId).stream()
                .map(voiceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<VoiceDto> getVoicesByPatientId(UUID patientId, Pageable pageable) {
        Patient patient = jpaPatientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + patientId));

        return jpaVoiceRepository.findByPatient(patient, pageable)
                .map(voiceMapper::toDTO);
    }

    @Override
    public List<VoiceDto> getVoicesByDoctorIdAndPatientId(UUID doctorId, UUID patientId) {
        return jpaVoiceRepository.findByDoctorIdAndPatientId(doctorId, patientId).stream()
                .map(voiceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<VoiceDto> getVoicesByDoctorIdAndPatientId(UUID doctorId, UUID patientId, Pageable pageable) {
        Medecin doctor = jpaMedecinRepository.findById(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + doctorId));

        Patient patient = jpaPatientRepository.findById(patientId)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + patientId));

        return jpaVoiceRepository.findByDoctorAndPatient(doctor, patient, pageable)
                .map(voiceMapper::toDTO);
    }

    @Override
    public Page<VoiceDto> searchVoicesByTitle(String searchText, Pageable pageable) {
        return jpaVoiceRepository.findByTitleContainingIgnoreCase(searchText, pageable)
                .map(voiceMapper::toDTO);
    }
}