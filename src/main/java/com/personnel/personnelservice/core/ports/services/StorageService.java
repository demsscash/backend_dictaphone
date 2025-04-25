package com.personnel.personnelservice.core.ports.services;

import com.personnel.personnelservice.adapters.persistances.entities.MetaDataFile;
import com.personnel.personnelservice.core.models.dtos.MetadataFileDTO;
import io.jsonwebtoken.io.IOException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;


/**
 * this interface defines the methods that can be performed on Storage
 */
public interface StorageService {
    /**
     * this method is used to store a file in the storage
     * @param file the file to store
     * @param fileType the type of the file
     * @return the metadata of the file
     **/
    MetadataFileDTO storeFile(MultipartFile file, String fileType) throws IOException, java.io.IOException;

    //byte[] retrieveFile(String fileId) throws IOException;
    //boolean deleteFile(String fileId) throws IOException;
    //
    Optional<MetadataFileDTO> getFileMetadata(UUID fileId);
}