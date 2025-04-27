package com.personnel.personnelservice.adapters.persistances.mappers;

import com.personnel.personnelservice.adapters.persistances.entities.Payment;
import com.personnel.personnelservice.core.models.dtos.PaymentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", expression = "java(getFullUserName(payment))")
    @Mapping(target = "userEmail", source = "user.email")
    PaymentDto toDTO(Payment payment);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    Payment toEntity(PaymentDto paymentDto);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntity(PaymentDto paymentDto, @MappingTarget Payment payment);

    default String getFullUserName(Payment payment) {
        if (payment == null || payment.getUser() == null) {
            return null;
        }
        return payment.getUser().getFirstName() + " " + payment.getUser().getLastName();
    }
}