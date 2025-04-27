package com.personnel.personnelservice.core.services;

import com.personnel.personnelservice.adapters.persistances.entities.Assistant;
import com.personnel.personnelservice.adapters.persistances.entities.DoctorAssistant;
import com.personnel.personnelservice.adapters.persistances.entities.Medecin;
import com.personnel.personnelservice.adapters.persistances.mappers.DoctorAssistantMapper;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaAssistantRepository;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaDoctorAssistantRepository;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaMedecinRepository;
import com.personnel.personnelservice.core.exceptions.EntityNotFoundException;
import com.personnel.personnelservice.core.exceptions.RoleAlreadyAssignedException;
import com.personnel.personnelservice.core.models.dtos.DoctorAssistantDto;
import com.personnel.personnelservice.core.ports.services.DoctorAssistantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of the DoctorAssistantService interface
 */
@Service
@RequiredArgsConstructor
public class DoctorAssistantServiceImpl implements DoctorAssistantService {

    private final JpaDoctorAssistantRepository jpaDoctorAssistantRepository;
    private final JpaMedecinRepository jpaMedecinRepository;
    private final JpaAssistantRepository jpaAssistantRepository;
    private final DoctorAssistantMapper doctorAssistantMapper;

    @Override
    @Transactional
    public DoctorAssistantDto createDoctorAssistant(DoctorAssistantDto doctorAssistantDto) {
        // Check if the relationship already exists
        if (jpaDoctorAssistantRepository.existsByDoctorIdAndAssistantId(
                doctorAssistantDto.getDoctorId(), doctorAssistantDto.getAssistantId())) {
            throw new RoleAlreadyAssignedException("This assistant is already assigned to this doctor");
        }

        DoctorAssistant doctorAssistant = doctorAssistantMapper.toEntity(doctorAssistantDto);

        // Set the doctor
        Medecin doctor = jpaMedecinRepository.findById(doctorAssistantDto.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + doctorAssistantDto.getDoctorId()));
        doctorAssistant.setDoctor(doctor);

        // Set the assistant
        Assistant assistant = jpaAssistantRepository.findById(doctorAssistantDto.getAssistantId())
                .orElseThrow(() -> new EntityNotFoundException("Assistant not found with id: " + doctorAssistantDto.getAssistantId()));
        doctorAssistant.setAssistant(assistant);

