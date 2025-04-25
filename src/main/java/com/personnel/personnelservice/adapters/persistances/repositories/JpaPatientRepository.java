package com.personnel.personnelservice.adapters.persistances.repositories;


import com.personnel.personnelservice.adapters.persistances.entities.Patient;

import java.util.List;
import java.util.Optional;


public interface JpaPatientRepository extends JpaBaseRepository<Patient>  {
    Optional<Patient> findByEmail(String email);
    List<Patient> findPatientByCreatedBy(String createdBy);
}
