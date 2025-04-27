package com.personnel.personnelservice.adapters.persistances.repositories;

import com.personnel.personnelservice.adapters.persistances.entities.Note;
import com.personnel.personnelservice.adapters.persistances.entities.Voice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaNoteRepository extends JpaBaseRepository<Note> {

    /**
     * Find all notes by voice
     * @param voiceId the voice id
     * @return list of notes
     */
    List<Note> findByVoiceId(UUID voiceId);

    /**
     * Find all notes by voice
     * @param voice the voice
     * @return list of notes
     */
    List<Note> findByVoice(Voice voice);

    /**
     * Find paginated notes by voice
     * @param voice the voice
     * @param pageable pagination information
     * @return paginated list of notes
     */
    Page<Note> findByVoice(Voice voice, Pageable pageable);

    /**
     * Find the latest note for a voice
     * @param voiceId the voice id
     * @return the latest note
     */
    Optional<Note> findTopByVoiceIdOrderByCreationDateDesc(UUID voiceId);

    /**
     * Find all validated notes by voice
     * @param voiceId the voice id
     * @return list of validated notes
     */
    List<Note> findByVoiceIdAndValidatedTrue(UUID voiceId);

    /**
     * Find all notes by assistant
     * @param assistantId the assistant id
     * @return list of notes
     */
    List<Note> findByAssistantId(UUID assistantId);

    /**
     * Find paginated notes by assistant
     * @param assistantId the assistant id
     * @param pageable pagination information
     * @return paginated list of notes
     */
    Page<Note> findByAssistantId(UUID assistantId, Pageable pageable);

    /**
     * Find all notes by modifier
     * @param modifierId the modifier id
     * @return list of notes
     */
    List<Note> findByModifierId(UUID modifierId);

    /**
     * Find notes containing a specific text
     * @param searchText the search text
     * @param pageable pagination information
     * @return paginated list of notes
     */
    Page<Note> findByContentTxtContainingIgnoreCase(String searchText, Pageable pageable);
}