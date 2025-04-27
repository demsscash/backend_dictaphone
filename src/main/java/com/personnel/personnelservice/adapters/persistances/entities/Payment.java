package com.personnel.personnelservice.adapters.persistances.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE payments SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Payment extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String productId;

    private String platform;

    private String purchaseToken;

    private String receiptData;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean active;

    private boolean deleted = false;
}