package com.personnel.personnelservice.adapters.persistances.repositories;

import com.personnel.personnelservice.adapters.persistances.entities.Role;

import java.util.Optional;

public interface JpaRoleRepository extends JpaBaseRepository<Role> {
    Optional<Role> findByName(String patient);
}
