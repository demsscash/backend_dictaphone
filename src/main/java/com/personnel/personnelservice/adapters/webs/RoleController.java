package com.personnel.personnelservice.adapters.webs;

import com.fasterxml.jackson.annotation.JsonView;
import com.personnel.personnelservice.core.models.dtos.RoleDto;
import com.personnel.personnelservice.core.models.dtos.RoleDto;
import com.personnel.personnelservice.core.models.views.Views;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import com.personnel.personnelservice.core.services.RoleServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/roles")
@AllArgsConstructor
public class RoleController {

    private RoleServiceImpl roleService;

    @Operation(summary = "Obtenir un rôle par ID", description = "Récupère un rôle en fonction de son identifiant.")
    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable UUID id) {
        RoleDto roleResponseDto = roleService.getRoleById(id);
        return ResponseEntity.ok(roleResponseDto);
    }

    @Operation(summary = "Obtenir tous les rôles", description = "Récupère la liste de tous les rôles.")
    @GetMapping
    @JsonView(Views.Response.class)
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<RoleDto> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @Operation(summary = "Créer un rôle", description = "Crée un nouveau rôle.")
    @PostMapping
    @JsonView(Views.Response.class)
    public ResponseEntity<RoleDto> createRole(@RequestBody @JsonView(Views.Create.class) RoleDto roleRequestDto) {
        RoleDto createdRole = roleService.createRole(roleRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    @Operation(summary = "Mettre à jour un rôle", description = "Met à jour un rôle existant.")
    @PutMapping("/{id}")
    @JsonView(Views.Response.class)
    public ResponseEntity<RoleDto> updateRole(@PathVariable UUID id, @RequestBody @JsonView(Views.Update.class) RoleDto roleRequestDto) {
        RoleDto updatedRole = roleService.updateRole(id, roleRequestDto);
        return ResponseEntity.ok(updatedRole);
    }

    @Operation(summary = "Supprimer un rôle", description = "Supprime un rôle existant.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable UUID id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok("Role supprimé avec succès.");
    }

    @Operation(summary = "Ajouter des permissions à un rôle", description = "Associe des permissions à un rôle spécifique.")
    @PostMapping("/{roleId}/permissions")
    @JsonView(Views.Response.class)
    public ResponseEntity<RoleDto> addPermissionsToRole(
            @PathVariable UUID roleId,
            @RequestBody Set<UUID> permissionIds) {
        return ResponseEntity.ok(roleService.addPermissionsToRole(roleId, permissionIds));
    }
}