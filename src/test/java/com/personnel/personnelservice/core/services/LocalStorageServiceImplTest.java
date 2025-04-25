package com.personnel.personnelservice.core.services;

import com.github.javafaker.Faker;
import com.personnel.personnelservice.adapters.persistances.entities.MetaDataFile;
import com.personnel.personnelservice.adapters.persistances.mappers.MetadataFileMapper;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaMetaDataFileRepository;
import com.personnel.personnelservice.core.exceptions.EmptyFileException;
import com.personnel.personnelservice.core.models.dtos.MetadataFileDTO;
import com.personnel.personnelservice.core.models.enums.FileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocalStorageServiceImplTest {

    @Mock
    private JpaMetaDataFileRepository metadataFileRepository;

    @Mock
    private MetadataFileMapper metadataFileMapper;

    @InjectMocks
    private LocalStorageServiceImpl storageService;

    @Captor
    private ArgumentCaptor<MetaDataFile> metaDataFileCaptor;

    @TempDir
    Path tempDir;

    private Faker faker;

    @BeforeEach
    void setUp() {
        faker = new Faker();
        // Injecter le répertoire temporaire comme emplacement de stockage
        ReflectionTestUtils.setField(storageService, "storageLocation", tempDir.toString());
    }

    @Test
    @DisplayName("Devrait stocker un fichier avec succès")
    void storeFile_ShouldStoreFileSuccessfully() throws IOException {
        // Arrange
        String fileName = faker.file().fileName();
        String contentType = "application/pdf";
        byte[] content = faker.lorem().paragraph().getBytes();
        String fileType = FileType.RAPPORT.toString();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                fileName + ".pdf",
                contentType,
                content
        );

        MetaDataFile savedMetaDataFile = new MetaDataFile();
        savedMetaDataFile.setId(UUID.randomUUID());
        savedMetaDataFile.setFileName("generated-file-name.pdf");
        savedMetaDataFile.setExtension(".pdf");
        savedMetaDataFile.setMimeType(contentType);
        savedMetaDataFile.setType(FileType.RAPPORT);
        savedMetaDataFile.setPath(tempDir.toString());

        MetadataFileDTO expectedDto = new MetadataFileDTO();
        expectedDto.setId(savedMetaDataFile.getId());
        expectedDto.setFileName(savedMetaDataFile.getFileName());
        expectedDto.setExtension(savedMetaDataFile.getExtension());
        expectedDto.setMimeType(savedMetaDataFile.getMimeType());
        expectedDto.setType(savedMetaDataFile.getType());
        expectedDto.setPath(savedMetaDataFile.getPath());

       when(metadataFileRepository.save(any(MetaDataFile.class))).thenReturn(savedMetaDataFile);
       when(metadataFileMapper.toDTO(savedMetaDataFile)).thenReturn(expectedDto);

        // Act
        MetadataFileDTO result = storageService.storeFile(file, fileType);

        // Assert
        verify(metadataFileRepository).save(metaDataFileCaptor.capture());
        MetaDataFile capturedMetaDataFile = metaDataFileCaptor.getValue();

        assertThat(capturedMetaDataFile.getExtension()).isEqualTo(".pdf");
        assertThat(capturedMetaDataFile.getMimeType()).isEqualTo(contentType);
        assertThat(capturedMetaDataFile.getType()).isEqualTo(FileType.RAPPORT);
        assertThat(capturedMetaDataFile.getPath()).isEqualTo(tempDir.toString());
        assertThat(capturedMetaDataFile.getFileName()).isNotNull();

        assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("Devrait lancer une exception lorsque le fichier est vide")
    void storeFile_ShouldThrowException_WhenFileIsEmpty() {
        // Arrange
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "empty.pdf",
                "application/pdf",
                new byte[0]
        );

        // Act & Assert
        Throwable thrown = catchThrowable(() -> storageService.storeFile(emptyFile, FileType.RAPPORT.name()));
        assertThat(thrown)
                .isInstanceOf(EmptyFileException.class)
                .hasMessage("Le fichier est vide");

        verify(metadataFileRepository, never()).save(any());
    }


    @Test
    @DisplayName("Devrait récupérer les métadonnées du fichier avec succès")
    void getFileMetadata_ShouldReturnFileMetadata_WhenFileExists() {
        // Arrange
        UUID fileId = UUID.randomUUID();
        MetaDataFile metaDataFile = new MetaDataFile();
        metaDataFile.setId(fileId);
        metaDataFile.setFileName(faker.file().fileName());
        metaDataFile.setExtension(".pdf");
        metaDataFile.setMimeType("application/pdf");
        metaDataFile.setType(FileType.RAPPORT);
        metaDataFile.setPath(tempDir.toString());

        MetadataFileDTO expectedDto = new MetadataFileDTO();
        expectedDto.setId(fileId);
        expectedDto.setFileName(metaDataFile.getFileName());
        expectedDto.setExtension(metaDataFile.getExtension());
        expectedDto.setMimeType(metaDataFile.getMimeType());
        expectedDto.setType(metaDataFile.getType());
        expectedDto.setPath(metaDataFile.getPath());

        when(metadataFileRepository.findById(fileId)).thenReturn(Optional.of(metaDataFile));
        when(metadataFileMapper.toDTO(metaDataFile)).thenReturn(expectedDto);

        // Act
        Optional<MetadataFileDTO> result = storageService.getFileMetadata(fileId);

        // Assert
        assertThat(result).contains(expectedDto);
        verify(metadataFileRepository).findById(fileId);
        verify(metadataFileMapper).toDTO(metaDataFile);
    }

    @Test
    @DisplayName("Devrait retourner un Optional vide lorsque le fichier n'existe pas")
    void getFileMetadata_ShouldReturnEmptyOptional_WhenFileDoesNotExist() {
        // Arrange
        UUID fileId = UUID.randomUUID();
        when(metadataFileRepository.findById(fileId)).thenReturn(Optional.empty());

        // Act
        Optional<MetadataFileDTO> result = storageService.getFileMetadata(fileId);

        // Assert
        assertThat(result).isEmpty();
        verify(metadataFileRepository).findById(fileId);
        verify(metadataFileMapper, never()).toDTO(any());
    }
}