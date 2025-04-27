package com.personnel.personnelservice.adapters.persistances.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "notes")
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE notes SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Note extends BaseEntity {

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contentTxt;

    private boolean validated;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "voice_id")
    private Voice voice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assistant_id")
    private Assistant assistant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "modifier_id")
    private User modifier;

    private boolean deleted = false;
}