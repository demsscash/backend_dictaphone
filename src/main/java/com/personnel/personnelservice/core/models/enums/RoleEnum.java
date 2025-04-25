package com.personnel.personnelservice.core.models.enums;

public enum RoleEnum {
    ASSISTANT("Assistant"),
    PATIENT("Patient"),
    MEDECIN("Médecin");

    private final String roleName;

    RoleEnum(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
