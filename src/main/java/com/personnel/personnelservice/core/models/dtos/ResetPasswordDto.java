package com.personnel.personnelservice.core.models.dtos;

import com.personnel.personnelservice.adapters.webs.validations.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ResetPasswordDto {
    private String token;
    @ValidPassword
    private String password;
    @ValidPassword
    private String confirmPassword;
}
