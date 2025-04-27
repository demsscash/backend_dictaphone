package com.personnel.personnelservice.adapters.persistances.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "doctor_patients")
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE doctor_patients SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class DoctorPatient extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Medecin doctor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    private boolean deleted = false;

    // Custom equals and hashCode to ensure unique doctor-patient pairs
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoctorPatient that = (DoctorPatient) o;

        if (doctor != null ? !doctor.getId().equals(that.doctor.getId()) : that.doctor != null) return false;
        return patient != null ? patient.getId().equals(that.patient.getId()) : that.patient == null;
    }

    @Override
    public int hashCode() {
        int result = doctor != null ? doctor.getId().hashCode() : 0;
        result = 31 * result + (patient != null ? patient.getId().hashCode() : 0);
        return result;
    }
}