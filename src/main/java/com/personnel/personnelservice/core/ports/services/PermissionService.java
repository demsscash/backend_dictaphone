package com.personnel.personnelservice.core.ports.services;

import com.personnel.personnelservice.core.models.dtos.PermissionDto;

import java.util.List;
import java.util.UUID;

public interface PermissionService {
    /**
     * Retrieve a permission by its ID
     * @param id The ID of the permission
     * @return PermissionDto containing permission information
     */
    PermissionDto getPermissionById(UUID id);

    /**
     * Retrieve all permissions
     * @return List of all permissions
     */
    List<PermissionDto> getAllPermissions();


}