package com.personnel.personnelservice.core.models.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import com.personnel.personnelservice.core.models.views.Views;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoiceDto {

    @JsonView({Views.Update.class, Views.Response.class})
    private UUID id;

    @JsonView({Views.Create.class, Views.Update.class, Views.Response.class})
    private String path;

    @JsonView({Views.Create.class, Views.Update.class, Views.Response.class})
    private String title;

    @JsonView({Views.Create.class, Views.Update.class, Views.Response.class})
    private String remarque;

    @JsonView({Views.Response.class})
    private LocalDateTime noteCreated;

    @JsonView({Views.Create.class, Views.Update.class, Views.Response.class})
    private UUID doctorId;

    @JsonView({Views.Response.class})
    private String doctorName;

    @JsonView({Views.Create.class, Views.Update.class, Views.Response.class})
    private UUID patientId;

    @JsonView({Views.Response.class})
    private String patientName;

    @JsonView({Views.Response.class})
    private String createdBy;

    @JsonView({Views.Response.class})
    private LocalDateTime creationDate;

    @JsonView({Views.Response.class})
    private String lastModifiedBy;

    @JsonView({Views.Response.class})
    private LocalDateTime lastModifiedDate;
}