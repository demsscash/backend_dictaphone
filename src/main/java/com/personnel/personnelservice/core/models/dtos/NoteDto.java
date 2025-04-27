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
public class NoteDto {

    @JsonView({Views.Update.class, Views.Response.class})
    private UUID id;

    @JsonView({Views.Create.class, Views.Update.class, Views.Response.class})
    private String contentTxt;

    @JsonView({Views.Create.class, Views.Update.class, Views.Response.class})
    private boolean validated;

    @JsonView({Views.Create.class, Views.Update.class, Views.Response.class})
    private UUID voiceId;

    @JsonView({Views.Response.class})
    private String voiceTitle;

    @JsonView({Views.Create.class, Views.Update.class, Views.Response.class})
    private UUID assistantId;

    @JsonView({Views.Response.class})
    private String assistantName;

    @JsonView({Views.Create.class, Views.Update.class, Views.Response.class})
    private UUID modifierId;

    @JsonView({Views.Response.class})
    private String modifierName;

    @JsonView({Views.Response.class})
    private String createdBy;

    @JsonView({Views.Response.class})
    private LocalDateTime creationDate;

    @JsonView({Views.Response.class})
    private String lastModifiedBy;

    @JsonView({Views.Response.class})
    private LocalDateTime lastModifiedDate;
}