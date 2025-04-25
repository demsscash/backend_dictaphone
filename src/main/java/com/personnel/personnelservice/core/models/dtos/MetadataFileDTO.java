package com.personnel.personnelservice.core.models.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import com.personnel.personnelservice.core.models.enums.FileType;
import com.personnel.personnelservice.core.models.views.Views;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MetadataFileDTO {
    @JsonView({Views.Response.class, Views.Update.class})
    private UUID id;
    @JsonView({Views.Response.class, Views.Create.class})
    private FileType type;
    @JsonView({Views.Response.class, Views.Update.class})
    private String extension;
    @JsonView({Views.Response.class, Views.Update.class})
    private String fileName;
    @JsonView({Views.Response.class, Views.Update.class})
    private String mimeType;
    @JsonView({Views.Response.class, Views.Update.class})
    private String path;
}