package com.personnel.personnelservice.adapters.persistances.repositories;

import com.personnel.personnelservice.adapters.persistances.entities.Medecin;

import java.util.Optional;

public interface JpaMedecinRepository extends JpaBaseRepository<Medecin>  {
    Optional<Medecin> findByEmail(String email);
}
