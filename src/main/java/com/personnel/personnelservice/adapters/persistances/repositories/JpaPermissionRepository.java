package com.personnel.personnelservice.adapters.persistances.repositories;

import com.personnel.personnelservice.adapters.persistances.entities.Permission;

import java.util.List;
import java.util.Optional;

public interface JpaPermissionRepository extends JpaBaseRepository<Permission> {
    Optional<Permission> findByName(String name);
    List<Permission> findAll();
}
