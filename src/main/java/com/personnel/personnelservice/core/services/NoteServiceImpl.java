package com.personnel.personnelservice.core.services;

import com.personnel.personnelservice.adapters.persistances.entities.Assistant;
import com.personnel.personnelservice.adapters.persistances.entities.Note;
import com.personnel.personnelservice.adapters.persistances.entities.User;
import com.personnel.personnelservice.adapters.persistances.entities.Voice;
import com.personnel.personnelservice.adapters.persistances.mappers.NoteMapper;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaAssistantRepository;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaNoteRepository;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaUserRepository;
import com.personnel.personnelservice.adapters.persistances.repositories.JpaVoiceRepository;
import com.personnel.personnelservice.core.exceptions.EntityNotFoundException;
import com.personnel.personnelservice.core.models.dtos.NoteDto;
import com.personnel.personnelservice.core.ports.services.NoteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of the NoteService interface
 */
@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final JpaNoteRepository jpaNoteRepository;
    private final JpaVoiceRepository jpaVoiceRepository;
    private final JpaAssistantRepository jpaAssistantRepository;
    private final JpaUserRepository jpaUserRepository;
    private final NoteMapper noteMapper;

    @Override
    @Transactional
    public NoteDto createNote(NoteDto noteDto) {
        Note note = noteMapper.toEntity(noteDto);

        // Set the voice
        if (noteDto.getVoiceId() != null) {
            Voice voice = jpaVoiceRepository.findById(noteDto.getVoiceId())
                    .orElseThrow(() -> new EntityNotFoundException("Voice not found with id: " + noteDto.getVoiceId()));
            note.setVoice(voice);
        }

        // Set the assistant
        if (noteDto.getAssistantId() != null) {
            Assistant assistant = jpaAssistantRepository.findById(noteDto.getAssistantId())
                    .orElseThrow(() -> new EntityNotFoundException("Assistant not found with id: " + noteDto.getAssistantId()));
            note.setAssistant(assistant);
        }

        // Set the modifier
        if (noteDto.getModifierId() != null) {
            User modifier = jpaUserRepository.findById(noteDto.getModifierId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + noteDto.getModifierId()));
            note.setModifier(modifier);
        }

        Note savedNote = jpaNoteRepository.save(note);
        return noteMapper.toDTO(savedNote);
    }

    @Override
    public NoteDto getNoteById(UUID id) {
        Note note = jpaNoteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Note not found with id: " + id));
        return noteMapper.toDTO(note);
    }

    @Override
    @Transactional
    public NoteDto updateNote(NoteDto noteDto) {
        Note note = jpaNoteRepository.findById(noteDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Note not found with id: " + noteDto.getId()));

        noteMapper.updateEntity(noteDto, note);

        // Update the voice if changed
        if (noteDto.getVoiceId() != null) {
            Voice voice = jpaVoiceRepository.findById(noteDto.getVoiceId())
                    .orElseThrow(() -> new EntityNotFoundException("Voice not found with id: " + noteDto.getVoiceId()));
            note.setVoice(voice);
        }

        // Update the assistant if changed
        if (noteDto.getAssistantId() != null) {
            Assistant assistant = jpaAssistantRepository.findById(noteDto.getAssistantId())
                    .orElseThrow(() -> new EntityNotFoundException("Assistant not found with id: " + noteDto.getAssistantId()));
            note.setAssistant(assistant);
        }

        // Update the modifier if changed
        if (noteDto.getModifierId() != null) {
            User modifier = jpaUserRepository.findById(noteDto.getModifierId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + noteDto.getModifierId()));
            note.setModifier(modifier);
        }

        Note updatedNote = jpaNoteRepository.save(note);
        return noteMapper.toDTO(updatedNote);
    }

    @Override
    @Transactional
    public void deleteNote(UUID id) {
        if (!jpaNoteRepository.existsById(id)) {
            throw new EntityNotFoundException("Note not found with id: " + id);
        }
        jpaNoteRepository.deleteById(id);
    }

    @Override
    public List<NoteDto> getAllNotes() {
        return jpaNoteRepository.findAll().stream()
                .map(noteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<NoteDto> getAllNotes(Pageable pageable) {
        return jpaNoteRepository.findAll(pageable)
                .map(noteMapper::toDTO);
    }

    @Override
    public List<NoteDto> getNotesByVoiceId(UUID voiceId) {
        return jpaNoteRepository.findByVoiceId(voiceId).stream()
                .map(noteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<NoteDto> getNotesByVoiceId(UUID voiceId, Pageable pageable) {
        Voice voice = jpaVoiceRepository.findById(voiceId)
                .orElseThrow(() -> new EntityNotFoundException("Voice not found with id: " + voiceId));

        return jpaNoteRepository.findByVoice(voice, pageable)
                .map(noteMapper::toDTO);
    }

    @Override
    public Optional<NoteDto> getLatestNoteByVoiceId(UUID voiceId) {
        return jpaNoteRepository.findTopByVoiceIdOrderByCreationDateDesc(voiceId)
                .map(noteMapper::toDTO);
    }

    @Override
    public List<NoteDto> getValidatedNotesByVoiceId(UUID voiceId) {
        return jpaNoteRepository.findByVoiceIdAndValidatedTrue(voiceId).stream()
                .map(noteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoteDto> getNotesByAssistantId(UUID assistantId) {
        return jpaNoteRepository.findByAssistantId(assistantId).stream()
                .map(noteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<NoteDto> getNotesByAssistantId(UUID assistantId, Pageable pageable) {
        return jpaNoteRepository.findByAssistantId(assistantId, pageable)
                .map(noteMapper::toDTO);
    }

    @Override
    public List<NoteDto> getNotesByModifierId(UUID modifierId) {
        return jpaNoteRepository.findByModifierId(modifierId).stream()
                .map(noteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<NoteDto> searchNotesByContent(String searchText, Pageable pageable) {
        return jpaNoteRepository.findByContentTxtContainingIgnoreCase(searchText, pageable)
                .map(noteMapper::toDTO);
    }

    @Override
    @Transactional
    public NoteDto validateNote(UUID noteId, UUID modifierId) {
        Note note = jpaNoteRepository.findById(noteId)
                .orElseThrow(() -> new EntityNotFoundException("Note not found with id: " + noteId));

        User modifier = jpaUserRepository.findById(modifierId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + modifierId));

        note.setValidated(true);
        note.setModifier(modifier);

        Note validatedNote = jpaNoteRepository.save(note);
        return noteMapper.toDTO(validatedNote);
    }
}