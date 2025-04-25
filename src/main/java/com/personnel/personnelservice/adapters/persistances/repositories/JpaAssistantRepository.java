package com.personnel.personnelservice.adapters.persistances.repositories;

import com.personnel.personnelservice.adapters.persistances.entities.Assistant;


import java.util.List;
import java.util.Optional;

public interface JpaAssistantRepository extends JpaBaseRepository<Assistant>  {
    Optional<Assistant> findByEmail(String email);
    List<Assistant> findAssistantByCreatedBy(String username);
}
