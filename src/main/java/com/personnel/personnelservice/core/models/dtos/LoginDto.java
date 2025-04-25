package com.personnel.personnelservice.core.models.dtos;

import com.personnel.personnelservice.adapters.webs.validations.ValidPassword;
import jakarta.validation.constraints.Email;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    @Email
    private String email;
    @ValidPassword
    private String password;
    private boolean rememberMe;
}
