package com.personnel.personnelservice.adapters.persistances.mappers;

import com.personnel.personnelservice.adapters.persistances.entities.Permission;
import com.personnel.personnelservice.adapters.persistances.entities.Role;
import com.personnel.personnelservice.core.models.dtos.RoleDto;
import com.personnel.personnelservice.core.models.dtos.RoleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", expression = "java(getPermissions(role))")
    RoleDto toRoleDto(Role role);

    @Mapping(target = "permissions", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    Role toEntity(RoleDto roleRequestDto);

    default Set<String> getPermissions(Role role) {
        if (role.getPermissions() == null) {
            return new HashSet<>();
        }
        return role.getPermissions().stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());
    }
}