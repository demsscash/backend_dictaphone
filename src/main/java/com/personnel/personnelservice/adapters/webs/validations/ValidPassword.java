package com.personnel.personnelservice.adapters.webs.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Le mot de passe ne respecte pas les critères de sécurité";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
