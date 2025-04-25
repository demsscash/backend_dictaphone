package com.personnel.personnelservice.adapters.webs.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface ValidPhoneNumber {
    String message() default "Le numéro de téléphone doit commencer par 0 avec 10 chiffres ou par +212 suivi de 9 chiffres";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
