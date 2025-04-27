package com.personnel.personnelservice.adapters.webs;

import com.fasterxml.jackson.annotation.JsonView;
import com.personnel.personnelservice.core.models.dtos.DoctorAssistantDto;
import com.personnel.personnelservice.core.models.views.Views;
import com.personnel.personnelservice.core.ports.services.DoctorAssistantService;
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
 * REST controller for managing doctor-assistant relationships
 */
@RestController
@RequestMapping("/api/doctor-assistants")
@RequiredArgsConstructor
@Tag(name = "Doctor-Assistant Controller", description = "Operations related to doctor-assistant relationships")
public class DoctorAssistantController {

    private final DoctorAssistantService doctorAssistantService;

    @PostMapping
    @Operation(summary = "Create a new doctor-assistant relationship", description = "Creates a new doctor-assistant relationship with the provided data")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Doctor-assistant relationship created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or relationship already exists"),
            @ApiResponse(responseCode = "404", description = "Doctor or assistant not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<DoctorAssistantDto> createDoctorAssistant(
            @Valid @RequestBody @JsonView(Views.Create.class) DoctorAssistantDto doctorAssistantDto) {
        DoctorAssistantDto createdRelationship = doctorAssistantService.createDoctorAssistant(doctorAssistantDto);
        return new ResponseEntity<>(createdRelationship, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a doctor-assistant relationship by ID", description = "Returns a doctor-assistant relationship as per the ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the doctor-assistant relationship"),
            @ApiResponse(responseCode = "404", description = "Doctor-assistant relationship not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<DoctorAssistantDto> getDoctorAssistantById(
            @Parameter(description = "ID of the doctor-assistant relationship", required = true) @PathVariable UUID id) {
        DoctorAssistantDto relationship = doctorAssistantService.getDoctorAssistantById(id);
        return ResponseEntity.ok(relationship);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a doctor-assistant relationship", description = "Updates an existing doctor-assistant relationship with the provided data")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Doctor-assistant relationship updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or relationship already exists"),
            @ApiResponse(responseCode = "404", description = "Doctor-assistant relationship, doctor, or assistant not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<DoctorAssistantDto> updateDoctorAssistant(
            @Parameter(description = "ID of the doctor-assistant relationship", required = true) @PathVariable UUID id,
            @Valid @RequestBody @JsonView(Views.Update.class) DoctorAssistantDto doctorAssistantDto) {
        doctorAssistantDto.setId(id);
        DoctorAssistantDto updatedRelationship = doctorAssistantService.updateDoctorAssistant(doctorAssistantDto);
        return ResponseEntity.ok(updatedRelationship);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a doctor-assistant relationship", description = "Deletes a doctor-assistant relationship by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Doctor-assistant relationship deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Doctor-assistant relationship not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteDoctorAssistant(
            @Parameter(description = "ID of the doctor-assistant relationship", required = true) @PathVariable UUID id) {
        doctorAssistantService.deleteDoctorAssistant(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all doctor-assistant relationships", description = "Returns a list of all doctor-assistant relationships")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of doctor-assistant relationships"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<List<DoctorAssistantDto>> getAllDoctorAssistants() {
        List<DoctorAssistantDto> relationships = doctorAssistantService.getAllDoctorAssistants();
        return ResponseEntity.ok(relationships);
    }

    @GetMapping("/paginated")
    @Operation(summary = "Get paginated doctor-assistant relationships", description = "Returns a paginated list of doctor-assistant relationships")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list of doctor-assistant relationships"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<Page<DoctorAssistantDto>> getAllDoctorAssistantsPaginated(Pageable pageable) {
        Page<DoctorAssistantDto> relationships = doctorAssistantService.getAllDoctorAssistants(pageable);
        return ResponseEntity.ok(relationships);
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get doctor-assistant relationships by doctor", description = "Returns a list of doctor-assistant relationships for a specific doctor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of doctor-assistant relationships"),
            @ApiResponse(responseCode = "404", description = "Doctor not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<List<DoctorAssistantDto>> getDoctorAssistantsByDoctorId(
            @Parameter(description = "ID of the doctor", required = true) @PathVariable UUID doctorId) {
        List<DoctorAssistantDto> relationships = doctorAssistantService.getDoctorAssistantsByDoctorId(doctorId);
        return ResponseEntity.ok(relationships);
    }

    @GetMapping("/doctor/{doctorId}/paginated")
    @Operation(summary = "Get paginated doctor-assistant relationships by doctor", description = "Returns a paginated list of doctor-assistant relationships for a specific doctor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list of doctor-assistant relationships"),
            @ApiResponse(responseCode = "404", description = "Doctor not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<Page<DoctorAssistantDto>> getDoctorAssistantsByDoctorIdPaginated(
            @Parameter(description = "ID of the doctor", required = true) @PathVariable UUID doctorId,
            Pageable pageable) {
        Page<DoctorAssistantDto> relationships = doctorAssistantService.getDoctorAssistantsByDoctorId(doctorId, pageable);
        return ResponseEntity.ok(relationships);
    }

    @GetMapping("/assistant/{assistantId}")
    @Operation(summary = "Get doctor-assistant relationships by assistant", description = "Returns a list of doctor-assistant relationships for a specific assistant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of doctor-assistant relationships"),
            @ApiResponse(responseCode = "404", description = "Assistant not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<List<DoctorAssistantDto>> getDoctorAssistantsByAssistantId(
            @Parameter(description = "ID of the assistant", required = true) @PathVariable UUID assistantId) {
        List<DoctorAssistantDto> relationships = doctorAssistantService.getDoctorAssistantsByAssistantId(assistantId);
        return ResponseEntity.ok(relationships);
    }

    @GetMapping("/assistant/{assistantId}/paginated")
    @Operation(summary = "Get paginated doctor-assistant relationships by assistant", description = "Returns a paginated list of doctor-assistant relationships for a specific assistant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list of doctor-assistant relationships"),
            @ApiResponse(responseCode = "404", description = "Assistant not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<Page<DoctorAssistantDto>> getDoctorAssistantsByAssistantIdPaginated(
            @Parameter(description = "ID of the assistant", required = true) @PathVariable UUID assistantId,
            Pageable pageable) {
        Page<DoctorAssistantDto> relationships = doctorAssistantService.getDoctorAssistantsByAssistantId(assistantId, pageable);
        return ResponseEntity.ok(relationships);
    }

    @GetMapping("/doctor/{doctorId}/assistant/{assistantId}")
    @Operation(summary = "Get doctor-assistant relationship by doctor and assistant", description = "Returns a specific doctor-assistant relationship for a doctor and assistant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the doctor-assistant relationship"),
            @ApiResponse(responseCode = "404", description = "Doctor-assistant relationship not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<DoctorAssistantDto> getDoctorAssistantByDoctorIdAndAssistantId(
            @Parameter(description = "ID of the doctor", required = true) @PathVariable UUID doctorId,
            @Parameter(description = "ID of the assistant", required = true) @PathVariable UUID assistantId) {
        return doctorAssistantService.getDoctorAssistantByDoctorIdAndAssistantId(doctorId, assistantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/doctor/{doctorId}/assistant/{assistantId}")
    @Operation(summary = "Delete doctor-assistant relationship by doctor and assistant", description = "Deletes a specific doctor-assistant relationship for a doctor and assistant")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Doctor-assistant relationship deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Doctor-assistant relationship not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteDoctorAssistantByDoctorIdAndAssistantId(
            @Parameter(description = "ID of the doctor", required = true) @PathVariable UUID doctorId,
            @Parameter(description = "ID of the assistant", required = true) @PathVariable UUID assistantId) {
        doctorAssistantService.deleteDoctorAssistantByDoctorIdAndAssistantId(doctorId, assistantId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check")
    @Operation(summary = "Check if doctor-assistant relationship exists", description = "Checks if a specific doctor-assistant relationship exists")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully checked the doctor-assistant relationship"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> existsDoctorAssistantByDoctorIdAndAssistantId(
            @Parameter(description = "ID of the doctor", required = true) @RequestParam UUID doctorId,
            @Parameter(description = "ID of the assistant", required = true) @RequestParam UUID assistantId) {
        boolean exists = doctorAssistantService.existsDoctorAssistantByDoctorIdAndAssistantId(doctorId, assistantId);
        return ResponseEntity.ok(exists);
    }
}