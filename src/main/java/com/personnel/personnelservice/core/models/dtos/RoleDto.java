package com.personnel.personnelservice.core.models.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import com.personnel.personnelservice.core.models.views.Views;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RoleDto {
    @JsonView({ Views.Update.class, Views.Response.class})
    private UUID id;

    @JsonView({ Views.Create.class,Views.Update.class, Views.Response.class})
    private String name;

    @JsonView({ Views.Create.class,Views.Update.class, Views.Response.class})
    private String description;

    @JsonView({ Views.Create.class,Views.Update.class, Views.Response.class})
    private Set<String> permissions;

}