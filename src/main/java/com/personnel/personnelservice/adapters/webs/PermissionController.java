package com.personnel.personnelservice.adapters.webs;
import com.fasterxml.jackson.annotation.JsonView;
import com.personnel.personnelservice.core.models.views.Views;
import com.personnel.personnelservice.core.services.PermissionServiceImpl;
import com.personnel.personnelservice.core.models.dtos.PermissionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/permissions")
@AllArgsConstructor
public class PermissionController {

    private PermissionServiceImpl permissionService;
    @Operation(summary = "Obtenir une permission par ID", description = "Récupère une permission en fonction de son identifiant.")
    @GetMapping("/{id}")
    @JsonView(Views.Response.class)
    public ResponseEntity<PermissionDto> getPermissionById(
            @Parameter(description = "ID de la permission") @PathVariable UUID id) {
        PermissionDto permissionResponseDto = permissionService.getPermissionById(id);
        return ResponseEntity.ok(permissionResponseDto);
    }

    @Operation(summary = "Obtenir toutes les permissions", description = "Récupère la liste de toutes les permissions.")
    @GetMapping
    @JsonView(Views.Response.class)
    public ResponseEntity<List<PermissionDto>> getAllPermissions() {
        List<PermissionDto> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }
}