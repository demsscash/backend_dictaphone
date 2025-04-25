package com.personnel.personnelservice.adapters.persistances.repositories;

import com.personnel.personnelservice.adapters.persistances.entities.Qualification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaQualificationRepository extends JpaBaseRepository<Qualification>{
    Optional<Qualification> findById(UUID patientId);
    List<Qualification> findByUserId(UUID userId);
}
