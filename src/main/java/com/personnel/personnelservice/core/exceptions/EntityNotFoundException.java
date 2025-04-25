package com.personnel.personnelservice.core.exceptions;

import java.util.UUID;

public class EntityNotFoundException extends BaseException{
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String entity,UUID id)  {
        super("%s with id %s not found".formatted(entity,id));
    }
}
