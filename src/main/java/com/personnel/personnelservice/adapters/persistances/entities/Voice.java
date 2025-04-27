package com.personnel.personnelservice.adapters.persistances.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "voices")
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE voices SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Voice extends BaseEntity {

    @Column(nullable = false)
    private String path;

    private String title;

    private String remarque;

    private LocalDateTime noteCreated;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "doctor_id")
    private Medecin doctor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToMany(mappedBy = "voice", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Note> notes = new HashSet<>();

    private boolean deleted = false;
}