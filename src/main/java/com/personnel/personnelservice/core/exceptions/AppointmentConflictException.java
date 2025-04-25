package com.personnel.personnelservice.core.exceptions;

import java.util.UUID;

public class AppointmentConflictException extends BaseException{
    public AppointmentConflictException(String message) {
        super(message);
    }
    public AppointmentConflictException(UUID id) {
        super("Appointment with id " + id + " already exists");
    }
}
