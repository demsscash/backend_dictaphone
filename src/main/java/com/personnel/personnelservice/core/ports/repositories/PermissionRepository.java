package com.personnel.personnelservice.core.ports.repositories;

import com.personnel.personnelservice.adapters.persistances.entities.Permission;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository {
    Optional<Permission> findById(UUID id);
    List<Permission> findAll();
}
