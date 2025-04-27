package com.personnel.personnelservice.core.services;

import com.personnel.personnelservice.adapters.persistances.entities.DoctorPatient;
import com.personnel.personnelservice.adapters.persistances.entities.Medecin;
import com.personnel.personnelservice.adapters.persistances.entities.Patient;
import com.personnel.personnelservice.adapters.persistances.mappers.DoctorPatientMapper;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaDoctorPatientRepository;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaMedecinRepository;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaPatientRepository;
import com.personnel.personnelservice.core.exceptions.EntityNotFoundException;
import com.personnel.personnelservice.core.exceptions.RoleAlreadyAssignedException;
import com.personnel.personnelservice.core.models.dtos.DoctorPatientDto;
import com.personnel.personnelservice.core.ports.services.DoctorPatientService;
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
 * Implementation of the DoctorPatientService interface
 */
@Service
@RequiredArgsConstructor
public class DoctorPatientServiceImpl implements DoctorPatientService {

    private final JpaDoctorPatientRepository jpaDoctorPatientRepository;
    private final JpaMedecinRepository jpaMedecinRepository;
    private final JpaPatientRepository jpaPatientRepository;
    private final DoctorPatientMapper doctorPatientMapper;

    @Override
    @Transactional
    public DoctorPatientDto createDoctorPatient(DoctorPatientDto doctorPatientDto) {
        // Check if the relationship already exists
        if (jpaDoctorPatientRepository.existsByDoctorIdAndPatientId(
                doctorPatientDto.getDoctorId(), doctorPatientDto.getPatientId())) {
            throw new RoleAlreadyAssignedException("This patient is already assigned to this doctor");
        }

        DoctorPatient doctorPatient = doctorPatientMapper.toEntity(doctorPatientDto);

        // Set the doctor
        Medecin doctor = jpaMedecinRepository.findById(doctorPatientDto.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + doctorPatientDto.getDoctorId()));
        doctorPatient.setDoctor(doctor);

        // Set the patient
        Patient patient = jpaPatientRepository.findById(doctorPatientDto.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + doctorPatientDto.getPatientId()));
        doctorPatient.setPatient(patient);

        DoctorPatient savedDoctorPatient = jpaDoctorPatientRepository.save(doctorPatient);
        return doctorPatientMapper.toDTO(savedDoctorPatient);
    }

    @Override
    public DoctorPatientDto getDoctorPatientById(UUID id) {
        DoctorPatient doctorPatient = jpaDoctorPatientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Doctor-Patient relationship not found with id: " + id));
        return doctorPatientMapper.toDTO(doctorPatient);
    }

    @Override
    @Transactional
    public DoctorPatientDto updateDoctorPatient(DoctorPatientDto doctorPatientDto) {
        DoctorPatient doctorPatient = jpaDoctorPatientRepository.findById(doctorPatientDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor-Patient relationship not found with id: " + doctorPatientDto.getId()));

        // Check if trying to change to an existing relationship
        if (doctorPatientDto.getDoctorId() != null && doctorPatientDto.getPatientId() != null &&
                !doctorPatient.getDoctor().getId().equals(doctorPatientDto.getDoctorId()) &&
                !doctorPatient.getPatient().getId().equals(doctorPatientDto.getPatientId()) &&
                jpaDoctorPatientRepository.existsByDoctorIdAndPatientId(
                        doctorPatientDto.getDoctorId(), doctorPatientDto.getPatientId())) {
            throw new RoleAlreadyAssignedException("This patient is already assigned to this doctor");
        }

        doctorPatientMapper.updateEntity(doctorPatientDto, doctorPatient);

        // Update the doctor if changed
        if (doctorPatientDto.getDoctorId() != null &&
                !doctorPatient.getDoctor().getId().equals(doctorPatientDto.getDoctorId())) {
            Medecin doctor = jpaMedecinRepository.findById(doctorPatientDto.getDoctorId())
                    .orElseThrow(() -> new EntityNotFoundException("Doctor not found with id: " + doctorPatientDto.getDoctorId()));
            doctorPatient.setDoctor(doctor);
        }

        // Update the patient if changed
        if (doctorPatientDto.getPatientId() != null &&
                !doctorPatient.getPatient().getId().equals(doctorPatientDto.getPatientId())) {
            Patient patient = jpaPatientRepository.findById(doctorPatientDto.getPatientId())
                    .orElseThrow(() -> new EntityNotFoundException("Patient not found with id: " + doctorPatientDto.getPatientId()));
            doctorPatient.setPatient(patient);
        }

        DoctorPatient updatedDoctorPatient = jpaDoctorPatientRepository.save(doctorPatient);
        return doctorPatientMapper.toDTO(updatedDoctorPatient);
    }

    @Override
    @Transactional
    public void deleteDoctorPatient(UUID id) {
        if (!jpaDoctorPatientRepository.existsById(id)) {
            throw new EntityNotFoundException("Doctor-Patient relationship not found with id: " + id);
        }
        jpaDoctorPatientRepository.deleteById(id);
    }

    @Override
    public List<DoctorPatientDto> getAllDoctorPatients() {
        return jpaDoctorPatientRepository.findAll().stream()
                .map(doctorPatientMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DoctorPatientDto> getAllDoctorPatients(Pageable pageable) {
        return jpaDoctorPatientRepository.findAll(pageable)
                .map(doctorPatientMapper::toDTO);
    }

    @Override
    public List<DoctorPatientDto> getDoctorPatientsByDoctorId(UUID doctorId) {
        return jpaDoctorPatientRepository.findByDoctorId(doctorId).stream()
                .map(doctorPatientMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DoctorPatientDto> getDoctorPatientsByDoctorId(UUID doctorId, Pageable pageable) {
        return jpaDoctorPatientRepository.findByDoctorId(doctorId, pageable)
                .map(doctorPatientMapper::toDTO);
    }

    @Override
    public List<DoctorPatientDto> getDoctorPatientsByPatientId(UUID patientId) {
        return jpaDoctorPatientRepository.findByPatientId(patientId).stream()
                .map(doctorPatientMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DoctorPatientDto> getDoctorPatientsByPatientId(UUID patientId, Pageable pageable) {
        return jpaDoctorPatientRepository.findByPatientId(patientId, pageable)
                .map(doctorPatientMapper::toDTO);
    }

    @Override
    public Optional<DoctorPatientDto> getDoctorPatientByDoctorIdAndPatientId(UUID doctorId, UUID patientId) {
        return jpaDoctorPatientRepository.findByDoctorIdAndPatientId(doctorId, patientId)
                .map(doctorPatientMapper::toDTO);
    }

    @Override
    @Transactional
    public void deleteDoctorPatientByDoctorIdAndPatientId(UUID doctorId, UUID patientId) {
        jpaDoctorPatientRepository.findByDoctorIdAndPatientId(doctorId, patientId)
                .ifPresentOrElse(
                        doctorPatient -> jpaDoctorPatientRepository.delete(doctorPatient),
                        () -> { throw new EntityNotFoundException("Doctor-Patient relationship not found"); }
                );
    }

    @Override
    public boolean existsDoctorPatientByDoctorIdAndPatientId(UUID doctorId, UUID patientId) {
        return jpaDoctorPatientRepository.existsByDoctorIdAndPatientId(doctorId, patientId);
    }
}