package com.personnel.personnelservice.adapters.webs;

import com.fasterxml.jackson.annotation.JsonView;
import com.personnel.personnelservice.core.models.dtos.DoctorPatientDto;
import com.personnel.personnelservice.core.models.views.Views;
import com.personnel.personnelservice.core.ports.services.DoctorPatientService;
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
 * REST controller for managing doctor-patient relationships
 */
@RestController
@RequestMapping("/api/doctor-patients")
@RequiredArgsConstructor
@Tag(name = "Doctor-Patient Controller", description = "Operations related to doctor-patient relationships")
public class DoctorPatientController {

    private final DoctorPatientService doctorPatientService;

    @PostMapping
    @Operation(summary = "Create a new doctor-patient relationship", description = "Creates a new doctor-patient relationship with the provided data")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Doctor-patient relationship created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or relationship already exists"),
            @ApiResponse(responseCode = "404", description = "Doctor or patient not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<DoctorPatientDto> createDoctorPatient(
            @Valid @RequestBody @JsonView(Views.Create.class) DoctorPatientDto doctorPatientDto) {
        DoctorPatientDto createdRelationship = doctorPatientService.createDoctorPatient(doctorPatientDto);
        return new ResponseEntity<>(createdRelationship, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a doctor-patient relationship by ID", description = "Returns a doctor-patient relationship as per the ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the doctor-patient relationship"),
            @ApiResponse(responseCode = "404", description = "Doctor-patient relationship not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<DoctorPatientDto> getDoctorPatientById(
            @Parameter(description = "ID of the doctor-patient relationship", required = true) @PathVariable UUID id) {
        DoctorPatientDto relationship = doctorPatientService.getDoctorPatientById(id);
        return ResponseEntity.ok(relationship);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a doctor-patient relationship", description = "Updates an existing doctor-patient relationship with the provided data")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Doctor-patient relationship updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or relationship already exists"),
            @ApiResponse(responseCode = "404", description = "Doctor-patient relationship, doctor, or patient not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<DoctorPatientDto> updateDoctorPatient(
            @Parameter(description = "ID of the doctor-patient relationship", required = true) @PathVariable UUID id,
            @Valid @RequestBody @JsonView(Views.Update.class) DoctorPatientDto doctorPatientDto) {
        doctorPatientDto.setId(id);
        DoctorPatientDto updatedRelationship = doctorPatientService.updateDoctorPatient(doctorPatientDto);
        return ResponseEntity.ok(updatedRelationship);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a doctor-patient relationship", description = "Deletes a doctor-patient relationship by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Doctor-patient relationship deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Doctor-patient relationship not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteDoctorPatient(
            @Parameter(description = "ID of the doctor-patient relationship", required = true) @PathVariable UUID id) {
        doctorPatientService.deleteDoctorPatient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all doctor-patient relationships", description = "Returns a list of all doctor-patient relationships")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of doctor-patient relationships"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<List<DoctorPatientDto>> getAllDoctorPatients() {
        List<DoctorPatientDto> relationships = doctorPatientService.getAllDoctorPatients();
        return ResponseEntity.ok(relationships);
    }

    @GetMapping("/paginated")
    @Operation(summary = "Get paginated doctor-patient relationships", description = "Returns a paginated list of doctor-patient relationships")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list of doctor-patient relationships"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<Page<DoctorPatientDto>> getAllDoctorPatientsPaginated(Pageable pageable) {
        Page<DoctorPatientDto> relationships = doctorPatientService.getAllDoctorPatients(pageable);
        return ResponseEntity.ok(relationships);
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Get doctor-patient relationships by doctor", description = "Returns a list of doctor-patient relationships for a specific doctor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of doctor-patient relationships"),
            @ApiResponse(responseCode = "404", description = "Doctor not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<List<DoctorPatientDto>> getDoctorPatientsByDoctorId(
            @Parameter(description = "ID of the doctor", required = true) @PathVariable UUID doctorId) {
        List<DoctorPatientDto> relationships = doctorPatientService.getDoctorPatientsByDoctorId(doctorId);
        return ResponseEntity.ok(relationships);
    }

    @GetMapping("/doctor/{doctorId}/paginated")
    @Operation(summary = "Get paginated doctor-patient relationships by doctor", description = "Returns a paginated list of doctor-patient relationships for a specific doctor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list of doctor-patient relationships"),
            @ApiResponse(responseCode = "404", description = "Doctor not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<Page<DoctorPatientDto>> getDoctorPatientsByDoctorIdPaginated(
            @Parameter(description = "ID of the doctor", required = true) @PathVariable UUID doctorId,
            Pageable pageable) {
        Page<DoctorPatientDto> relationships = doctorPatientService.getDoctorPatientsByDoctorId(doctorId, pageable);
        return ResponseEntity.ok(relationships);
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get doctor-patient relationships by patient", description = "Returns a list of doctor-patient relationships for a specific patient")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of doctor-patient relationships"),
            @ApiResponse(responseCode = "404", description = "Patient not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<List<DoctorPatientDto>> getDoctorPatientsByPatientId(
            @Parameter(description = "ID of the patient", required = true) @PathVariable UUID patientId) {
        List<DoctorPatientDto> relationships = doctorPatientService.getDoctorPatientsByPatientId(patientId);
        return ResponseEntity.ok(relationships);
    }

    @GetMapping("/patient/{patientId}/paginated")
    @Operation(summary = "Get paginated doctor-patient relationships by patient", description = "Returns a paginated list of doctor-patient relationships for a specific patient")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the paginated list of doctor-patient relationships"),
            @ApiResponse(responseCode = "404", description = "Patient not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<Page<DoctorPatientDto>> getDoctorPatientsByPatientIdPaginated(
            @Parameter(description = "ID of the patient", required = true) @PathVariable UUID patientId,
            Pageable pageable) {
        Page<DoctorPatientDto> relationships = doctorPatientService.getDoctorPatientsByPatientId(patientId, pageable);
        return ResponseEntity.ok(relationships);
    }

    @GetMapping("/doctor/{doctorId}/patient/{patientId}")
    @Operation(summary = "Get doctor-patient relationship by doctor and patient", description = "Returns a specific doctor-patient relationship for a doctor and patient")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the doctor-patient relationship"),
            @ApiResponse(responseCode = "404", description = "Doctor-patient relationship not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<DoctorPatientDto> getDoctorPatientByDoctorIdAndPatientId(
            @Parameter(description = "ID of the doctor", required = true) @PathVariable UUID doctorId,
            @Parameter(description = "ID of the patient", required = true) @PathVariable UUID patientId) {
        return doctorPatientService.getDoctorPatientByDoctorIdAndPatientId(doctorId, patientId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/doctor/{doctorId}/patient/{patientId}")
    @Operation(summary = "Delete doctor-patient relationship by doctor and patient", description = "Deletes a specific doctor-patient relationship for a doctor and patient")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Doctor-patient relationship deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Doctor-patient relationship not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteDoctorPatientByDoctorIdAndPatientId(
            @Parameter(description = "ID of the doctor", required = true) @PathVariable UUID doctorId,
            @Parameter(description = "ID of the patient", required = true) @PathVariable UUID patientId) {
        doctorPatientService.deleteDoctorPatientByDoctorIdAndPatientId(doctorId, patientId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check")
    @Operation(summary = "Check if doctor-patient relationship exists", description = "Checks if a specific doctor-patient relationship exists")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully checked the doctor-patient relationship"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Boolean> existsDoctorPatientByDoctorIdAndPatientId(
            @Parameter(description = "ID of the doctor", required = true) @RequestParam UUID doctorId,
            @Parameter(description = "ID of the patient", required = true) @RequestParam UUID patientId) {
        boolean exists = doctorPatientService.existsDoctorPatientByDoctorIdAndPatientId(doctorId, patientId);
        return ResponseEntity.ok(exists);
    }
}