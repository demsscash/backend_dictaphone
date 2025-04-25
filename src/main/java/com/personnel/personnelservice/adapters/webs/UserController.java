package com.personnel.personnelservice.adapters.webs;

import com.fasterxml.jackson.annotation.JsonView;
import com.personnel.personnelservice.core.models.dtos.UserDto;
import com.personnel.personnelservice.core.models.views.Views;
import com.personnel.personnelservice.core.ports.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "User Management", description = "Operations related to user management")
public class UserController {
    private final UserService userService;

    @PatchMapping()
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    @Operation(summary = "Update user", description = "Updates an existing user's details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<UserDto> updateUser(@RequestBody @Valid @JsonView(Views.Update.class) UserDto userDto) {
        UserDto updatedUser = userService.updateUser(userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes an existing user by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User supprimé avec succès.");
    }
    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves all users.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
