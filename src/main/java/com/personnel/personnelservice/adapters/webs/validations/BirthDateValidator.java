package com.personnel.personnelservice.adapters.webs.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class BirthDateValidator implements ConstraintValidator<ValidBirthDate, LocalDate> {

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        if (birthDate == null) {
            return false;
        }
        LocalDate today = LocalDate.now();
        if (birthDate.isAfter(today)) {
            return false;
        }
        LocalDate minDate = today.minusYears(110);
        return !birthDate.isBefore(minDate);
    }
}
