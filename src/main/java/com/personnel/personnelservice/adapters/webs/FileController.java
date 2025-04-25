package com.personnel.personnelservice.adapters.webs;


import com.fasterxml.jackson.annotation.JsonView;
import com.personnel.personnelservice.core.models.dtos.MetadataFileDTO;
import com.personnel.personnelservice.core.models.views.Views;
import com.personnel.personnelservice.core.ports.services.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Tag(name = "MetaData Controller", description = "This controller handles metadata related operations")
public class FileController {

    private final StorageService fileStorageService;


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a file", description = "Uploads a file and returns its metadata")
    @JsonView(Views.Response.class)
    public ResponseEntity<MetadataFileDTO> uploadFile(
            @JsonView(Views.Create.class)
            @Parameter(description = "File to upload", content = @Content(mediaType = "multipart/form-data"))
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileType") String fileType) throws IOException {

        MetadataFileDTO metadataFileDTO = fileStorageService.storeFile(file, fileType);
        return ResponseEntity.ok(metadataFileDTO);
    }

    @JsonView(Views.Response.class)
    @GetMapping("/metadata/{fileId}")
    public ResponseEntity<MetadataFileDTO> getFileMetadata(@PathVariable UUID fileId) {
        return fileStorageService.getFileMetadata(fileId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
