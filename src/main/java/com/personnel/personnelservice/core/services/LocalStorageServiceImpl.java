package com.personnel.personnelservice.core.services;

import com.personnel.personnelservice.adapters.persistances.entities.MetaDataFile;
import com.personnel.personnelservice.adapters.persistances.mappers.MetadataFileMapper;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaMetaDataFileRepository;
import com.personnel.personnelservice.core.exceptions.EmptyFileException;
import com.personnel.personnelservice.core.models.dtos.MetadataFileDTO;
import com.personnel.personnelservice.core.models.enums.FileType;
import com.personnel.personnelservice.core.ports.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class LocalStorageServiceImpl implements StorageService {

    @Value("${file.storage.location:./uploads}")
    private String storageLocation;

    private final JpaMetaDataFileRepository metadataFileRepository;
    private final MetadataFileMapper metadataFileMapper;

    @Override
    public MetadataFileDTO storeFile(MultipartFile file, String fileType) throws IOException {
        if (file.isEmpty()) {
            throw new EmptyFileException("Le fichier est vide");
        }

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = getFileExtension(originalFileName);

        String storedFileName = UUID.randomUUID().toString().replace("-", "") + fileExtension;

        MetaDataFile metadataFile = new MetaDataFile();
        metadataFile.setFileName(storedFileName);
        metadataFile.setExtension(fileExtension);
        metadataFile.setMimeType(file.getContentType());
        metadataFile.setType(FileType.valueOf(fileType));
        metadataFile.setPath(storageLocation);

        Path targetPath = Paths.get(storageLocation).resolve(storedFileName);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        MetaDataFile savedFile = metadataFileRepository.save(metadataFile);

        return metadataFileMapper.toDTO(savedFile);
    }

    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex);
    }

    @Override
    public Optional<MetadataFileDTO> getFileMetadata(UUID fileId) {
        return metadataFileRepository.findById(fileId)
                .map(metadataFileMapper::toDTO);
    }
}
