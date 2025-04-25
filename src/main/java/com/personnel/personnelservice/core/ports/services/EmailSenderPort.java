package com.personnel.personnelservice.core.ports.services;

public interface EmailSenderPort {
    void sendEmail(String to, String subject, String body);
}
