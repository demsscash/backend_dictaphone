package com.personnel.personnelservice.adapters.persistances.mappers;

import com.personnel.personnelservice.adapters.persistances.entities.Voice;
import com.personnel.personnelservice.core.models.dtos.VoiceDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VoiceMapper {

    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorName", expression = "java(getFullDoctorName(voice))")
    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientName", expression = "java(getFullPatientName(voice))")
    VoiceDto toDTO(Voice voice);

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "notes", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    Voice toEntity(VoiceDto voiceDto);

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "notes", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntity(VoiceDto voiceDto, @MappingTarget Voice voice);

    default String getFullDoctorName(Voice voice) {
        if (voice == null || voice.getDoctor() == null) {
            return null;
        }
        return voice.getDoctor().getFirstName() + " " + voice.getDoctor().getLastName();
    }

    default String getFullPatientName(Voice voice) {
        if (voice == null || voice.getPatient() == null) {
            return null;
        }
        return voice.getPatient().getFirstName() + " " + voice.getPatient().getLastName();
    }
}