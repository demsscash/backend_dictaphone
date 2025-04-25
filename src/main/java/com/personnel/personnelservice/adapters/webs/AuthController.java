package com.personnel.personnelservice.adapters.webs;

import com.fasterxml.jackson.annotation.JsonView;
import com.personnel.personnelservice.core.models.dtos.*;
import com.personnel.personnelservice.core.models.views.Views;
import com.personnel.personnelservice.core.ports.services.AuthService;
import com.personnel.personnelservice.core.ports.services.ForgotPasswordUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin("*")
@Tag(name = "Auth Controller", description = "This controller handles authentication related operations")
public class AuthController {
    private final AuthService authService;
    private final ForgotPasswordUseCase forgotPasswordUseCase;

    @PostMapping("/registerUser")
    @Operation(summary = "Register a new user", description = "This endpoint allows the registration of a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "user already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<?> registerUser(@RequestBody @Valid @JsonView(Views.Create.class) UserDto userRequest) {
        UserDto registeredUser = authService.registerUser(userRequest);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/registerPatient")
    @Operation(summary = "Register a new patient", description = "This endpoint allows the registration of a new patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient registered successfully"),
            @ApiResponse(responseCode = "400", description = "user already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<?> registerPatient(@RequestBody @Valid @JsonView(Views.Create.class) PatientDto userRequest) {
        PatientDto registeredUser = authService.registerPatient(userRequest);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/registerMedecin")
    @Operation(summary = "Register a new doctor", description = "This endpoint allows the registration of a new doctor (medecin)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Register registered successfully"),
            @ApiResponse(responseCode = "400", description = "user already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<?> registerMedecin(@RequestBody @Valid @JsonView(Views.Create.class) MedecinDto medecinRequest) {
        MedecinDto registeredUser = authService.registerMedecin(medecinRequest);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/registerAssistant")
    @Operation(summary = "Register a new assistant", description = "This endpoint allows the registration of a new assistant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assistant registered successfully"),
            @ApiResponse(responseCode = "400", description = "user already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @JsonView(Views.Response.class)
    public ResponseEntity<?> registerAssistant(@RequestBody @Valid @JsonView(Views.Create.class) AssistantDto userRequest) {
        AssistantDto registeredUser = authService.registerAssistant(userRequest);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/loginMedecin")
    @Operation(summary = "Login doctor", description = "This endpoint allows a doctor to log in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor logged in successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "404", description = " user not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "none")
    public ResponseEntity<?> loginMedecin(@RequestBody @Valid LoginDto loginDto) {
        JwtResponseDto jwtResponseDto = authService.loginMedecin(loginDto);
        return ResponseEntity.ok(jwtResponseDto);
    }

    @PostMapping("/loginPatient")
    @Operation(summary = "Login patient", description = "This endpoint allows a patient to log in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient logged in successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "404", description = " user not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "none")
    public ResponseEntity<?> loginPatient(@RequestBody @Valid LoginDto loginDto) {
        JwtResponseDto jwtResponseDto = authService.loginPatient(loginDto);
        return ResponseEntity.ok(jwtResponseDto);
    }

    @PostMapping("/loginUser")
    @Operation(summary = "Login user", description = "This endpoint allows a user to log in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "404", description = " user not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "none")
    public ResponseEntity<?> loginUser(@RequestBody @Valid LoginDto loginDto) {
        JwtResponseDto loggedUser = authService.login(loginDto);
        return ResponseEntity.ok(loggedUser);
    }

    @PostMapping("/loginAssistant")
    @Operation(summary = "Login assistant", description = "This endpoint allows an assistant to log in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assistant logged in successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "404", description = " user not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "none")
    public ResponseEntity<?> loginAssistant(@RequestBody @Valid LoginDto loginDto) {
        JwtResponseDto jwtResponseDto = authService.loginAssistant(loginDto);
            return ResponseEntity.ok(jwtResponseDto);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String responseMessage = forgotPasswordUseCase.forgotPassword(email);

        return ResponseEntity.ok(Map.of("message", responseMessage));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestHeader("Authorization") String header,
                                           @RequestBody ResetPasswordDto resetPasswordDto) {
        if (resetPasswordDto.getPassword() == null || resetPasswordDto.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Le mot de passe est requis");
        }

        header = header.substring(7);
        resetPasswordDto.setToken(header);

        String response = forgotPasswordUseCase.resetPassword(resetPasswordDto);
        return ResponseEntity.ok(Map.of("message", response));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user info", description = "Retrieve information of the authenticated user")
    @SecurityRequirement(name = "bearerAuth")
    @JsonView(Views.Response.class)
    public ResponseEntity<?> getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        PatientDto userDto = authService.getCurrentUser(email);
        return ResponseEntity.ok(userDto);
    }
}
