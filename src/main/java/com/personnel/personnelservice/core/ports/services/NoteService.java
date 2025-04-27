package com.personnel.personnelservice.core.ports.services;

import com.personnel.personnelservice.core.models.dtos.NoteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service interface for managing notes
 */
public interface NoteService {

    /**
     * Create a new note
     * @param noteDto note data
     * @return the created note
     */
    NoteDto createNote(NoteDto noteDto);

    /**
     * Get a note by id
     * @param id note id
     * @return the note
     */
    NoteDto getNoteById(UUID id);

    /**
     * Update a note
     * @param noteDto note data
     * @return the updated note
     */
    NoteDto updateNote(NoteDto noteDto);

    /**
     * Delete a note
     * @param id note id
     */
    void deleteNote(UUID id);

    /**
     * Get all notes
     * @return list of all notes
     */
    List<NoteDto> getAllNotes();

    /**
     * Get paginated notes
     * @param pageable pagination information
     * @return paginated list of notes
     */
    Page<NoteDto> getAllNotes(Pageable pageable);

    /**
     * Get all notes by voice id
     * @param voiceId voice id
     * @return list of notes
     */
    List<NoteDto> getNotesByVoiceId(UUID voiceId);

    /**
     * Get paginated notes by voice id
     * @param voiceId voice id
     * @param pageable pagination information
     * @return paginated list of notes
     */
    Page<NoteDto> getNotesByVoiceId(UUID voiceId, Pageable pageable);

    /**
     * Get latest note by voice id
     * @param voiceId voice id
     * @return the latest note or empty if none found
     */
    Optional<NoteDto> getLatestNoteByVoiceId(UUID voiceId);

    /**
     * Get all validated notes by voice id
     * @param voiceId voice id
     * @return list of validated notes
     */
    List<NoteDto> getValidatedNotesByVoiceId(UUID voiceId);

    /**
     * Get all notes by assistant id
     * @param assistantId assistant id
     * @return list of notes
     */
    List<NoteDto> getNotesByAssistantId(UUID assistantId);

    /**
     * Get paginated notes by assistant id
     * @param assistantId assistant id
     * @param pageable pagination information
     * @return paginated list of notes
     */
    Page<NoteDto> getNotesByAssistantId(UUID assistantId, Pageable pageable);

    /**
     * Get all notes by modifier id
     * @param modifierId modifier id
     * @return list of notes
     */
    List<NoteDto> getNotesByModifierId(UUID modifierId);

    /**
     * Search notes by content
     * @param searchText search text
     * @param pageable pagination information
     * @return paginated list of notes
     */
    Page<NoteDto> searchNotesByContent(String searchText, Pageable pageable);

    /**
     * Validate a note
     * @param noteId note id
     * @param modifierId modifier id
     * @return the validated note
     */
    NoteDto validateNote(UUID noteId, UUID modifierId);
}