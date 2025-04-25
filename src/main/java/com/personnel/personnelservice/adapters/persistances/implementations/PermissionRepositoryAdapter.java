package com.personnel.personnelservice.adapters.persistances.implementations;

import com.personnel.personnelservice.adapters.persistances.entities.Permission;
import com.personnel.personnelservice.core.ports.repositories.PermissionRepository;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaPermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PermissionRepositoryAdapter implements PermissionRepository {
    private final JpaPermissionRepository jpaPermissionRepository;
    @Override
    public Optional<Permission> findById(UUID id) {
        return jpaPermissionRepository.findById(id);
    }

    @Override
    public List<Permission> findAll() {
        return jpaPermissionRepository.findAll();
    }
}
