package com.personnel.personnelservice.adapters.persistances.mappers;

import com.personnel.personnelservice.adapters.persistances.entities.Note;
import com.personnel.personnelservice.core.models.dtos.NoteDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NoteMapper {

    @Mapping(target = "voiceId", source = "voice.id")
    @Mapping(target = "voiceTitle", source = "voice.title")
    @Mapping(target = "assistantId", source = "assistant.id")
    @Mapping(target = "assistantName", expression = "java(getFullAssistantName(note))")
    @Mapping(target = "modifierId", source = "modifier.id")
    @Mapping(target = "modifierName", expression = "java(getFullModifierName(note))")
    NoteDto toDTO(Note note);

    @Mapping(target = "voice", ignore = true)
    @Mapping(target = "assistant", ignore = true)
    @Mapping(target = "modifier", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    Note toEntity(NoteDto noteDto);

    @Mapping(target = "voice", ignore = true)
    @Mapping(target = "assistant", ignore = true)
    @Mapping(target = "modifier", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntity(NoteDto noteDto, @MappingTarget Note note);

    default String getFullAssistantName(Note note) {
        if (note == null || note.getAssistant() == null) {
            return null;
        }
        return note.getAssistant().getFirstName() + " " + note.getAssistant().getLastName();
    }

    default String getFullModifierName(Note note) {
        if (note == null || note.getModifier() == null) {
            return null;
        }
        return note.getModifier().getFirstName() + " " + note.getModifier().getLastName();
    }
}