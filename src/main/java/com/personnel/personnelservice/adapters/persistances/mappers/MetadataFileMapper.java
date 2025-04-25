package com.personnel.personnelservice.adapters.persistances.mappers;

import com.personnel.personnelservice.adapters.persistances.entities.MetaDataFile;
import com.personnel.personnelservice.core.models.dtos.MetadataFileDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MetadataFileMapper {
    MetadataFileDTO toDTO(MetaDataFile metaDataFile);
    MetaDataFile toEntity(MetadataFileDTO metadataFileDTO);
}