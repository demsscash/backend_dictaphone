package com.personnel.personnelservice.adapters.persistances.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "doctor_assistants")
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE doctor_assistants SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class DoctorAssistant extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Medecin doctor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assistant_id", nullable = false)
    private Assistant assistant;

    private boolean deleted = false;

    // Custom equals and hashCode to ensure unique doctor-assistant pairs
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoctorAssistant that = (DoctorAssistant) o;

        if (doctor != null ? !doctor.getId().equals(that.doctor.getId()) : that.doctor != null) return false;
        return assistant != null ? assistant.getId().equals(that.assistant.getId()) : that.assistant == null;
    }

    @Override
    public int hashCode() {
        int result = doctor != null ? doctor.getId().hashCode() : 0;
        result = 31 * result + (assistant != null ? assistant.getId().hashCode() : 0);
        return result;
    }
}