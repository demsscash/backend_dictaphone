package com.personnel.personnelservice.core.exceptions;

import java.util.UUID;

public class RoleAlreadyAssignedException extends BaseException{
    public RoleAlreadyAssignedException(String message) {
        super(message);
    }
    public RoleAlreadyAssignedException(UUID uuid) {
        super("Role already assigned to user with id: " + uuid);
    }
}
