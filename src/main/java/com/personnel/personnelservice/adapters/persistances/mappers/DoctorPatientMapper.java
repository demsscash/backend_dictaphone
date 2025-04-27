package com.personnel.personnelservice.adapters.persistances.mappers;

import com.personnel.personnelservice.adapters.persistances.entities.DoctorPatient;
import com.personnel.personnelservice.core.models.dtos.DoctorPatientDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DoctorPatientMapper {

    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorName", expression = "java(getFullDoctorName(doctorPatient))")
    @Mapping(target = "doctorEmail", source = "doctor.email")
    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientName", expression = "java(getFullPatientName(doctorPatient))")
    @Mapping(target = "patientEmail", source = "patient.email")
    DoctorPatientDto toDTO(DoctorPatient doctorPatient);

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    DoctorPatient toEntity(DoctorPatientDto doctorPatientDto);

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntity(DoctorPatientDto doctorPatientDto, @MappingTarget DoctorPatient doctorPatient);

    default String getFullDoctorName(DoctorPatient doctorPatient) {
        if (doctorPatient == null || doctorPatient.getDoctor() == null) {
            return null;
        }
        return doctorPatient.getDoctor().getFirstName() + " " + doctorPatient.getDoctor().getLastName();
    }

    default String getFullPatientName(DoctorPatient doctorPatient) {
        if (doctorPatient == null || doctorPatient.getPatient() == null) {
            return null;
        }
        return doctorPatient.getPatient().getFirstName() + " " + doctorPatient.getPatient().getLastName();
    }
}