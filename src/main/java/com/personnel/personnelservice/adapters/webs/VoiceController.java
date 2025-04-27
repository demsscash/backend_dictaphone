package com.personnel.personnelservice.adapters.webs;

import com.fasterxml.jackson.annotation.JsonView;
import com.personnel.personnelservice.core.models.dtos.VoiceDto;
import com.personnel.personnelservice.core.models.views.Views;
import com.personnel.personnelservice.core.ports.services.VoiceService;
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
 * REST controller for managing voice recordings
 */
@RestController
@RequestMapping("/api/voices")
@RequiredArgsConstructor
@Tag(name = "Voice Controller", description = "Operations related to voice recordings")
public class VoiceController {

    private final VoiceService voiceService;

    @PostMapping
    @Operation(summary = "Create a new voice recording", description = "Creates a new voice recording with the provided data")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Voice recording created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Doctor or patient not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<VoiceDto> createVoice(
            @Valid @RequestBody @JsonView(Views.Create.class) VoiceDto voiceDto) {
        VoiceDto createdVoice = voiceService.createVoice(voiceDto);
        return new ResponseEntity<>(createdVoice, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a voice recording by ID", description = "Returns a voice recording as per the ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the voice recording"),
            @ApiResponse(responseCode = "404", description = "Voice recording not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<VoiceDto> getVoiceById(
            @Parameter(description = "ID of the voice recording", required = true) @PathVariable UUID id) {
        VoiceDto voice = voiceService.getVoiceById(id);
        return ResponseEntity.ok(voice);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a voice recording", description = "Updates an existing voice recording with the provided data")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Voice recording updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Voice recording, doctor, or patient not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<VoiceDto> updateVoice(
            @Parameter(description = "ID of the voice recording", required = true) @PathVariable UUID id,
            @Valid @RequestBody @JsonView(Views.Update.class) VoiceDto voiceDto) {
        voiceDto.setId(id);
        VoiceDto updatedVoice = voiceService.updateVoice(voiceDto);
        return ResponseEntity.ok(updatedVoice);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a voice recording", description = "Deletes a voice recording by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Voice recording deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Voice recording not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteVoice(
            @Parameter(description = "ID of the voice recording", required = true) @PathVariable UUID id) {
        voiceService.deleteVoice(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all voice recordings", description = "Returns a list of all voice recordings")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of voice recordings"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<List<VoiceDto>> getAllVoices() {
        List<VoiceDto> voices = voiceService.getAllVoices();
        return ResponseEntity.ok(voices);
    }

    @GetMapping("/paginated")
    @Operation(summary = "Get paginated voice recordings", description = "Returns a paginated list of voice recordings")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list of voice recordings"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<Page<VoiceDto>> getAllVoicesPaginated(Pageable pageable) {
        Page<VoiceDto> voices = voiceService.getAllVoices(pageable);
        return ResponseEntity.ok(voices);
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get voice recordings by doctor", description = "Returns a list of voice recordings for a specific doctor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of voice recordings"),
            @ApiResponse(responseCode = "404", description = "Doctor not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<List<VoiceDto>> getVoicesByDoctorId(
            @Parameter(description = "ID of the doctor", required = true) @PathVariable UUID doctorId) {
        List<VoiceDto> voices = voiceService.getVoicesByDoctorId(doctorId);
        return ResponseEntity.ok(voices);
    }

    @GetMapping("/doctor/{doctorId}/paginated")
    @Operation(summary = "Get paginated voice recordings by doctor", description = "Returns a paginated list of voice recordings for a specific doctor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list of voice recordings"),
            @ApiResponse(responseCode = "404", description = "Doctor not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<Page<VoiceDto>> getVoicesByDoctorIdPaginated(
            @Parameter(description = "ID of the doctor", required = true) @PathVariable UUID doctorId,
            Pageable pageable) {
        Page<VoiceDto> voices = voiceService.getVoicesByDoctorId(doctorId, pageable);
        return ResponseEntity.ok(voices);
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get voice recordings by patient", description = "Returns a list of voice recordings for a specific patient")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of voice recordings"),
            @ApiResponse(responseCode = "404", description = "Patient not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<List<VoiceDto>> getVoicesByPatientId(
            @Parameter(description = "ID of the patient", required = true) @PathVariable UUID patientId) {
        List<VoiceDto> voices = voiceService.getVoicesByPatientId(patientId);
        return ResponseEntity.ok(voices);
    }

    @GetMapping("/patient/{patientId}/paginated")
    @Operation(summary = "Get paginated voice recordings by patient", description = "Returns a paginated list of voice recordings for a specific patient")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list of voice recordings"),
            @ApiResponse(responseCode = "404", description = "Patient not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<Page<VoiceDto>> getVoicesByPatientIdPaginated(
            @Parameter(description = "ID of the patient", required = true) @PathVariable UUID patientId,
            Pageable pageable) {
        Page<VoiceDto> voices = voiceService.getVoicesByPatientId(patientId, pageable);
        return ResponseEntity.ok(voices);
    }

    @GetMapping("/doctor/{doctorId}/patient/{patientId}")
    @Operation(summary = "Get voice recordings by doctor and patient", description = "Returns a list of voice recordings for a specific doctor and patient")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of voice recordings"),
            @ApiResponse(responseCode = "404", description = "Doctor or patient not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<List<VoiceDto>> getVoicesByDoctorIdAndPatientId(
            @Parameter(description = "ID of the doctor", required = true) @PathVariable UUID doctorId,
            @Parameter(description = "ID of the patient", required = true) @PathVariable UUID patientId) {
        List<VoiceDto> voices = voiceService.getVoicesByDoctorIdAndPatientId(doctorId, patientId);
        return ResponseEntity.ok(voices);
    }

    @GetMapping("/doctor/{doctorId}/patient/{patientId}/paginated")
    @Operation(summary = "Get paginated voice recordings by doctor and patient", description = "Returns a paginated list of voice recordings for a specific doctor and patient")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list of voice recordings"),
            @ApiResponse(responseCode = "404", description = "Doctor or patient not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<Page<VoiceDto>> getVoicesByDoctorIdAndPatientIdPaginated(
            @Parameter(description = "ID of the doctor", required = true) @PathVariable UUID doctorId,
            @Parameter(description = "ID of the patient", required = true) @PathVariable UUID patientId,
            Pageable pageable) {
        Page<VoiceDto> voices = voiceService.getVoicesByDoctorIdAndPatientId(doctorId, patientId, pageable);
        return ResponseEntity.ok(voices);
    }

    @GetMapping("/search")
    @Operation(summary = "Search voice recordings by title", description = "Returns a paginated list of voice recordings containing the search text in their title")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list of voice recordings"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<Page<VoiceDto>> searchVoicesByTitle(
            @Parameter(description = "Search text", required = true) @RequestParam String searchText,
            Pageable pageable) {
        Page<VoiceDto> voices = voiceService.searchVoicesByTitle(searchText, pageable);
        return ResponseEntity.ok(voices);
    }
}