        DoctorAssistant savedDoctorAssistant = jpaDoctorAssistantRepository.save(doctorAssistant);
        return doctorAssistantMapper.toDTO(savedDoctorAssistant);
    }

    @Override
    public DoctorAssistantDto getDoctorAssistantById(UUID id) {
        DoctorAssistant doctorAssistant = jpaDoctorAssistantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor-Assistant relationship not found with id: " + id));
        return doctorAssistantMapper.toDTO(doctorAssistant);
    }

    @Override
    @Transactional
    public DoctorAssistantDto updateDoctorAssistant(DoctorAssistantDto doctorAssistantDto) {
        DoctorAssistant doctorAssistant = jpaDoctorAssistantRepository.findById(doctorAssistantDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor-Assistant relationship not found with id: " + doctorAssistantDto.getId()));

        // Check if trying to change to an existing relationship
        if (doctorAssistantDto.getDoctorId() != null && doctorAssistantDto.getAssistantId() != null &&
                !doctorAssistant.getDoctor().getId().equals(doctorAssistantDto.getDoctorId()) &&
                !doctorAssistant.getAssistant().getId().equals(doctorAssistantDto.getAssistantId()) &&
                jpaDoctorAssistantRepository.existsByDoctorIdAndAssistantId(
                        doctorAssistantDto.getDoctorId(), doctorAssistantDto.getAssistantId())) {
            throw new RoleAlreadyAssignedException("This assistant is already assigned to this doctor");
        }

        doctorAssistantMapper.updateEntity(doctorAssistantDto, doctorAssistant);

        // Update the doctor if changed
        if (doctorAssistantDto.getDoctorId() != null &&
                !doctorAssistant.getDoctor().getId().equals(doctorAssistantDto.getDoctorId())) {
            Medecin doctor = jpaMedecinRepository.findById(doctorAssistantDto.getDoctorId())
                    .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + doctorAssistantDto.getDoctorId()));
            doctorAssistant.setDoctor(doctor);
        }

        // Update the assistant if changed
        if (doctorAssistantDto.getAssistantId() != null &&
                !doctorAssistant.getAssistant().getId().equals(doctorAssistantDto.getAssistantId())) {
            Assistant assistant = jpaAssistantRepository.findById(doctorAssistantDto.getAssistantId())
                    .orElseThrow(() -> new EntityNotFoundException("Assistant not found with id: " + doctorAssistantDto.getAssistantId()));
            doctorAssistant.setAssistant(assistant);
        }

        DoctorAssistant updatedDoctorAssistant = jpaDoctorAssistantRepository.save(doctorAssistant);
        return doctorAssistantMapper.toDTO(updatedDoctorAssistant);
    }

    @Override
    @Transactional
    public void deleteDoctorAssistant(UUID id) {
        if (!jpaDoctorAssistantRepository.existsById(id)) {
            throw new EntityNotFoundException("Doctor-Assistant relationship not found with id: " + id);
        }
        jpaDoctorAssistantRepository.deleteById(id);
    }

    @Override
    public List<DoctorAssistantDto> getAllDoctorAssistants() {
        return jpaDoctorAssistantRepository.findAll().stream()
                .map(doctorAssistantMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DoctorAssistantDto> getAllDoctorAssistants(Pageable pageable) {
        return jpaDoctorAssistantRepository.findAll(pageable)
                .map(doctorAssistantMapper::toDTO);
    }

    @Override
    public List<DoctorAssistantDto> getDoctorAssistantsByDoctorId(UUID doctorId) {
        return jpaDoctorAssistantRepository.findByDoctorId(doctorId).stream()
                .map(doctorAssistantMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DoctorAssistantDto> getDoctorAssistantsByDoctorId(UUID doctorId, Pageable pageable) {
        return jpaDoctorAssistantRepository.findByDoctorId(doctorId, pageable)
                .map(doctorAssistantMapper::toDTO);
    }

    @Override
    public List<DoctorAssistantDto> getDoctorAssistantsByAssistantId(UUID assistantId) {
        return jpaDoctorAssistantRepository.findByAssistantId(assistantId).stream()
                .map(doctorAssistantMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DoctorAssistantDto> getDoctorAssistantsByAssistantId(UUID assistantId, Pageable pageable) {
        return jpaDoctorAssistantRepository.findByAssistantId(assistantId, pageable)
                .map(doctorAssistantMapper::toDTO);
    }

    @Override
    public Optional<DoctorAssistantDto> getDoctorAssistantByDoctorIdAndAssistantId(UUID doctorId, UUID assistantId) {
        return jpaDoctorAssistantRepository.findByDoctorIdAndAssistantId(doctorId, assistantId)
                .map(doctorAssistantMapper::toDTO);
    }

    @Override
    @Transactional
    public void deleteDoctorAssistantByDoctorIdAndAssistantId(UUID doctorId, UUID assistantId) {
        jpaDoctorAssistantRepository.findByDoctorIdAndAssistantId(doctorId, assistantId)
                .ifPresentOrElse(
                        doctorAssistant -> jpaDoctorAssistantRepository.delete(doctorAssistant),
                        () -> { throw new EntityNotFoundException("Doctor-Assistant relationship not found"); }
                );
    }

    @Override
    public boolean existsDoctorAssistantByDoctorIdAndAssistantId(UUID doctorId, UUID assistantId) {
        return jpaDoctorAssistantRepository.existsByDoctorIdAndAssistantId(doctorId, assistantId);
    }
}