package com.personnel.personnelservice.adapters.webs;

import com.personnel.personnelservice.core.models.dtos.RoleDto;
import com.personnel.personnelservice.core.models.dtos.UserDto;
import com.personnel.personnelservice.core.ports.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class UserRoleController {

    private final UserService userService;

    @PostMapping("/{userId}/roles/{roleId}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> addRoleToUser(
            @PathVariable UUID userId,
            @PathVariable UUID roleId) {
        UserDto response = userService.addRoleToUser(userId, roleId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> removeRoleFromUser(
            @PathVariable UUID userId,
            @PathVariable UUID roleId) {
        UserDto response = userService.removeRoleFromUser(userId, roleId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}/roles")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> updateUserRoles(
            @PathVariable UUID userId,
            @RequestBody Set<UUID> roleIds) {
        UserDto response = userService.updateUserRoles(userId, roleIds);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/roles")
    //@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Set<RoleDto>> getUserRoles(@PathVariable UUID userId) {
        Set<RoleDto> roles = userService.getUserRoles(userId);
        return ResponseEntity.ok(roles);
    }
}