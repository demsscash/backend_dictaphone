package com.personnel.personnelservice.adapters.webs;

import com.fasterxml.jackson.annotation.JsonView;
import com.personnel.personnelservice.core.models.dtos.NoteDto;
import com.personnel.personnelservice.core.models.views.Views;
import com.personnel.personnelservice.core.ports.services.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing notes
 */
@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
@Tag(name = "Note Controller", description = "Operations related to notes")
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    @Operation(summary = "Create a new note", description = "Creates a new note with the provided data")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Note created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Voice, assistant, or modifier not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<NoteDto> createNote(
            @Valid @RequestBody @JsonView(Views.Create.class) NoteDto noteDto) {
        NoteDto createdNote = noteService.createNote(noteDto);
        return new ResponseEntity<>(createdNote, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a note by ID", description = "Returns a note as per the ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the note"),
            @ApiResponse(responseCode = "404", description = "Note not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<NoteDto> getNoteById(
            @Parameter(description = "ID of the note", required = true) @PathVariable UUID id) {
        NoteDto note = noteService.getNoteById(id);
        return ResponseEntity.ok(note);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a note", description = "Updates an existing note with the provided data")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Note updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Note, voice, assistant, or modifier not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<NoteDto> updateNote(
            @Parameter(description = "ID of the note", required = true) @PathVariable UUID id,
            @Valid @RequestBody @JsonView(Views.Update.class) NoteDto noteDto) {
        noteDto.setId(id);
        NoteDto updatedNote = noteService.updateNote(noteDto);
        return ResponseEntity.ok(updatedNote);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a note", description = "Deletes a note by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Note deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Note not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteNote(
            @Parameter(description = "ID of the note", required = true) @PathVariable UUID id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all notes", description = "Returns a list of all notes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of notes"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<List<NoteDto>> getAllNotes() {
        List<NoteDto> notes = noteService.getAllNotes();
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/paginated")
    @Operation(summary = "Get paginated notes", description = "Returns a paginated list of notes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list of notes"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<Page<NoteDto>> getAllNotesPaginated(Pageable pageable) {
        Page<NoteDto> notes = noteService.getAllNotes(pageable);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/voice/{voiceId}")
    @Operation(summary = "Get notes by voice", description = "Returns a list of notes for a specific voice recording")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of notes"),
            @ApiResponse(responseCode = "404", description = "Voice not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<List<NoteDto>> getNotesByVoiceId(
            @Parameter(description = "ID of the voice recording", required = true) @PathVariable UUID voiceId) {
        List<NoteDto> notes = noteService.getNotesByVoiceId(voiceId);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/voice/{voiceId}/paginated")
    @Operation(summary = "Get paginated notes by voice", description = "Returns a paginated list of notes for a specific voice recording")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list of notes"),
            @ApiResponse(responseCode = "404", description = "Voice not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<Page<NoteDto>> getNotesByVoiceIdPaginated(
            @Parameter(description = "ID of the voice recording", required = true) @PathVariable UUID voiceId,
            Pageable pageable) {
        Page<NoteDto> notes = noteService.getNotesByVoiceId(voiceId, pageable);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/voice/{voiceId}/latest")
    @Operation(summary = "Get latest note by voice", description = "Returns the latest note for a specific voice recording")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the latest note"),
            @ApiResponse(responseCode = "404", description = "Voice not found or no notes available"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<NoteDto> getLatestNoteByVoiceId(
            @Parameter(description = "ID of the voice recording", required = true) @PathVariable UUID voiceId) {
        return noteService.getLatestNoteByVoiceId(voiceId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/voice/{voiceId}/validated")
    @Operation(summary = "Get validated notes by voice", description = "Returns a list of validated notes for a specific voice recording")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of validated notes"),
            @ApiResponse(responseCode = "404", description = "Voice not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<List<NoteDto>> getValidatedNotesByVoiceId(
            @Parameter(description = "ID of the voice recording", required = true) @PathVariable UUID voiceId) {
        List<NoteDto> notes = noteService.getValidatedNotesByVoiceId(voiceId);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/assistant/{assistantId}")
    @Operation(summary = "Get notes by assistant", description = "Returns a list of notes for a specific assistant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of notes"),
            @ApiResponse(responseCode = "404", description = "Assistant not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<List<NoteDto>> getNotesByAssistantId(
            @Parameter(description = "ID of the assistant", required = true) @PathVariable UUID assistantId) {
        List<NoteDto> notes = noteService.getNotesByAssistantId(assistantId);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/assistant/{assistantId}/paginated")
    @Operation(summary = "Get paginated notes by assistant", description = "Returns a paginated list of notes for a specific assistant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list of notes"),
            @ApiResponse(responseCode = "404", description = "Assistant not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<Page<NoteDto>> getNotesByAssistantIdPaginated(
            @Parameter(description = "ID of the assistant", required = true) @PathVariable UUID assistantId,
            Pageable pageable) {
        Page<NoteDto> notes = noteService.getNotesByAssistantId(assistantId, pageable);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/modifier/{modifierId}")
    @Operation(summary = "Get notes by modifier", description = "Returns a list of notes for a specific modifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of notes"),
            @ApiResponse(responseCode = "404", description = "Modifier not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<List<NoteDto>> getNotesByModifierId(
            @Parameter(description = "ID of the modifier", required = true) @PathVariable UUID modifierId) {
        List<NoteDto> notes = noteService.getNotesByModifierId(modifierId);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/search")
    @Operation(summary = "Search notes by content", description = "Returns a paginated list of notes containing the search text in their content")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list of notes"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<Page<NoteDto>> searchNotesByContent(
            @Parameter(description = "Search text", required = true) @RequestParam String searchText,
            Pageable pageable) {
        Page<NoteDto> notes = noteService.searchNotesByContent(searchText, pageable);
        return ResponseEntity.ok(notes);
    }

    @PatchMapping("/{id}/validate")
    @Operation(summary = "Validate a note", description = "Validates a note with the specified modifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Note validated successfully"),
            @ApiResponse(responseCode = "404", description = "Note or modifier not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<NoteDto> validateNote(
            @Parameter(description = "ID of the note", required = true) @PathVariable UUID id,
            @Parameter(description = "ID of the modifier", required = true) @RequestParam UUID modifierId) {
        NoteDto validatedNote = noteService.validateNote(id, modifierId);
        return ResponseEntity.ok(validatedNote);
    }
}