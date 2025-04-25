package com.personnel.personnelservice.core.ports.services;

import com.personnel.personnelservice.core.models.dtos.RoleDto;
import com.personnel.personnelservice.core.models.dtos.RoleDto;

import java.security.Policy;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface RoleService {
    /**
     * Retrieve a role by its ID
     * @param id The ID of the role
     * @return RoleDto containing role information
     */
    RoleDto getRoleById(UUID id);

    /**
     * Retrieve all roles
     * @return List of all roles
     */
    List<RoleDto> getAllRoles();

    /**
     * Create a new role
     * @param RoleDto DTO containing role information
     * @return RoleDto of the created role
     */
    RoleDto createRole(RoleDto roleRequestDto);

    /**
     * Update an existing role
     * @param id The ID of the role to update
     * @param RoleDto DTO containing updated role information
     * @return RoleDto of the updated role
     */
    RoleDto updateRole(UUID id, RoleDto roleRequestDto);

    /**
     * Delete a role by its ID
     * @param id The ID of the role to delete
     */
    void deleteRole(UUID id);

    /**
     * Add permissions to a role
     * @param roleId The ID of the role
     * @param permissionIds Set of permission IDs to add
     * @return RoleDto of the updated role
     */
    RoleDto addPermissionsToRole(UUID roleId, Set<UUID> permissionIds);

    RoleDto getRoleByName(String patient);
}