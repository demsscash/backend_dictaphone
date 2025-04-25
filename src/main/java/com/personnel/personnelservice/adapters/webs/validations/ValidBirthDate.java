package com.personnel.personnelservice.adapters.webs.validations;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BirthDateValidator.class)
public @interface ValidBirthDate {
    String message() default "La date de naissance doit être valide (pas dans le futur et pas plus de 110 ans dans le passé)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
