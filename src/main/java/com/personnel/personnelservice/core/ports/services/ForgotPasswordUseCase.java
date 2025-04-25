package com.personnel.personnelservice.core.ports.services;

import com.personnel.personnelservice.core.models.dtos.ResetPasswordDto;

public interface ForgotPasswordUseCase {
    String forgotPassword(String email);
    String resetPassword(ResetPasswordDto resetPasswordDto);
}
