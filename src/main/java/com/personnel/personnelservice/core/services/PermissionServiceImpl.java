package com.personnel.personnelservice.core.services;

import com.personnel.personnelservice.adapters.persistances.entities.Permission;
import com.personnel.personnelservice.core.exceptions.EntityNotFoundException;
import com.personnel.personnelservice.core.ports.services.PermissionService;
import com.personnel.personnelservice.core.models.dtos.PermissionDto;
import com.personnel.personnelservice.adapters.persistances.mappers.PermissionMapper;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaPermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionMapper permissionMapper;
    private final JpaPermissionRepository jpaPermissionRepository;

    @Override
    public PermissionDto getPermissionById(UUID id) {
        Permission permission = jpaPermissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permission not found with id: " + id));
        return permissionMapper.toPermissionDto(permission);
    }

    @Override
    public List<PermissionDto> getAllPermissions() {
        List<Permission> permissions = jpaPermissionRepository.findAll();
        return permissions.stream()
                .map(permissionMapper::toPermissionDto)
                .collect(Collectors.toList());
    }

}