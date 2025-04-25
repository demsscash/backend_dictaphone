package com.personnel.personnelservice.adapters.persistances.mappers;
import com.personnel.personnelservice.adapters.persistances.entities.User;
import com.personnel.personnelservice.core.models.dtos.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {GrantedAuthority.class, Collection.class, Collectors.class})
public interface UserMapper {

    UserDto toDTO(User user);

    User toEntity(UserDto dto);
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "birthDate", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateEntity(UserDto dto, @MappingTarget User entity);
}