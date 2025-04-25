package com.personnel.personnelservice.core.services;

import com.personnel.personnelservice.adapters.persistances.entities.Permission;
import com.personnel.personnelservice.adapters.persistances.entities.Role;
import com.personnel.personnelservice.core.exceptions.EntityNotFoundException;
import com.personnel.personnelservice.core.ports.services.RoleService;
import com.personnel.personnelservice.core.models.dtos.RoleDto;
import com.personnel.personnelservice.adapters.persistances.mappers.RoleMapper;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaPermissionRepository;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final JpaRoleRepository jpaRoleRepository;
    private final JpaPermissionRepository jpaPermissionRepository;
    private final RoleMapper roleMapper;

    public RoleDto getRoleById(UUID id) {
        Role role = jpaRoleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
        return roleMapper.toRoleDto(role);
    }

    public List<RoleDto> getAllRoles() {
        return jpaRoleRepository.findAll().stream().map(roleMapper::toRoleDto)
                .collect(Collectors.toList());
    }

    public RoleDto createRole(RoleDto roleRequestDto) {
        Role role = roleMapper.toEntity(roleRequestDto);
        if (roleRequestDto.getPermissions() != null && !roleRequestDto.getPermissions().isEmpty()) {
            Set<Permission> permissions = roleRequestDto.getPermissions().stream().map(name -> jpaPermissionRepository.findByName(name).orElseThrow(() -> new RuntimeException("Permission not found: " + name))).collect(Collectors.toSet());
            role.setPermissions(permissions);
        }
        Role savedRole = jpaRoleRepository.save(role);
        return roleMapper.toRoleDto(savedRole);
    }

    public RoleDto updateRole(UUID id, RoleDto roleRequestDto) {
        Role existingRole = jpaRoleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + id));
        existingRole.setName(roleRequestDto.getName());
        existingRole.setDescription(roleRequestDto.getDescription());
        if (roleRequestDto.getPermissions() != null) {
            Set<Permission> permissions = roleRequestDto.getPermissions().stream().map(name -> jpaPermissionRepository.findByName(name).orElseThrow(() -> new RuntimeException("Permission not found: " + name))).collect(Collectors.toSet());
            existingRole.setPermissions(permissions);
        }
        Role updatedRole = jpaRoleRepository.save(existingRole);
        return roleMapper.toRoleDto(updatedRole);
    }

    public void deleteRole(UUID id) {
        if (!jpaRoleRepository.existsById(id)) {
            throw new RuntimeException("Role not found with id: " + id);
        }
        jpaRoleRepository.deleteById(id);
    }
    @Transactional
    public RoleDto addPermissionsToRole(UUID roleId, Set<UUID> permissionIds) {
        Role role = jpaRoleRepository.findById(roleId).orElseThrow(() -> new EntityNotFoundException("Role not found"));
        Set<Permission> permissions = new HashSet<>(jpaPermissionRepository.findAllById(permissionIds));
        role.getPermissions().addAll(permissions);
        Role savedRole = jpaRoleRepository.save(role);
        return roleMapper.toRoleDto(savedRole);
    }

    @Override
    public RoleDto getRoleByName(String patient) {
        Optional<Role> role = jpaRoleRepository.findByName(patient);
        return  role.map(roleMapper::toRoleDto).orElseThrow(() -> new EntityNotFoundException("Role not found"));

    }
}
