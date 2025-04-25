package com.personnel.personnelservice.core.ports.repositories;

import com.personnel.personnelservice.adapters.persistances.entities.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository {
    Role save(Role role);
    Optional<Role> findById(UUID id);
    void deleteById(UUID id);
    List<Role> findAll();
    Optional<Role> findByName(String roleName);
}
