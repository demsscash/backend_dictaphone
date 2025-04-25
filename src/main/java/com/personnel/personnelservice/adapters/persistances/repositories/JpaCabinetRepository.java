package com.personnel.personnelservice.adapters.persistances.repositories;


import com.personnel.personnelservice.adapters.persistances.entities.Assistant;
import com.personnel.personnelservice.adapters.persistances.entities.Cabinet;
import com.personnel.personnelservice.adapters.persistances.entities.Medecin;
import com.personnel.personnelservice.adapters.persistances.entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;


public interface JpaCabinetRepository extends JpaBaseRepository<Cabinet> {

    @Query("SELECT a FROM Assistant a WHERE a.cabinet.id = :cabinetId")
    Page<Assistant> findAssistantsByCabinetId(@Param("cabinetId") UUID cabinetId, Pageable pageable);

    // Récupérer les patients paginés d'un cabinet
    @Query("SELECT p FROM Patient p WHERE p.cabinet.id = :cabinetId")
    Page<Patient> findPatientsByCabinetId(@Param("cabinetId") UUID cabinetId, Pageable pageable);

    @Query("SELECT p FROM Patient p WHERE p.cabinet.id = :cabinetId AND (LOWER(p.firstName) LIKE %:query% OR LOWER(p.lastName) LIKE %:query% OR p.phoneNumber LIKE %:query%)")
    Page<Patient> searchPatientsByCabinetId(@Param("cabinetId") UUID cabinetId, @Param("query") String query, Pageable pageable);

    // Récupérer les médecins paginés d'un cabinet
    @Query("SELECT m FROM Medecin m WHERE m.cabinet.id = :cabinetId")
    Page<Medecin> findDoctorsByCabinetId(@Param("cabinetId") UUID cabinetId, Pageable pageable);
}