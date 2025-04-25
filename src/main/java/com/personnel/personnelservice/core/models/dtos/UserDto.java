package com.personnel.personnelservice.core.models.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import com.personnel.personnelservice.adapters.webs.validations.ValidBirthDate;
import com.personnel.personnelservice.adapters.webs.validations.ValidPassword;
import com.personnel.personnelservice.adapters.webs.validations.ValidPhoneNumber;
import com.personnel.personnelservice.core.models.views.Views;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @JsonView({Views.Update.class, Views.Response.class})
    private UUID id;

    @JsonView({Views.Create.class, Views.Update.class, Views.Response.class})
    private String lastName;

    @JsonView({Views.Create.class, Views.Update.class, Views.Response.class})
    private String firstName;

    @JsonView({Views.Create.class, Views.Response.class})
    @Email
    private String email;

    @JsonView({Views.Create.class})
    @ValidPassword
    private String password;

    @JsonView( Views.Response.class)
    private UUID imageId;

    @JsonView({Views.Create.class, Views.Update.class, Views.Response.class})
    @ValidPhoneNumber
    private String phoneNumber;

    @JsonView({Views.Create.class, Views.Response.class})
    @ValidBirthDate
    private LocalDate birthDate;

    @JsonView({Views.Create.class, Views.Update.class, Views.Response.class})
    private String gender;

    @JsonView({Views.Create.class, Views.Update.class, Views.Response.class})
    private String address;

    @JsonView({Views.Create.class, Views.Update.class, Views.Response.class})
    private String idCard;

    @JsonView(Views.Response.class)
    private String createdBy;

    @JsonView(Views.Response.class)
    private LocalDateTime creationDate;

    @JsonView(Views.Response.class)
    private String lastModifiedBy;

    @JsonView(Views.Response.class)
    private LocalDateTime lastModifiedDate;
}
