package com.personnel.personnelservice.core.models.dtos;

public class JwtResponseDto {
    private String token;

    public JwtResponseDto(String token) {
        this.token = token;
    }
    public JwtResponseDto() {}
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
