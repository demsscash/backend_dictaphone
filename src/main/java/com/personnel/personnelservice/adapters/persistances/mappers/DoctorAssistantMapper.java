package com.personnel.personnelservice.adapters.persistances.mappers;

import com.personnel.personnelservice.adapters.persistances.entities.DoctorAssistant;
import com.personnel.personnelservice.core.models.dtos.DoctorAssistantDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DoctorAssistantMapper {

    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorName", expression = "java(getFullDoctorName(doctorAssistant))")
    @Mapping(target = "doctorEmail", source = "doctor.email")
    @Mapping(target = "assistantId", source = "assistant.id")
    @Mapping(target = "assistantName", expression = "java(getFullAssistantName(doctorAssistant))")
    @Mapping(target = "assistantEmail", source = "assistant.email")
    DoctorAssistantDto toDTO(DoctorAssistant doctorAssistant);

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "assistant", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    DoctorAssistant toEntity(DoctorAssistantDto doctorAssistantDto);

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "assistant", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntity(DoctorAssistantDto doctorAssistantDto, @MappingTarget DoctorAssistant doctorAssistant);

    default String getFullDoctorName(DoctorAssistant doctorAssistant) {
        if (doctorAssistant == null || doctorAssistant.getDoctor() == null) {
            return null;
        }
        return doctorAssistant.getDoctor().getFirstName() + " " + doctorAssistant.getDoctor().getLastName();
    }

    default String getFullAssistantName(DoctorAssistant doctorAssistant) {
        if (doctorAssistant == null || doctorAssistant.getAssistant() == null) {
            return null;
        }
        return doctorAssistant.getAssistant().getFirstName() + " " + doctorAssistant.getAssistant().getLastName();
    }
}