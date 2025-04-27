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
public class PaymentDto {

    @JsonView({Views.Update.class, Views.Response.class})
    private UUID id;

    @JsonView({Views.Create.class, Views.Update.class, Views.Response.class})
    private UUID userId;

    @JsonView({Views.Response.class})
    private String userName;

    @JsonView({Views.Response.class})
    private String userEmail;

    @JsonView({Views.Create.class, Views.Update.class, Views.Response.class})
    private String productId;

    @JsonView({Views.Create.class, Views.Update.class, Views.Response.class})
    private String platform;

    @JsonView({Views.Create.class, Views.Update.class, Views.Response.class})
    private String purchaseToken;

    @JsonView({Views.Create.class, Views.Update.class, Views.Response.class})
    private String receiptData;

    @JsonView({Views.Create.class, Views.Update.class, Views.Response.class})
    private LocalDateTime startDate;

    @JsonView({Views.Create.class, Views.Update.class, Views.Response.class})
    private LocalDateTime endDate;

    @JsonView({Views.Create.class, Views.Update.class, Views.Response.class})
    private boolean active;

    @JsonView({Views.Response.class})
    private String createdBy;

    @JsonView({Views.Response.class})
    private LocalDateTime creationDate;

    @JsonView({Views.Response.class})
    private String lastModifiedBy;

    @JsonView({Views.Response.class})
    private LocalDateTime lastModifiedDate;
}