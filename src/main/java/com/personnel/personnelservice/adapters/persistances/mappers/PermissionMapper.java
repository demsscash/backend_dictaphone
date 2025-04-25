package com.personnel.personnelservice.adapters.persistances.mappers;

import com.personnel.personnelservice.adapters.persistances.entities.Permission;
import com.personnel.personnelservice.core.models.dtos.PermissionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toEntity(PermissionDto permissionRequestDto);

    PermissionDto toPermissionDto(Permission permission);
}